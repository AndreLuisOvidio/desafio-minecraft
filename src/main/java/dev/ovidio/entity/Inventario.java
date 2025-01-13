package dev.ovidio.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Inventario extends PanacheEntity {
    @ManyToOne
    public Item capacete;

    @ManyToOne
    public Item peitoral;
    @ManyToOne
    public Item bota;
    @ManyToOne
    public Item calca;
    @OneToMany(mappedBy = "inventario")
    public List<SlotInventario> slots;

    @Override
    public String toString() {
        return "Inventario{" +
                "id=" + id +
                ", slots=" + slots +
                ", bota=" + bota +
                ", calca=" + calca +
                ", peitoral=" + peitoral +
                ", capacete=" + capacete +
                '}';
    }

}
