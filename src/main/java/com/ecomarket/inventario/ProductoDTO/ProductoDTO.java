package com.ecomarket.inventario.ProductoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductoDTO {

    private int productoId;
    private String nombre;
    private CategoriaDTO categoria;

}
