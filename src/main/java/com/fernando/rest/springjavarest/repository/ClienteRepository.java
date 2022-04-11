package com.fernando.rest.springjavarest.repository;

import com.fernando.rest.springjavarest.model.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {


}
