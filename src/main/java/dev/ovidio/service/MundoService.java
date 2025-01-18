package dev.ovidio.service;

import dev.ovidio.entity.*;
import dev.ovidio.exception.BaseException;
import dev.ovidio.exception.BlocoPreenchidoException;
import dev.ovidio.exception.ItemNaoEncontradoException;
import dev.ovidio.record.ColocarBlocoRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@ApplicationScoped
public class MundoService {

    @Inject
    PlayerService playerService;

    @Transactional
    public Mundo recuperaMundo(UUID uuid) {
        return Mundo.<Mundo>find("player.uuid", uuid)
                .singleResultOptional()
                .orElseGet(() -> novoMundo(uuid));
    }

    private Mundo novoMundo(UUID uuid) {
        Mundo mundo = new Mundo();
        mundo.player = playerService.recuperaPlayer(uuid);
        Mundo.persist(mundo);
        return mundo;
    }

    @Transactional
    public Bloco colocarBloco(ColocarBlocoRecord colocarBlocoRecord, UUID uuid) throws BaseException {
        Bloco bloco = recuperaBloco(colocarBlocoRecord.coordenadas(), uuid);
        if (bloco.item != null) {
            throw new BlocoPreenchidoException();
        }

        SlotInventario slotInventario = playerService.slotOf(uuid, colocarBlocoRecord.slotItem());
        if (slotInventario.item == null) {
            throw new ItemNaoEncontradoException("Não encontrado nenhum item no slot: "+slotInventario);
        }
        bloco.item = slotInventario.item;
        playerService.removerItem(slotInventario.codigo, 1, uuid);
        Bloco.persist(bloco);
        return bloco;
    }

    private Bloco recuperaBloco(Coordenadas coordenadas, UUID uuid) {
        return Bloco.<Bloco>find("coordenadas.x = ?1 and coordenadas.y = ?2 and coordenadas.z = ?3 and mundo.player.uuid = ?4",
                        coordenadas.x,
                        coordenadas.y,
                        coordenadas.z,
                        uuid)
                .singleResultOptional()
                .orElseGet(() -> novoBloco(coordenadas, uuid));
    }

    private Bloco novoBloco(Coordenadas coordenadas, UUID uuid) {
        Bloco bloco = new Bloco();
        bloco.coordenadas = coordenadas;
        bloco.mundo = recuperaMundo(uuid);
        Bloco.persist(bloco);
        if (bloco.mundo.blocos == null) {
            bloco.mundo.blocos = new ArrayList<>();
        }
        bloco.mundo.blocos.add(bloco);
        Mundo.persist(bloco.mundo);
        return bloco;
    }

    @Transactional
    public Item quebrarBloco(Coordenadas coordenadas, UUID uuid) throws ItemNaoEncontradoException {
        Bloco bloco = recuperaBloco(coordenadas, uuid);
        if (bloco.item == null) {
            throw new ItemNaoEncontradoException("Bloco vazio não pode ser quebrado");
        }
        Item item = bloco.item;
        bloco.item = null;
        Bloco.persist(bloco);
        return item;
    }
}
