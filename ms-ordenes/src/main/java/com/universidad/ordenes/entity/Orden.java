package com.universidad.ordenes.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Orden extends PanacheEntity {

    public LocalDateTime fecha;

    @ElementCollection(fetch = FetchType.EAGER)
    public List<Long> productoIds = new ArrayList<>();

    public double total;

    public Orden() {
    }

    public Orden(LocalDateTime fecha, List<Long> productoIds, double total) {
        this.fecha = fecha;
        this.productoIds = productoIds;
        this.total = total;
    }
}

