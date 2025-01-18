package dev.ovidio.service;

import dev.ovidio.entity.Inventario;
import dev.ovidio.entity.Item;
import dev.ovidio.entity.Player;
import dev.ovidio.entity.SlotInventario;
import dev.ovidio.exception.InventarioLotadoException;
import dev.ovidio.exception.ItemNaoEncontradoException;
import dev.ovidio.record.ColetarItemRecord;
import dev.ovidio.type.CodigoSlot;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class PlayerService {

    public static final String CAPACETE = "capacete";
    public static final String PEITORAL = "peitoral";
    public static final String CALCA = "calca";
    public static final String BOTA = "bota";

    @Transactional
    public Player recuperaPlayer(UUID uuid) {
        return Player.<Player>find("uuid", uuid)
                .singleResultOptional()
                .orElseGet(() -> novoPlayer(uuid));
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
        Item.persist(slotInventario.item);
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
            Item item = slotInventario.item;
            slotInventario.item = null;
            SlotInventario.persist(slotInventario);
            Item.deleteById(item.id);
        }
        return slotInventario;
    }

    @Transactional
    public Item removerDurabilidade(@NotBlank String nomePeca, int qtdRemover, UUID uuid) throws ItemNaoEncontradoException {
        Inventario inventario = recuperaPlayer(uuid).inventario;
        final Item item = switch (nomePeca) {
            case CAPACETE -> inventario.capacete;
            case PEITORAL -> inventario.peitoral;
            case CALCA -> inventario.calca;
            case BOTA -> inventario.bota;
            default -> throw nomeSloteArmaduraInvalido();
        };
        if (item == null) {
            throw new ItemNaoEncontradoException("Não encontrado nenhum item equipado no slot: " + nomePeca);
        }
        item.durabilidade = item.durabilidade - qtdRemover;
        if (item.durabilidade < 1) {
            switch (nomePeca) {
                case CAPACETE -> inventario.capacete = null;
                case PEITORAL -> inventario.peitoral = null;
                case CALCA -> inventario.calca = null;
                case BOTA -> inventario.bota = null;
                default -> throw  nomeSloteArmaduraInvalido();
            }
            Item.deleteById(item.id);
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
            Item item = slotInventario.item;
            slotInventario.item = null;
            SlotInventario.persist(slotInventario);
            Item.deleteById(item.id);
        }
        return slotInventario;
    }

    @Transactional
    public Object removerItem(String pecaArmadura, UUID uuid) {
        Inventario inventario = recuperaPlayer(uuid).inventario;
        Item item = switch (pecaArmadura) {
            case CAPACETE -> {
                var i = inventario.capacete;
                inventario.capacete = null;
                yield i;
            }
            case PEITORAL -> {
                var i = inventario.peitoral;
                inventario.peitoral = null;
                yield i;
            }
            case CALCA-> {
                var i = inventario.calca;
                inventario.calca = null;
                yield i;
            }
            case BOTA -> {
                var i = inventario.bota;
                inventario.bota = null;
                yield i;
            }
            default -> throw nomeSloteArmaduraInvalido();
        };
        Item.deleteById(item.id);
        return item;
    }
}
