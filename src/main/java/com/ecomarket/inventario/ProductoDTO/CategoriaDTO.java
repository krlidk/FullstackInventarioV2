package com.ecomarket.inventario.ProductoDTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO {
    private int categoriaId;
    private String descripcion;
    private List<ProductoDTO> productos;
}
