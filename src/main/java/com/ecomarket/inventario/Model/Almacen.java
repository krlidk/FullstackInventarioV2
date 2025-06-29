package com.ecomarket.inventario.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "almacen")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Almacén de productos")

public class Almacen {

    @EmbeddedId
    @Column(name = "id_almacen")
    private AlmacenId almacenId;

    @Column (nullable = false,name = "nombre_almacen")
    private String almacenNombre;

    @Column (nullable = false)
    private String direccion;

    @Column (nullable = true)
    private double precio;

    @Column (nullable = true)
    private int stock;




}