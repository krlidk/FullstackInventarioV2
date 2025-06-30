package com.ecomarket.inventario.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import com.ecomarket.inventario.Controller.AlmacenControllerV2;
import com.ecomarket.inventario.ProductoDTO.AlmacenDTO;

@Component
public class AlmacenModelAssembler implements RepresentationModelAssembler<AlmacenDTO, EntityModel<AlmacenDTO>> {

    @Override
    @NonNull
    public EntityModel<AlmacenDTO> toModel(@NonNull AlmacenDTO almacenDTO) {
        return EntityModel.of(almacenDTO,
                linkTo(methodOn(AlmacenControllerV2.class).buscarAlmacenPorId(almacenDTO.getAlmacenId())).withSelfRel(),
                linkTo(methodOn(AlmacenControllerV2.class).agregarProducto(null, almacenDTO.getAlmacenId())).withRel("agregarProducto"),
                linkTo(methodOn(AlmacenControllerV2.class).actualizarAlmacen(null)).withRel("actualizarAlmacen")
                );
    }

}
