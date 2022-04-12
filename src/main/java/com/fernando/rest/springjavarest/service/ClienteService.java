package com.fernando.rest.springjavarest.service;

import com.fernando.rest.springjavarest.model.Cliente;
import com.fernando.rest.springjavarest.model.Region;
import com.fernando.rest.springjavarest.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    void save(Cliente cliente);
    Cliente saveAndReturnCliente(Cliente cliente);
    List<Cliente> findAll();
    Cliente findById(Long id);
    Cliente deleteById(Long id);
    List<Cliente> findAllByRegion(Region region);
}
