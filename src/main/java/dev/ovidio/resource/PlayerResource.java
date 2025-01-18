package dev.ovidio.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ovidio.entity.Player;
import dev.ovidio.entity.SlotInventario;
import dev.ovidio.exception.BaseException;
import dev.ovidio.exception.InventarioLotadoException;
import dev.ovidio.exception.ItemNaoEncontradoException;
import dev.ovidio.record.AcaoMoverResponseRecord;
import dev.ovidio.record.ColetarItemRecord;
import dev.ovidio.record.MoverItemRecord;
import dev.ovidio.record.RemoverDurabilidadeRecord;
import dev.ovidio.service.PlayerService;
import dev.ovidio.type.CodigoSlot;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/player")
public class PlayerResource {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    PlayerService playerService;

    @GET
    @Path("uuid")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public UUID randomUuid() {
        return UUID.randomUUID();
    }

    @GET()
    @Path("{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Player recuperarInventario(@PathParam("uuid") UUID uuid) {
        return playerService.recuperaPlayer(uuid);
    }

    @POST
    @Path("{uuid}/item/coletar")
    @Produces(MediaType.APPLICATION_JSON)
    public SlotInventario coletarItem(@Valid ColetarItemRecord item, @PathParam("uuid") UUID uuid) throws InventarioLotadoException {
        return playerService.coletarItem(item, uuid);
    }

    @PUT
    @Path("{uuid}/item/removerDurabilidade")
    public Response removerDurabilidade(@Valid RemoverDurabilidadeRecord request, @PathParam("uuid") UUID uuid) throws ItemNaoEncontradoException {
        try {
            CodigoSlot codigoSlot = Enum.valueOf(CodigoSlot.class, request.nomeSlot());
            return Response.status(Response.Status.OK)
                    .entity(playerService.removerDurabilidade(codigoSlot, request.quantidadeRemover(), uuid))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.OK)
                    .entity(playerService.removerDurabilidade(request.nomeSlot(), request.quantidadeRemover(), uuid))
                    .build();
        }
    }

    @DELETE
    @Path("{uuid}/item/dropar/{slot}/{quantidade}")
    public Response removerItem(@PathParam("slot") String slot, @PathParam("quantidade") int quantidade, @PathParam("uuid") UUID uuid) throws ItemNaoEncontradoException {
        try {
            CodigoSlot codigoSlot = Enum.valueOf(CodigoSlot.class, slot);
            return Response.status(Response.Status.OK)
                    .entity(playerService.removerItem(codigoSlot, quantidade, uuid))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.OK)
                    .entity(playerService.removerItem(slot, uuid))
                    .build();
        }
    }

    @POST
    @Path("{uuid}/item/mover")
    public AcaoMoverResponseRecord moverItem(MoverItemRecord moverItem, @PathParam("uuid") UUID uuid) throws BaseException {
        return playerService.moverItem(moverItem, uuid);
    }

}
