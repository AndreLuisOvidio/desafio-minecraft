package dev.ovidio.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.Comparator;
import java.util.List;

@Entity
public class Inventario extends PanacheEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    public Item capacete;

    @ManyToOne(fetch = FetchType.EAGER)
    public Item peitoral;
    @ManyToOne(fetch = FetchType.EAGER)
    public Item bota;
    @ManyToOne(fetch = FetchType.EAGER)
    public Item calca;
    @OneToMany(mappedBy = "inventario", fetch = FetchType.EAGER)
    @JsonIgnore
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

    @JsonGetter("slots")
    public List<SlotInventario> getSlotsOrdenados() {
        return slots.stream()
                .sorted(Comparator.comparing(slot -> slot.codigo))
                .toList();
    }

}
