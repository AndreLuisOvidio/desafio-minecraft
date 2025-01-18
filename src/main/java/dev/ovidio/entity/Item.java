package dev.ovidio.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Item extends PanacheEntity {
    public String nome;
    public Integer durabilidade;

    public Item() {
    }

    public Item(String nome, Integer durabilidade) {
        this.nome = nome;
        this.durabilidade = durabilidade;
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
