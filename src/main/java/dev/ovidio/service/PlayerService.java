package dev.ovidio.service;

import dev.ovidio.entity.*;
import dev.ovidio.exception.BaseException;
import dev.ovidio.exception.InventarioLotadoException;
import dev.ovidio.exception.ItemNaoEncontradoException;
import dev.ovidio.exception.SlotJaPreenchidoException;
import dev.ovidio.record.AcaoMoverResponseRecord;
import dev.ovidio.record.ColetarItemRecord;
import dev.ovidio.record.MoverItemRecord;
import dev.ovidio.type.CodigoSlot;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;

import java.util.*;

@ApplicationScoped
public class PlayerService {

    public static final String CAPACETE = "capacete";
    public static final String PEITORAL = "peitoral";
    public static final String CALCA = "calca";
    public static final String BOTA = "bota";

    @Transactional
    public Player recuperaPlayer(UUID uuid) {
        var player = Player.<Player>find("uuid", uuid)
                .singleResultOptional()
                .orElseGet(() -> novoPlayer(uuid));
        if (player.inventario.armadura == null) player.inventario.armadura = new Armadura();
        return player;
    }

    private Player novoPlayer(UUID uuid) {
        var player = new Player();
        player.uuid = uuid;
        player.inventario = novoInventario();
        Player.persist(player);
        return player;
    }

    private Inventario novoInventario() {
        Inventario inventario = new Inventario();
        Inventario.persist(inventario);
        inventario.slots = novosSlotes(inventario);
        return inventario;
    }

    private List<SlotInventario> novosSlotes(Inventario inventario) {
        List<SlotInventario> slots = Arrays.stream(CodigoSlot.values())
                .map(codigoSlot -> new SlotInventario(codigoSlot, inventario))
                .toList();
        SlotInventario.persist(slots);
        return slots;
    }

    @Transactional
    public SlotInventario coletarItem(ColetarItemRecord item, UUID uuid) throws InventarioLotadoException {
        Inventario inventario = recuperaPlayer(uuid)
                .inventario;

        List<SlotInventario> slotInventarios = inventario.getSlotsOrdenados();

        SlotInventario slotInventario = slotInventarios.stream()
                .filter(slot -> slot.item == null
                        || (slot.item.nome.equals(item.nome()) && Objects.equals(item.durabilidade(), slot.item.durabilidade)) )
                .findFirst()
                .orElseThrow(InventarioLotadoException::new);

        if (slotInventario.item == null) {
            slotInventario.item = new Item(item.nome(), item.durabilidade());
        }
        slotInventario.quantidade = slotInventario.quantidade + item.quantidade();
        SlotInventario.persist(slotInventario);
        return slotInventario;
    }

    @Transactional
    public SlotInventario removerDurabilidade(CodigoSlot codigoSlot, int qtdRemover, UUID uuid) throws ItemNaoEncontradoException {
        Inventario inventario = recuperaPlayer(uuid)
                .inventario;

        SlotInventario slotInventario = inventario.slots.stream()
                .filter(slot ->
                    slot.codigo.equals(codigoSlot)
                    && slot.item != null && slot.item.durabilidade != null
                )
                .findFirst()
                .orElseThrow(() -> new ItemNaoEncontradoException("Não encontrado item nesse slot que tenha durabilidade."));
        slotInventario.item.durabilidade = slotInventario.item.durabilidade - qtdRemover;
        if (slotInventario.item.durabilidade < 1) {
            slotInventario.item = null;
            SlotInventario.persist(slotInventario);
        }
        return slotInventario;
    }

    @Transactional
    public Item removerDurabilidade(@NotBlank String nomePeca, int qtdRemover, UUID uuid) throws ItemNaoEncontradoException {
        Inventario inventario = recuperaPlayer(uuid).inventario;
        final Item item = encontrarItemArmadura(inventario, nomePeca);
        if (item == null) {
            throw new ItemNaoEncontradoException("Não encontrado nenhum item equipado no slot: " + nomePeca);
        }
        item.durabilidade = item.durabilidade - qtdRemover;
        if (item.durabilidade < 1) {
            removeArmadura(nomePeca, inventario);
        }
        return item;
    }

    private static IllegalArgumentException nomeSloteArmaduraInvalido() {
        return new IllegalArgumentException("nomeSlote precisa ser um codigo de slot ou uma peça da armadura (capacete, peitoral, calca, bota)");
    }

