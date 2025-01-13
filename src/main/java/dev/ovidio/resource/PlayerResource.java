package dev.ovidio.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ovidio.MyEntity;
import dev.ovidio.entity.Inventario;
import dev.ovidio.entity.Item;
import dev.ovidio.entity.Player;
import dev.ovidio.entity.SlotInventario;
import dev.ovidio.type.CodigoSlot;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.UUID;

@Path("/player")
public class PlayerResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String teste() throws JsonProcessingException {

        var uuid = geraUmPlayer();

        var player = Player.find("uuid", uuid);

        return "Hello from Quarkus REST: "+player.<Player>firstResult().toString();
    }

    private static UUID geraUmPlayer() {
        UUID uuid = UUID.randomUUID();

        var player = new Player();
        player.uuid = uuid;
        player.inventario = new Inventario();

        player.inventario.capacete = new Item("Capacete");
        player.inventario.peitoral = new Item("Peitoral");
        player.inventario.calca = new Item("Calça");
        player.inventario.bota = new Item("Bota");

        Item itemSlot = new Item("Machado");
        player.inventario.slots = new ArrayList<>();
        player.inventario.slots.add(new SlotInventario(itemSlot, CodigoSlot.A1, player.inventario));

        Item.persist(player.inventario.capacete,
                player.inventario.peitoral,
                player.inventario.calca,
                player.inventario.bota,
                itemSlot);

        Inventario.persist(player.inventario);
        SlotInventario.persist(player.inventario.slots);
        Player.persist(player);
        return uuid;
    }

}
