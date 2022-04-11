package com.fernando.rest.springjavarest.controller;
import com.fernando.rest.springjavarest.model.Cliente;
import com.fernando.rest.springjavarest.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    @GetMapping("/clientesJSON")
    public ResponseEntity<List<Cliente>> showClientesJSON(){
        return new ResponseEntity<>(clienteService.findAll(), HttpStatus.OK);
    }

    /*WITH REQUEST PARAM*/
    @GetMapping("/cliente")
    public ResponseEntity<Cliente> clienteJSON(@RequestParam("id") Long id){
        return new ResponseEntity<>(clienteService.findById(id), HttpStatus.OK);
    }

    /*WITH PATH VARIABLE*/
    @GetMapping("/cliente/{id}")
    public ResponseEntity<Cliente> clienteJSON1(@PathVariable("id") Long id){
        return new ResponseEntity<>(clienteService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/cliente")
    public ResponseEntity<Cliente> saveCliente(@RequestBody Cliente cliente){
        return new ResponseEntity<>(clienteService.saveAndReturnCliente(cliente), HttpStatus.OK);
    }

    @PostMapping("/cliente/{id}")
    public ResponseEntity<Cliente> updateCliente(@RequestBody Cliente cliente, @PathVariable long id){
        Cliente clienteUpdate = clienteService.findById(id);
        clienteUpdate.setNombre(cliente.getNombre());
        clienteUpdate.setApellido(cliente.getApellido());
        clienteUpdate.setEmail(cliente.getEmail());
        clienteUpdate.setTelefono(cliente.getTelefono());
        clienteUpdate.setCreatedAt(cliente.getCreatedAt());
        return new ResponseEntity<>(clienteService.saveAndReturnCliente(clienteUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/cliente/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable long id){
        return new ResponseEntity<>(clienteService.deleteById(id), HttpStatus.OK);
    }
}
