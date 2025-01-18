package dev.ovidio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.List;

@Entity
public class Mundo extends PanacheEntity {
    @OneToMany(mappedBy = "mundo", fetch = FetchType.EAGER)
    public List<Bloco> blocos;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    public Player player;
}
