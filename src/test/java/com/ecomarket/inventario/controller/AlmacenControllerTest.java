package com.ecomarket.inventario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.ecomarket.inventario.Controller.AlmacenController;
import com.ecomarket.inventario.Model.Almacen;
import com.ecomarket.inventario.Model.AlmacenId;
import com.ecomarket.inventario.ProductoDTO.AlmacenDTO;
import com.ecomarket.inventario.ProductoDTO.ProductoDTO;
import com.ecomarket.inventario.Service.AlmacenService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AlmacenController.class)
public class AlmacenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlmacenService almacenService;

    @Autowired
    private ObjectMapper objectMapper;

    private AlmacenDTO almacenDTO;
    private Almacen almacen;

    @BeforeEach
    public void setUp() {
        almacenDTO = new AlmacenDTO(1, "Almacen Central", "Calle Principal 123");
        almacen = new Almacen();
        almacen.setAlmacenId(new AlmacenId(1, 1));
        almacen.setAlmacenNombre("Almacen Central");
        almacen.setDireccion("Calle Principal 123");
    }

    @Test
    public void testAgregarProducto() throws Exception {
        when(almacenService.agregarProducto(any(), anyInt())).thenReturn(ResponseEntity.ok().body(new ProductoDTO(){}));

        mockMvc.perform(post("/api/v1/almacen/agregarProducto/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(almacenDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value(0));
    }

    @Test
    public void testActualizarAlmacen() throws Exception {
        when(almacenService.actualizarAlmacen(anyMap())).thenAnswer(invocation -> {
            return ResponseEntity.ok().body("Almacen actualizado con éxito");
        });

        mockMvc.perform(put("/api/v1/almacen/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(almacenDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Almacen actualizado con éxito"));
    }

    @Test
    public void testBuscarAlmacenPorId() throws Exception {
        when(almacenService.buscarAlmacenPorId(1)).thenReturn(ResponseEntity.ok().body(new AlmacenDTO(1, "Almacen Central", "Calle Principal 123")));

        mockMvc.perform(post("/api/v1/almacen/agregarProducto/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(almacenDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.almacenId").value(1))
        .andExpect(jsonPath("$.almacenNombre").value("Almacen Central"))
        .andExpect(jsonPath("$.direccion").value("Calle Principal 123"));
    }
    
    @Test
    public void testBuscarProductoPorAlmacen() throws Exception {
        when(almacenService.buscarProductoPorAlmacen(1)).thenAnswer(invocation -> {
            return ResponseEntity.ok().body(new ProductoDTO[]{});
        });

        mockMvc.perform(get("/api/v1/almacen/buscarProductos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testObtenerAlmacenes() throws Exception {
        when(almacenService.obtenerAlmacenes()).thenReturn(ResponseEntity.ok().body(List.of(almacenDTO)));

        mockMvc.perform(get("/api/v1/almacen/mostrarAlmacenes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].almacenId").value(1))
                .andExpect(jsonPath("$[0].almacenNombre").value("Almacen Central"))
                .andExpect(jsonPath("$[0].direccion").value("Calle Principal 123"));
    }

    @Test
    public void testBuscarPorNombre() throws Exception {
        when(almacenService.buscarPorNombre("Almacen Central")).thenReturn(ResponseEntity.ok().body(List.of(almacenDTO)));

        mockMvc.perform(get("/api/v1/almacen/buscarPorNombre")
                .param("almacenNombre", "Almacen Central")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].almacenId").value(1))
                .andExpect(jsonPath("$[0].almacenNombre").value("Almacen Central"))
                .andExpect(jsonPath("$[0].direccion").value("Calle Principal 123"));
    }

    @Test
    public void testBuscarPorDireccion() throws Exception {
        when(almacenService.buscarPorDireccion("Calle Principal 123")).thenAnswer(invocation -> {
            return ResponseEntity.ok().body(List.of(new AlmacenDTO(1, "Almacen Central", "Calle Principal 123")));
        });

        mockMvc.perform(get("/api/v1/almacen/almacenPorDireccion")
                .param("direccion", "Calle Principal 123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].almacenId").value(1))
                .andExpect(jsonPath("$[0].almacenNombre").value("Almacen Central"))
                .andExpect(jsonPath("$[0].direccion").value("Calle Principal 123"));
    }

    @Test
    public void testEliminarAlmacen() throws Exception {
        when(almacenService.eliminarAlmacen(1)).thenAnswer(invocation -> {
            return ResponseEntity.ok().body("Almacen eliminado con éxito");
        });

        mockMvc.perform(delete("/api/v1/almacen/eliminar/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Almacen eliminado con éxito"));
    }
}
