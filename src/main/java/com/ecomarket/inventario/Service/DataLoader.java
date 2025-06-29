package com.ecomarket.inventario.Service;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import com.ecomarket.inventario.Model.*;
import com.ecomarket.inventario.Repository.AlmacenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import jakarta.transaction.Transactional;
import net.datafaker.Faker;

@Profile("test")
@Component
public class DataLoader implements CommandLineRunner{

    @Autowired
    AlmacenService almacenService; 

    @Autowired
    AlmacenRepository almacenRepository;

    @Override
    @Transactional
    public void run (String ... args) throws Exception{
        Faker faker = new Faker();
        Random random = new Random();
        
        try {
            //recibiendo lo productos disponibles del otro microservicio
            ResponseEntity<List<Integer>> ids = new RestTemplate().exchange(
            "http://localhost:8080/api/v1/producto/productoDisponible" ,
                HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Integer>>() {} 
            );

                List<Integer> productoIds = ids.getBody(); 

                if(productoIds == null || productoIds.isEmpty()){
                    System.out.println("No se pudo obtener lista de ids");
                    return ;
                }
                
                //guardamos los almacenes en memoria
                List<Almacen> almacenes = new ArrayList<>();

                for(int i = 0; i < 50; i ++){
                    int almacenId = i;

                    String nombre = faker.commerce().vendor();
                    String direccion = faker.address().city();

                    for(int j = 0; j < 2; j ++ ){
                        int productoId = productoIds.get(random.nextInt(productoIds.size()));

                        double precio = Math.round((random.nextDouble() * (15000 - 1000) + 1000) * 10) / 10.0;
                        int stock = random.nextInt(100);

                        AlmacenId id = new AlmacenId(productoId ,almacenId);

                        Almacen almacenTest = new Almacen(id,nombre,direccion,precio,stock);

                        almacenes.add(almacenTest);

                    }
                    almacenRepository.saveAll(almacenes);
               }

                almacenRepository.saveAll(almacenes);

        } catch (Exception e) {
            System.out.println("error al recibir la lista de id "+ e.getMessage());
        }

    } 

}
