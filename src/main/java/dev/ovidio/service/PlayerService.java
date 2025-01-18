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
            case "capacete" -> inventario.capacete;
            case "peitoral" -> inventario.peitoral;
            case "calca" -> inventario.calca;
            case "bota" -> inventario.bota;
            default -> throw new IllegalArgumentException("nomeSlote precisa ser um codigo de slot ou uma peça da armadura (capacete, peitoral, calca, bota)");
        };
        if (item == null) {
            throw new ItemNaoEncontradoException("Não encontrado nenhum item equipado no slot: " + nomePeca);
        }
        item.durabilidade = item.durabilidade - qtdRemover;
        if (item.durabilidade < 1) {
            switch (nomePeca) {
                case "capacete" -> inventario.capacete = null;
                case "peitoral" -> inventario.peitoral = null;
                case "calca" -> inventario.calca = null;
                case "bota" -> inventario.bota = null;
                default -> throw new IllegalArgumentException("nomeSlote precisa ser um codigo de slot ou uma peça da armadura (capacete, peitoral, calca, bota)");
            }
            Item.deleteById(item.id);
        }
        return item;
    }
}
