package com.ecomarket.inventario.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.ecomarket.inventario.Controller.AlmacenController;
import com.ecomarket.inventario.Model.Almacen;
import com.ecomarket.inventario.Model.AlmacenId;
import com.ecomarket.inventario.ProductoDTO.CategoriaDTO;
import com.ecomarket.inventario.ProductoDTO.ProductoDTO;
import com.ecomarket.inventario.Repository.AlmacenRepository;
import com.ecomarket.inventario.Service.AlmacenService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AlmacenControllerTest {

    RestTemplate restTemplate;
    @MockitoBean
    AlmacenController almacenController;

    @MockitoBean
    AlmacenRepository almacenRepository;
    @MockitoBean
    AlmacenService almacenService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void buscarAlmacenId() throws Exception {
        int almacenId = 1;
        int productoId = 1;
        AlmacenId id = new AlmacenId(productoId, almacenId);
        String nombre = "almacen";
        String direccion = "direccion";
        int stock = 100;
        double precio = 100.0;
        Almacen almacen = new Almacen(id, nombre, direccion, precio, stock);

        List<Almacen> almacenes = new ArrayList<>();
        almacenes.add(almacen);
        when(almacenRepository.findOnebyId(almacenId)).thenReturn(almacenes);

        mockMvc.perform(get("/api/v1/almacen/buscarAlmacenId/{id}", almacenId))
                .andExpect(status().isOk());
    }

    @Test
    void agregarProducto() throws Exception {
        Integer id = 1;

        Map<String, Object> datos = new HashMap<>();
        datos.put("id", 1);
        datos.put("nombre", "Ecomarket Calera");
        datos.put("direccion", "Calera");
        datos.put("precio", 100.0);
        datos.put("stock", 100);

        List<ProductoDTO> productos = new ArrayList<>();
        CategoriaDTO categoriaDTO = new CategoriaDTO(1, "categoria", productos);
        ProductoDTO productoDTO = new ProductoDTO(id, "producto", categoriaDTO);

        when(almacenService.agregarProducto(datos, id)).thenReturn(ResponseEntity.ok(productoDTO));

        mockMvc
                .perform(post("/api/v1/almacen/agregarProducto/{idProducto}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datos)))
                .andExpect(status().isOk());

    }

    @Test
    void actualizarAlmacen() throws Exception {

        Map<String, Object> datos = new HashMap<>();
        datos.put("id", 1);
        datos.put("nombre", "Ecomarket Calera");
        datos.put("direccion", "Calera");
        datos.put("precio", 100.0);
        datos.put("stock", 100);

        String response = "almacen actualizado";
        when(almacenService.actualizarAlmacen(datos)).thenReturn(ResponseEntity.ok(response));
        mockMvc.perform(
                put("/api/v1/almacen/actualizarAlmacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datos)))
                .andExpect(status().isOk());
    }

}
