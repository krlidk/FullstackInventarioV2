package com.ecomarket.inventario.ProductoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AlmacenDTO {
    private int almacenId;
    private String nombre;
    private String direccion;
}
