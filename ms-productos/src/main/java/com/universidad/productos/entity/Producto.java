package com.universidad.productos.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Producto extends PanacheEntity {

    public String name;
    public double price;

    public Producto() {
    }

    public Producto(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

