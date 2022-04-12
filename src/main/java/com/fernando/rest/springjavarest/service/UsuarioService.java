package com.fernando.rest.springjavarest.service;

import com.fernando.rest.springjavarest.model.Usuario;



public interface UsuarioService{

    Usuario findByUsername(String username);
}
