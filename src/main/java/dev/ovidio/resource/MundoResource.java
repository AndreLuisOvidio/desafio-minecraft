package dev.ovidio.resource;

import dev.ovidio.entity.Bloco;
import dev.ovidio.entity.Coordenadas;
import dev.ovidio.entity.Item;
import dev.ovidio.entity.Mundo;
import dev.ovidio.exception.BaseException;
import dev.ovidio.record.ColocarBlocoRecord;
import dev.ovidio.service.MundoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import java.util.UUID;

@Path("/mundo/{uuid}")
public class MundoResource {

    @Inject
    MundoService mundoService;

    @GET
    public Mundo recuperaMundo(@PathParam("uuid") UUID uuid) {
        return mundoService.recuperaMundo(uuid);
    }

    @POST
    @Path("/colocarBloco")
    public Bloco colocarBloco(@PathParam("uuid") UUID uuid, ColocarBlocoRecord colocarBlocoRecord) throws BaseException {
        return mundoService.colocarBloco(colocarBlocoRecord, uuid);
    }

    @DELETE
    @Path("/quebrarBloco")
    public Item quebrarBloco(@PathParam("uuid") UUID uuid, Coordenadas coordenadas) throws BaseException {
        return mundoService.quebrarBloco(coordenadas, uuid);
    }

}
