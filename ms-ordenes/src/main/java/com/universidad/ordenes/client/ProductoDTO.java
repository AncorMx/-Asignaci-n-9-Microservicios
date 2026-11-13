package com.universidad.ordenes.client;

public class ProductoDTO {

    public Long id;
    public String name;
    public double price;

    public ProductoDTO() {
    }

    public ProductoDTO(Long id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

