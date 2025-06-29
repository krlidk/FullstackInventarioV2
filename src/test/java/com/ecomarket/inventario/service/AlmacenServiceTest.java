package com.ecomarket.inventario.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import com.ecomarket.inventario.Model.Almacen;
import com.ecomarket.inventario.Model.AlmacenId;
import com.ecomarket.inventario.ProductoDTO.ProductoDTO;
import com.ecomarket.inventario.ProductoDTO.AlmacenDTO;
import com.ecomarket.inventario.Service.AlmacenService;
import com.ecomarket.inventario.Repository.AlmacenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest
public class AlmacenServiceTest {

    @InjectMocks
    private AlmacenService almacenService;

    @Mock
    private AlmacenRepository almacenRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void agregarProducto() {
        // Arrange
        Map<String, Object> datos = new HashMap<>();
        datos.put("id", 1);
        datos.put("precio", 100.0);
        datos.put("stock", 50);
        datos.put("nombre", "Almacén Central");
        datos.put("direccion", "Calle Principal 123");
        int productoId = 1;

        ProductoDTO mockResponse = new ProductoDTO();
        when(restTemplate.getForEntity(
            "http://localhost:8080/api/v1/producto/buscarProductoId/{id}",
            ProductoDTO.class,
            productoId
        )).thenReturn(ResponseEntity.ok(mockResponse));

        // Act
        ResponseEntity<ProductoDTO> response = almacenService.agregarProducto(datos, productoId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void actualizarAlmacen() {
        // Arrange
        Almacen almacen = new Almacen();
        almacen.setAlmacenId(new AlmacenId(1, 1));
        almacen.setPrecio(150.0);
        almacen.setStock(75);

        when(almacenRepository.findOnebyId(1)).thenReturn(List.of(almacen));
        when(almacenRepository.save(any(Almacen.class))).thenReturn(almacen);

        Map<String, Object> datos = new HashMap<>();
        datos.put("id", 1);
        datos.put("precio", 150.0);
        datos.put("stock", 75);

        // Act
        ResponseEntity<?> response = almacenService.actualizarAlmacen(datos);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void buscarAlmacenPorId() {
        // Arrange
        Almacen almacen = new Almacen();
        almacen.setAlmacenId(new AlmacenId(1, 1));
        almacen.setAlmacenNombre("Almacén Central");
        almacen.setDireccion("Calle Principal 123");

        when(almacenRepository.findOnebyId(1)).thenReturn(List.of(almacen));

        // Act
        ResponseEntity<AlmacenDTO> response = almacenService.buscarAlmacenPorId(1);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void buscarProductoPorAlmacen() {
        // Arrange
        Almacen almacen = new Almacen();
        almacen.setAlmacenId(new AlmacenId(1, 1));
        almacen.setAlmacenNombre("Almacén Central");
        almacen.setDireccion("Calle Principal 123");

        when(almacenRepository.findOnebyId(1)).thenReturn(List.of(almacen));

        ProductoDTO[] mockResponse = new ProductoDTO[]{new ProductoDTO()};
        when(restTemplate.postForEntity(
            eq("http://localhost:8080/api/v1/producto/buscarProductos"),
            any(),
            eq(ProductoDTO[].class)
        )).thenReturn(ResponseEntity.ok(mockResponse));

        // Act
        ResponseEntity<?> response = almacenService.buscarProductoPorAlmacen(1);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void obtenerAlmacenes() {
        // Arrange
        Almacen almacen1 = new Almacen();
        almacen1.setAlmacenId(new AlmacenId(1, 1));
        almacen1.setAlmacenNombre("Almacén Central");
        almacen1.setDireccion("Calle Principal 123");

        Almacen almacen2 = new Almacen();
        almacen2.setAlmacenId(new AlmacenId(2, 2));
        almacen2.setAlmacenNombre("Almacén Secundario");
        almacen2.setDireccion("Avenida Secundaria 456");

        when(almacenRepository.findAll()).thenReturn(List.of(almacen1, almacen2));

        // Act
        ResponseEntity<List<AlmacenDTO>> response = almacenService.obtenerAlmacenes();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void buscarPorNombre() {
        // Arrange
        String nombre = "Almacén Central";
        Almacen almacen = new Almacen();
        almacen.setAlmacenId(new AlmacenId(1, 1));
        almacen.setAlmacenNombre(nombre);
        almacen.setDireccion("Calle Principal 123");

        when(almacenRepository.findByAlmacenNombre(nombre)).thenReturn(List.of(almacen));

        // Act
        ResponseEntity<List<AlmacenDTO>> response = almacenService.buscarPorNombre(nombre);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void buscarPorDireccion() {
        // Arrange
        String direccion = "Calle Principal 123";
        Almacen almacen = new Almacen();
        almacen.setAlmacenId(new AlmacenId(1, 1));
        almacen.setAlmacenNombre("Almacén Central");
        almacen.setDireccion(direccion);

        when(almacenRepository.findByDireccion(direccion)).thenReturn(List.of(almacen));

        // Act
        ResponseEntity<?> response = almacenService.buscarPorDireccion(direccion);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void eliminarAlmacen() {
        // Arrange
        int id = 1;
        when(almacenRepository.encontraAlmacen(id)).thenReturn(true);

        // Act
        ResponseEntity<?> response = almacenService.eliminarAlmacen(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(almacenRepository, times(1)).eliminarAlmacen(id);
    }

}
