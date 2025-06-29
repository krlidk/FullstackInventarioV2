package com.ecomarket.inventario.repository;

import com.ecomarket.inventario.Model.Almacen;
import com.ecomarket.inventario.Repository.AlmacenRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AlmacenRepositoryTest {

    @Autowired
    private AlmacenRepository almacenRepository;

    @Test
    @Sql({"classpath:insert-almacen.sql"}) // Archivo SQL para insertar datos de prueba
    public void testFindOneById() {
        int testId = 1; // ID de prueba

        List<Almacen> result = almacenRepository.findOnebyId(testId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(testId, result.get(0).getAlmacenId().getAlmacenId());
    }
}
