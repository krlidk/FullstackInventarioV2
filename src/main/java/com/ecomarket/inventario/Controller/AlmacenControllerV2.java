package com.ecomarket.inventario.Controller;

import com.ecomarket.inventario.Model.Almacen;
import com.ecomarket.inventario.Model.AlmacenId;
import com.ecomarket.inventario.ProductoDTO.AlmacenDTO;
import com.ecomarket.inventario.ProductoDTO.ProductoDTO;
import com.ecomarket.inventario.Assemblers.AlmacenModelAssembler;
import com.ecomarket.inventario.Service.AlmacenService;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/v2/almacen")
public class AlmacenControllerV2 {

    @Autowired
    private AlmacenService almacenService;

    @Autowired
    private AlmacenModelAssembler almacenModelAssembler;

    @GetMapping(value = "/buscarAlmacenId/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AlmacenDTO>> buscarAlmacenPorId(@PathVariable int id) {
        AlmacenDTO almacenDTO = almacenService.buscarAlmacenPorId(id).getBody();
        if (almacenDTO == null) {
            return ResponseEntity.notFound().build();
        }
        EntityModel<AlmacenDTO> entityModel = almacenModelAssembler.toModel(almacenDTO);
        return ResponseEntity.ok(entityModel);
    }


    @PostMapping(value = "/agregarProducto/{idProducto}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AlmacenDTO>> agregarProducto(@RequestBody Map<String, Object> datos, @PathVariable int idProducto) {
        ResponseEntity<ProductoDTO> response = almacenService.agregarProducto(datos, idProducto);
        if (response.getStatusCode().is2xxSuccessful()) {
            ProductoDTO productoDTO = response.getBody();
            if (productoDTO == null) {
                return ResponseEntity.badRequest().build();
            }
            AlmacenDTO almacenDTO = new AlmacenDTO(productoDTO.getProductoId(), productoDTO.getNombre(), ""); // Ajustar según sea necesario
            EntityModel<AlmacenDTO> entityModel = almacenModelAssembler.toModel(almacenDTO);
            return ResponseEntity.ok(entityModel);
        }
        return ResponseEntity.status(response.getStatusCode()).body(null);
    }

    @PutMapping(value = "/actualizarAlmacen", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AlmacenDTO>> actualizarAlmacen(@RequestBody Map<String, Object> datos) {
        // Validar campos requeridos
        if (!datos.containsKey("id") || !datos.containsKey("nombre") || !datos.containsKey("direccion")) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            int almacenId = Integer.parseInt(datos.get("id").toString());
            String nombre = datos.get("nombre").toString();
            String direccion = datos.get("direccion").toString();

            ResponseEntity<String> response = almacenService.actualizarAlmacen(datos);
            if (response.getStatusCode().is2xxSuccessful()) {
                AlmacenDTO almacenDTO = new AlmacenDTO(almacenId, nombre, direccion);
                EntityModel<AlmacenDTO> entityModel = almacenModelAssembler.toModel(almacenDTO);
                return ResponseEntity.ok(entityModel);
            }
            return ResponseEntity.status(response.getStatusCode()).body(null);
        } catch (NumberFormatException | NullPointerException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping(value = "/eliminarAlmacen/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> eliminarAlmacen(@PathVariable int id) {
        ResponseEntity<?> response = almacenService.eliminarAlmacen(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            Object responseBody = response.getBody();
            if (responseBody != null) {
                return ResponseEntity.ok(responseBody.toString());
            }
            return ResponseEntity.ok("Eliminación exitosa, pero sin cuerpo de respuesta.");
        }
        return ResponseEntity.status(response.getStatusCode()).body("Almacen no encontrado");
    }


}
