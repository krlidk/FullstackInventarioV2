package com.ecomarket.inventario.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecomarket.inventario.ProductoDTO.ProductoDTO;
import com.ecomarket.inventario.Service.AlmacenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("api/v1/almacen")
@Tag(name = "Almacen", description = "Controlador para gestionar almacenes y productos asociados")
public class AlmacenController {

    @Autowired
    AlmacenService almacenService;

    @GetMapping("/buscarAlmacenId/{id}")
    @Operation(summary = "Buscar Almacen por ID", description = "Obtiene un almacen por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Almacén encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Almacén no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> buscarAlmacenPorId(@PathVariable int id){
        ResponseEntity<?> response = almacenService.buscarAlmacenPorId(id);

        return response;
    }

    @PostMapping("agregarProducto/{idProducto}")
    @Operation(summary = "Agregar Producto al Almacen", description = "Agrega un producto a un almacen específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta (faltan datos o datos inválidos)"),
        @ApiResponse(responseCode = "404", description = "Almacén o producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ProductoDTO[]> agregarProducto(@RequestBody Map<String, Object> datos, @PathVariable int idProducto){
       ResponseEntity<ProductoDTO[]> producto =  almacenService.agregarProducto(datos, idProducto);
       return producto;
    }

    @PutMapping("actualizarAlmacen")
    @Operation(summary = "Actualizar Almacen", description = "Actualiza la información de un almacen")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Almacén actualizado con éxito"),
        @ApiResponse(responseCode = "204", description = "No se proporcionaron datos para actualizar"),
        @ApiResponse(responseCode = "404", description = "Almacén no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> actualizarAlmacen(@RequestBody Map<String,Object> datos){
        ResponseEntity<?> response = almacenService.actualizarAlmacen(datos);
        return response;
    }

    @GetMapping("mostrarProductosPorAlmacen/{id}")
    @Operation(summary = "Buscar Productos por Almacen", description = "Obtiene los productos asociados a un almacen específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos encontrados exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta (ID inválido o sin productos)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> buscarProductosAlmacen(@PathVariable int id){
        ResponseEntity<?> productos = almacenService.buscarProductoPorAlmacen(id);
        return productos;
    }

    @GetMapping("/mostrarAlmacenes")
    @Operation(summary = "Mostrar Almacenes", description = "Obtiene una lista de todos los almacenes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Almacenes encontrados exitosamente"),
        @ApiResponse(responseCode = "204", description = "No se encontraron almacenes"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> mostrarAlmacenes(){
        ResponseEntity<?> almacenes = almacenService.obtenerAlmacenes();

        return almacenes;
    }

    @GetMapping("almacenPorNombre")
    @Operation(summary = "Buscar Almacen por Nombre", description = "Obtiene un almacen por su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Almacén encontrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta (nombre inválido o no encontrado)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> buscarAlmacenPorNombre(@RequestParam String nombre){
        return almacenService.buscarPorNombre(nombre);
    }

    @GetMapping("almacenPorDireccion")
    @Operation(summary = "Buscar Almacen por Dirección", description = "Obtiene un almacen por su dirección")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Almacén encontrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta (dirección inválida o no encontrada)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> buscarAlmacenPorDireccion(@RequestParam String direccion){
        return almacenService.buscarPorDireccion(direccion);
    }
}