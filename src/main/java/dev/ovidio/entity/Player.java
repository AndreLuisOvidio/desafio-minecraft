package dev.ovidio.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

import java.util.UUID;

@Entity
public class Player extends PanacheEntity {
    public UUID uuid;

    @OneToOne
    public Inventario inventario;

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", inventario=" + inventario +
                ", uuid=" + uuid +
                '}';
    }
}
