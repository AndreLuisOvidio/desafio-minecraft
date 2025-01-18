package dev.ovidio.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Coordenadas {
    public int x = 0;
    public int y = 0;
    public int z = 0;
}
