package com.fernando.rest.springjavarest.service;

import com.fernando.rest.springjavarest.model.Producto;

import java.util.List;

public interface ProductoService {

    void save(Producto producto);
    Producto saveAndReturnProducto(Producto producto);
    List<Producto> findAll();
    Producto findById(Long id);
    void delete(Long id);
}
