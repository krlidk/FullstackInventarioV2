package com.ecomarket.inventario.ProductoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AlmacenDTO {
    private int almacenId;
    private String nombre;
    private String direccion;
}
