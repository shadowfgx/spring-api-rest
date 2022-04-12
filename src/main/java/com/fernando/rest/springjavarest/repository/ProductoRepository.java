package com.fernando.rest.springjavarest.repository;

import com.fernando.rest.springjavarest.model.Producto;
import org.springframework.data.repository.CrudRepository;

public interface ProductoRepository extends CrudRepository<Producto, Long> {

}
