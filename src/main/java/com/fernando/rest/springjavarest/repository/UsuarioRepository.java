package com.fernando.rest.springjavarest.repository;

import com.fernando.rest.springjavarest.model.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Usuario findByUsername(String username);
}
