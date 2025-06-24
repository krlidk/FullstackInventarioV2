package com.ecomarket.inventario.Repository;

import java.util.List;
import com.ecomarket.inventario.Model.Almacen;
import com.ecomarket.inventario.Model.AlmacenId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlmacenRepository extends JpaRepository<Almacen, AlmacenId> {
    
    // Encuentra un almacen por su ID
    @Query("SELECT a FROM Almacen a WHERE a.almacenId.almacenId = :id")
    List<Almacen> findOnebyId(@Param("id") int id);

    //Encuentra un almacen y producto por sus ids
    @Query("SELECT a FROM Almacen a WHERE a.almacenId = :id")
    Almacen findbyalmacenId(@Param("id") AlmacenId id);
    
    // Método para encontrar almacenes por nombre
    @Query("SELECT a FROM Almacen a WHERE LOWER(a.almacenNombre) LIKE LOWER(CONCAT('%',:almacenNombre,'%'))")
    List<Almacen> findByAlmacenNombre(String almacenNombre); 

    // Encuentra almacenes por dirección
    @Query("SELECT a FROM Almacen a WHERE LOWER(a.direccion) LIKE LOWER(CONCAT('%',:direccion,'%'))") 
    List<Almacen> findByDireccion(String direccion); 

    //Querys mas modificar un almacen
    @Modifying
    @Query("UPDATE Almacen a SET a.almacenNombre = :nombre WHERE a.almacenId.almacenId = :id")
    void actualizarAlmacenNombre(@Param("nombre") String nombre, @Param("id") int id);

    @Modifying
    @Query("UPDATE Almacen a SET a.direccion = :direccion WHERE a.almacenId.almacenId = :id")
    void actualizarAlmacenDireccion(@Param("direccion") String direccion, @Param("id") int id);

    @Modifying
    @Query("UPDATE Almacen a SET a.stock = :stock WHERE a.almacenId.almacenId = :id")
    void actualizarAlmacenStock(@Param("stock") int stock, @Param("id") int id);

    @Query("UPDATE Almacen a SET a.stock = :precio WHERE a.almacenId.almacenId = :id")
    void actualizarAlmacenPrecio(@Param("precio") double precio, @Param("id") int id);

    //Query para comprobar si un almacen existe
    @Query("SELECT COUNT(a) > 0 FROM Almacen a WHERE a.almacenId.almacenId = :id")
    boolean encontraAlmacen(@Param("id") int id); 

    
}