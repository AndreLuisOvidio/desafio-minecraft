package dev.ovidio.entity;

import dev.ovidio.type.CodigoSlot;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class SlotInventario extends PanacheEntity {

    public int quantidade;

    @ManyToOne
    public Item item;

    @ManyToOne
    public Inventario inventario;

    public CodigoSlot codigo;

    public SlotInventario() {
    }

    public SlotInventario(Item item, CodigoSlot codigo, Inventario inventario) {
        this.item = item;
        this.codigo = codigo;
        this.inventario = inventario;
        this.quantidade = 1;
    }

    @Override
    public String toString() {
        return "SlotInventario{" +
                "quantidade=" + quantidade +
                ", item=" + item +
                ", codigo=" + codigo +
                ", id=" + id +
                '}';
    }
}
