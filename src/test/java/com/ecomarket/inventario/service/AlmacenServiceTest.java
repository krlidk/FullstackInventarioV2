package com.ecomarket.inventario.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ecomarket.inventario.ProductoDTO.AlmacenDTO;
import com.ecomarket.inventario.ProductoDTO.CategoriaDTO;
import com.ecomarket.inventario.ProductoDTO.ProductoDTO;
import com.ecomarket.inventario.Repository.AlmacenRepository;
import com.ecomarket.inventario.Service.AlmacenService;

@SpringBootTest
@AutoConfigureMockMvc
public class AlmacenServiceTest {

    @MockitoBean
    AlmacenRepository almacenRepository;

    @MockitoBean
    AlmacenService almacenService;


    @Test
    void agregarProducto(){
        Map<String, Object> datos = new HashMap<>();

        int productoId = 1;
        
        datos.put("id", 1);
        datos.put("nombre", "Ecomarket Calera");
        datos.put("direccion", "Calera");
        datos.put("precio", 100.0);
        datos.put("stock", 100);
        List<ProductoDTO> productos = new ArrayList<>();
        CategoriaDTO categoriaDTO = new CategoriaDTO(1, "categoria", productos);
        ProductoDTO productoDTO = new ProductoDTO(productoId, "producto", categoriaDTO);

        ResponseEntity<ProductoDTO> producto = ResponseEntity.ok(productoDTO);
        when(almacenService.agregarProducto(datos, productoId)).thenReturn(producto);

        assertTrue(producto.getStatusCode().value() == 200);

    }

    @Test
    void actualizarAlmacen() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("id", 1);
        datos.put("nombre", "ecomarket Cabildo");
        datos.put("direccion", "Cabildo");
        datos.put("precio", 200.0);
        datos.put("stock", 200);

        when(almacenService.actualizarAlmacen(datos)).thenReturn(ResponseEntity.ok("Almacen Actualizado"));
        ResponseEntity<String> resultado = almacenService.actualizarAlmacen(datos);

        assertTrue(resultado.getStatusCode().value() == 200);

    }

    @Test
    void buscarAlmacenPorId() {
        int almacenId = 1;
        String nombre = "almacen";
        String direccion = "direccion";
        AlmacenDTO almacenDTO = new AlmacenDTO(almacenId,nombre,direccion);

        when(almacenService.buscarAlmacenPorId(almacenId)).thenReturn(ResponseEntity.ok(almacenDTO));
        ResponseEntity<AlmacenDTO> almacen = almacenService.buscarAlmacenPorId(almacenId);
        assertTrue(almacen.getStatusCode().value() == 200);
    }

    @Test
    void buscarProductoPorAlmacen(){
        int id = 1;

        List<ProductoDTO> productos = new ArrayList<>();
        CategoriaDTO categoria = new CategoriaDTO(1, "aseo", productos);
        ProductoDTO producto1 = new ProductoDTO(1, "producto1", categoria);
        ProductoDTO producto2 = new ProductoDTO(2, "producto2", categoria);
        ProductoDTO producto3 = new ProductoDTO(3, "producto3", categoria);
        ProductoDTO productosDTO[] = { producto1, producto2, producto3 };
        productos.add(producto1);
        productos.add(producto2);
        productos.add(producto3);

        ResponseEntity<ProductoDTO[]> response = ResponseEntity.ok(productosDTO);
        when(almacenService.buscarProductoPorAlmacen(id)).thenReturn(response);
        ResponseEntity<?> resultado = almacenService.buscarProductoPorAlmacen(id);
        assertTrue(resultado.getStatusCode().value() == 200);
    }
}
