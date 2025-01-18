package dev.ovidio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Bloco extends PanacheEntity {

    @ManyToOne
    @JsonIgnore
    public Mundo mundo;

    @Embedded
    public Coordenadas coordenadas;

    @Embedded
    public Item item;

}
