package com.ecomarket.inventario.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.ecomarket.inventario.Model.Almacen;
import com.ecomarket.inventario.Model.AlmacenId;
import com.ecomarket.inventario.ProductoDTO.AlmacenDTO;
import com.ecomarket.inventario.ProductoDTO.ProductoDTO;
import com.ecomarket.inventario.Repository.AlmacenRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AlmacenService {
    @Autowired
    private AlmacenRepository almacenRepository;

    @Autowired
    private RestTemplate restTemplate;

    // metodo guardar producto
    @Transactional
    public ResponseEntity<ProductoDTO> agregarProducto(Map<String, Object> datos, int productoId) {
        try {
            // capturamos los datos del producto agregado
            ResponseEntity<ProductoDTO> producto = restTemplate.getForEntity(
                    "http://catalogo-app:8080/api/v1/producto/buscarProductoId/{id}",
                    ProductoDTO.class,
                    productoId);

            if (!datos.containsKey("id")) {
                System.err.println("No se ha encontrado el parametro id en el json");
                return ResponseEntity.badRequest().body(producto.getBody());
            }

            int almacenId = (int) datos.get("id");

            // caso en donde el almacen ya existe
            if (almacenRepository.encontraAlmacen(almacenId)) {
                AlmacenId id = new AlmacenId(productoId, almacenId);

                Optional<Almacen> almacenOpt = almacenRepository.findById(id);

                // si el producto ya existe
                if (almacenOpt.isPresent()) {
                    Almacen almacen = almacenOpt.get();

                    if (datos.containsKey("precio")) {
                        almacen.setPrecio((double) datos.get("precio"));
                    }

                    if (datos.containsKey("stock")) {
                        almacen.setStock((int) datos.get("stock"));
                    }
                    almacenRepository.save(almacen);

                    return ResponseEntity.ok(producto.getBody());
                }

                // si almacen existe pero no el producto
                if (!datos.containsKey("precio") || !datos.containsKey("stock")) {
                    System.err.println("Faltan parametros obligatorios: stock y precio");
                    return ResponseEntity.badRequest().body(producto.getBody());
                }

                Almacen almacenBase = almacenRepository.findOnebyId(almacenId).stream().findFirst().orElse(null);
                if (almacenBase == null) {
                    System.err.println("No se encontró un almacén con el ID proporcionado");
                    return ResponseEntity.badRequest().body(producto.getBody());
                }

                String nombre = almacenBase.getAlmacenNombre();
                String direccion = almacenBase.getDireccion();
                int stock = (int) datos.get("stock");
                double precio = (double) datos.get("precio");

                Almacen almacenNuevoProducto = new Almacen(id, nombre, direccion, precio, stock);

                almacenRepository.save(almacenNuevoProducto);

                return ResponseEntity.ok(producto.getBody());
            }

            // caso donde el almacen no existe
            String clavesRequeridas[] = { "nombre", "direccion", "precio", "stock" };

            for (String clave : clavesRequeridas) {
                if (!datos.containsKey(clave)) {
                    System.err.println("Falta alguna de las claves necesarias: nombre, direccion, precio, stock");
                    return ResponseEntity.badRequest().body(producto.getBody());
                }
            }

            AlmacenId id = new AlmacenId(productoId, almacenId);
            String nombre = (String) datos.get("nombre");
            String direccion = (String) datos.get("direccion");
            double precio = (double) datos.get("precio");
            int stock = (int) datos.get("stock");

            Almacen nuevoAlmacen = new Almacen(id, nombre, direccion, precio, stock);

            almacenRepository.save(nuevoAlmacen);

            return ResponseEntity.ok(producto.getBody());

        } catch (Exception e) {
            System.out.println("Error al recibir el JSON " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    public ResponseEntity<String> actualizarAlmacen(Map<String, Object> datos) {
        if (!datos.containsKey("id")) {
            return ResponseEntity.notFound().build();
        }

        int almacenId = (int) datos.get("id");

        Optional<Almacen> almacenOpt = almacenRepository.findOnebyId(almacenId).stream().findFirst();
        if (almacenOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Almacen almacen = almacenOpt.get();

        if (datos.containsKey("nombre")) {
            almacen.setAlmacenNombre((String) datos.get("nombre"));
        }
        if (datos.containsKey("direccion")) {
            almacen.setDireccion((String) datos.get("direccion"));
        }
        if (datos.containsKey("precio")) {
            almacen.setPrecio((double) datos.get("precio"));
        }
        if (datos.containsKey("stock")) {
            almacen.setStock((int) datos.get("stock"));
        }

        almacenRepository.save(almacen);

        return ResponseEntity.ok("Almacen actualizado con éxito");
    }

    public ResponseEntity<AlmacenDTO> buscarAlmacenPorId(int id){
        Almacen almacenBase = almacenRepository.findOnebyId(id).stream().findFirst().orElse(null);
        if (almacenBase == null) {
            return ResponseEntity.notFound().build();
        }

        AlmacenDTO almacenVista = new AlmacenDTO(id, almacenBase.getAlmacenNombre(), almacenBase.getDireccion());

        return ResponseEntity.ok().body(almacenVista); 
    }

    public ResponseEntity<ProductoDTO[]>buscarProductoPorAlmacen(int id){
        try {
        
            List<Integer> ids = almacenRepository.findOnebyId(id).stream().map(almacen -> almacen.getAlmacenId().getProductoId()).toList();
            
            if(ids.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            ResponseEntity<ProductoDTO[]> productos = new RestTemplate().postForEntity(
                "http://catalogo-app:8080/api/v1/producto/buscarProductos",
                ids,
                ProductoDTO[].class);
            
            return productos;

        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
        
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<List<AlmacenDTO>> obtenerAlmacenes() {
        List<AlmacenDTO> almacenes = almacenRepository.findAll().stream().map(almacen -> new AlmacenDTO(
            almacen.getAlmacenId().getAlmacenId(),
            almacen.getAlmacenNombre(),
            almacen.getDireccion())).distinct().toList();

        if (almacenes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(almacenes);
    }
    

    public ResponseEntity<List<AlmacenDTO>> buscarPorNombre(@RequestParam String almacenNombre) {
        List<AlmacenDTO> almacenes = almacenRepository.findByAlmacenNombre(almacenNombre).stream().map(almacen -> new AlmacenDTO(
            almacen.getAlmacenId().getAlmacenId(),
            almacen.getAlmacenNombre(),
            almacen.getDireccion())).distinct().toList();

        if (almacenes.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(almacenes);
    }

    public ResponseEntity<?> buscarPorDireccion(@RequestParam String direccion) {
        List<AlmacenDTO> almacenes = almacenRepository.findByDireccion(direccion).stream().map(almacen -> new AlmacenDTO(almacen.getAlmacenId().getAlmacenId(), almacen.getAlmacenNombre(), almacen.getDireccion())).distinct().toList();

        if(almacenes.isEmpty()){
            return ResponseEntity.badRequest().body("No se han encontrado almacenes con esta direccion");
        }
        return ResponseEntity.ok(almacenes);
    }

    //Eliminar un almacen
    @Transactional
    public ResponseEntity<?> eliminarAlmacen(int id) {
        if (!almacenRepository.encontraAlmacen(id)) {
            return ResponseEntity.notFound().build();} 
        else {
            almacenRepository.eliminarAlmacen(id);
            return ResponseEntity.ok("Almacen eliminado con éxito");
        }    
    }

    public List<AlmacenDTO> getAllAlmacenes() {
        List<AlmacenDTO> almacenes = almacenRepository.findAll().stream().map(almacen -> new AlmacenDTO(
            almacen.getAlmacenId().getAlmacenId(),
            almacen.getAlmacenNombre(),
            almacen.getDireccion())).distinct().toList();

        return almacenes;
    }
    
}