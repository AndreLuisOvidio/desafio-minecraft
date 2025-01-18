package dev.ovidio.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.Comparator;
import java.util.List;

@Entity
public class Inventario extends PanacheEntity {

    @Embedded
    public Armadura armadura = new Armadura();

    @OneToMany(mappedBy = "inventario", fetch = FetchType.EAGER)
    @JsonIgnore
    public List<SlotInventario> slots;

    @Override
    public String toString() {
        return "Inventario{" +
                "id=" + id +
                ", slots=" + slots +
                ", bota=" + armadura.bota +
                ", calca=" + armadura.calca +
                ", peitoral=" + armadura.peitoral +
                ", capacete=" + armadura.capacete +
                '}';
    }

    @JsonGetter("slots")
    public List<SlotInventario> getSlotsOrdenados() {
        return slots.stream()
                .sorted(Comparator.comparing(slot -> slot.codigo))
                .toList();
    }

}
