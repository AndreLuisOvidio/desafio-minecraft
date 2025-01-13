package dev.ovidio.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Item extends PanacheEntity {
    public String nome;
    public int durabilidade;

    public Item() {
        this.durabilidade = 1;
    }

    public Item(String nome) {
        this();
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Item{" +
                "nome='" + nome + '\'' +
                ", durabilidade=" + durabilidade +
                ", id=" + id +
                '}';
    }
}
