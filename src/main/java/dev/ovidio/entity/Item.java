package dev.ovidio.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Item {
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;
        return Objects.equals(nome, item.nome) && Objects.equals(durabilidade, item.durabilidade);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(nome);
        result = 31 * result + Objects.hashCode(durabilidade);
        return result;
    }
}
