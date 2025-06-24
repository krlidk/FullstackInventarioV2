package com.ecomarket.inventario.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecomarket.inventario.ProductoDTO.ProductoDTO;
import com.ecomarket.inventario.Service.AlmacenService;

import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("api/v1/almacen")
public class AlmacenController {

    @Autowired
    AlmacenService almacenService;

    @GetMapping("/buscarAlmacenId/{id}")
    public ResponseEntity<?> buscarAlmacenPorId(@PathVariable int id){
        ResponseEntity<?> response = almacenService.buscarAlmacenPorId(id);

        return response;
    }
    @PostMapping("agregarProducto/{idProducto}")
    public ResponseEntity<ProductoDTO[]> agregarProducto(@RequestBody Map<String, Object> datos, @PathVariable int idProducto){
       ResponseEntity<ProductoDTO[]> producto =  almacenService.agregarProducto(datos, idProducto);
       return producto;
    }

    @PutMapping("actualizarAlmacen")
    public ResponseEntity<?> actualizarAlmacen(@RequestBody Map<String,Object> datos){
        ResponseEntity<?> response = almacenService.actualizarAlmacen(datos);
        return response;
    }

    @GetMapping("mostrarProductosPorAlmacen/{id}")
    public ResponseEntity<?> buscarProductosAlmacen(@PathVariable int id){
        ResponseEntity<?> productos = almacenService.buscarProductoPorAlmacen(id);
        return productos;
    }

    @GetMapping("/mostrarAlmacenes")
    public ResponseEntity<?> mostrarAlmacenes(){
        ResponseEntity<?> almacenes = almacenService.obtenerAlmacenes();

        return almacenes;
    }

    @GetMapping("almacenPorNombre")
    public ResponseEntity<?> buscarAlmacenPorNombre(@RequestParam String nombre){
        return almacenService.buscarPorNombre(nombre);
    }

    @GetMapping("almacenPorDireccion")
    public ResponseEntity<?> buscarAlmacenPorDireccion(@RequestParam String direccion){
        return almacenService.buscarPorDireccion(direccion);
    }
}
