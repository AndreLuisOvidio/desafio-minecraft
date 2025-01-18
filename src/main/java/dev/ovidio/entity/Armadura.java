package dev.ovidio.entity;

import jakarta.persistence.*;

@Embeddable
public class Armadura {

    @Embedded
    @AttributeOverride(name = "nome", column = @Column(name = "nome_capacete"))
    @AttributeOverride(name = "durabilidade", column = @Column(name = "durabilidade_capacete"))
    public Item capacete;
    @Embedded
    @AttributeOverride(name = "nome", column = @Column(name = "nome_peitoral"))
    @AttributeOverride(name = "durabilidade", column = @Column(name = "durabilidade_peitoral"))
    public Item peitoral;
    @Embedded
    @AttributeOverride(name = "nome", column = @Column(name = "nome_bota"))
    @AttributeOverride(name = "durabilidade", column = @Column(name = "durabilidade_bota"))
    public Item bota;
    @Embedded
    @AttributeOverride(name = "nome", column = @Column(name = "nome_calca"))
    @AttributeOverride(name = "durabilidade", column = @Column(name = "durabilidade_calca"))
    public Item calca;

}
