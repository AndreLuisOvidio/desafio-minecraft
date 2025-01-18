package dev.ovidio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.ovidio.type.CodigoSlot;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class SlotInventario extends PanacheEntity {

    public int quantidade;

    @Embedded
    public Item item;

    @ManyToOne
    @JsonIgnore
    public Inventario inventario;

    public CodigoSlot codigo;

    public SlotInventario() {

    }
    public SlotInventario(Item item) {
        this.item = item;
        this.quantidade = 1;
    }

    public SlotInventario(CodigoSlot codigo, Inventario inventario) {
        this.codigo = codigo;
        this.inventario = inventario;
        this.quantidade = 0;
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
