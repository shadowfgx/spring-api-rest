package com.fernando.rest.springjavarest.repository;

import com.fernando.rest.springjavarest.model.Cliente;
import com.fernando.rest.springjavarest.model.Region;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {

    List<Cliente> findAllByRegion(Region region);

}