    @Transactional
    public SlotInventario removerItem(CodigoSlot codigoSlot, int qtdRemover, UUID uuid) throws ItemNaoEncontradoException {
        SlotInventario slotInventario = recuperaPlayer(uuid).inventario.slots
                .stream()
                .filter(slot -> slot.codigo.equals(codigoSlot) && slot.item != null)
                .findFirst()
                .orElseThrow(() -> new ItemNaoEncontradoException("Nenhum item não encontrado no slot: "+codigoSlot));

        slotInventario.quantidade = slotInventario.quantidade - qtdRemover;

        if (slotInventario.quantidade < 1) {
            slotInventario.item = null;
            SlotInventario.persist(slotInventario);
        }
        return slotInventario;
    }

    @Transactional
    public Armadura removerItem(String pecaArmadura, UUID uuid) {
        Inventario inventario = recuperaPlayer(uuid).inventario;
        removeArmadura(pecaArmadura, inventario);
        return inventario.armadura;
    }

    private void removeArmadura(String pecaArmadura, Inventario inventario) {
        setaItemArmadura(pecaArmadura, inventario , null);
    }

    private static void setaItemArmadura(String pecaArmadura, Inventario inventario, Item item) {
        switch (pecaArmadura) {
            case CAPACETE -> inventario.armadura.capacete = item;
            case PEITORAL -> inventario.armadura.peitoral = item;
            case CALCA-> inventario.armadura.calca = item;
            case BOTA -> inventario.armadura.bota = item;

            default -> throw nomeSloteArmaduraInvalido();
        }
    }

    private static Item encontrarItemArmadura(Inventario inventario, String codigoSlotStr) {
        return switch (codigoSlotStr) {
            case CAPACETE -> inventario.armadura.capacete;
            case PEITORAL -> inventario.armadura.peitoral;
            case CALCA -> inventario.armadura.calca;
            case BOTA -> inventario.armadura.bota;
            default -> throw nomeSloteArmaduraInvalido();
        };
    }

    @Transactional
    public AcaoMoverResponseRecord moverItem(MoverItemRecord moverItem, UUID uuid) throws BaseException {
        Inventario inventario = recuperaPlayer(uuid).inventario;

        SlotInventario origem = slotOf(inventario, moverItem.slotOrigem());
        SlotInventario destino = slotOf(inventario, moverItem.slotDestino());

        if (origem.item == null) {
            throw new ItemNaoEncontradoException("Nenhum item encontrado no slot de origem");
        }
        int qtdMover = moverItem.quantidade() != null ? moverItem.quantidade() : origem.quantidade;
        if (qtdMover > origem.quantidade) qtdMover = origem.quantidade;

        if (origem.item.equals(destino.item)
            && destino.codigo != null) { // codigo = null slot de armadura
            destino.quantidade = destino.quantidade + qtdMover;
        } else if (destino.item == null) {
            if (destino.codigo == null) {
                setaItemArmadura(moverItem.slotDestino(), inventario, origem.item);
            } else {
                destino.item = origem.item;
                destino.quantidade = qtdMover;
            }
        } else {
            throw new SlotJaPreenchidoException();
        }

        origem.quantidade = origem.quantidade - qtdMover;
        if (origem.quantidade < 1) {
            origem.quantidade = 0;
            if (origem.codigo == null) { // codigo = null slot de armadura
                removeArmadura(moverItem.slotOrigem(), inventario);
            } else {
                origem.item = null;
            }
        }
        List<SlotInventario> slotsAlterados = new ArrayList<>();
        if (origem.codigo != null) {
            SlotInventario.persist(origem);
            slotsAlterados.add(origem);
        }
        if (destino.codigo != null) {
            SlotInventario.persist(destino);
            slotsAlterados.add(destino);
        }
        Inventario.persist(inventario);
        return new AcaoMoverResponseRecord(slotsAlterados, inventario.armadura);
    }

    private SlotInventario slotOf(Inventario inventario, String codigoSlotStr) throws ItemNaoEncontradoException {
        try {
            CodigoSlot codigoSlot = Enum.valueOf(CodigoSlot.class, codigoSlotStr);
            return inventario.slots.stream().filter(slot -> slot.codigo.equals(codigoSlot))
                    .findFirst()
                    .orElseThrow(() -> new ItemNaoEncontradoException("Não encontrado o slot: "+codigoSlot));
        } catch (IllegalArgumentException e) {
            Item item = encontrarItemArmadura(inventario, codigoSlotStr);
            return new SlotInventario(item);
        }
    }
}
