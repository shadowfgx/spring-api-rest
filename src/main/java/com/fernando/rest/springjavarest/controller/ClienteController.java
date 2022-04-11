package com.fernando.rest.springjavarest.controller;
import com.fernando.rest.springjavarest.model.Cliente;
import com.fernando.rest.springjavarest.service.ClienteService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    /*WITH HANDLE ERRORS*/
    @GetMapping("/cliente/{id}")
    public ResponseEntity<?> findCliente(@PathVariable Long id){

        Cliente cliente = null;
        Map<String, Object> response = new HashMap<>();

        try{
            cliente = clienteService.findById(id);

        }catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar consulta a base de datos");
            response.put("error", Objects.requireNonNull(e.getMessage()).concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(cliente == null){
            response.put("mensaje", "El cliente con ID: "+ id+" no existe");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    /*WITHOUT HANDLE ERRORS*/
    /*
    @GetMapping("/cliente/{id}")
    public ResponseEntity<Cliente> clienteJSON1(@PathVariable("id") Long id){
        return new ResponseEntity<>(clienteService.findById(id), HttpStatus.OK);
    }*/

    /*WITHOUT HANDLE ERRORS*/
    /*
    @PostMapping("/cliente")
    public ResponseEntity<Cliente> saveCliente(@RequestBody Cliente cliente){
        return new ResponseEntity<>(clienteService.saveAndReturnCliente(cliente), HttpStatus.OK);
    }*/

    @PostMapping("/cliente")
    public ResponseEntity<?> saveClienteWithHandlerErrors(@RequestBody Cliente cliente){

        Cliente newCliente = null;
        Map<String, Object> response = new HashMap<>();

        try {
            newCliente = clienteService.saveAndReturnCliente(cliente);
        }catch (DataAccessException e) {
            response.put("mensaje", "Error al insertar base de datos");
            response.put("error", Objects.requireNonNull(e.getMessage()).concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente se ha creado exitosamente");
        response.put("cliente", newCliente);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /*
    @PutMapping("/cliente/{id}")
    public ResponseEntity<Cliente> updateCliente(@RequestBody Cliente cliente, @PathVariable long id){
        Cliente clienteUpdate = clienteService.findById(id);
        clienteUpdate.setNombre(cliente.getNombre());
        clienteUpdate.setApellido(cliente.getApellido());
        clienteUpdate.setEmail(cliente.getEmail());
        clienteUpdate.setTelefono(cliente.getTelefono());
        clienteUpdate.setCreatedAt(cliente.getCreatedAt());
        return new ResponseEntity<>(clienteService.saveAndReturnCliente(clienteUpdate), HttpStatus.OK);
    }*/

    @PutMapping("/cliente/{id}")
    public ResponseEntity<?> updateClienteWithHandlerErrors(@RequestBody Cliente cliente, @PathVariable long id){
        Cliente clienteUpdate = null;

        clienteUpdate = clienteService.findById(id);

        Map<String, Object> response = new HashMap<>();

        if(clienteUpdate == null){
            response.put("mensaje", "Error: no se puedo editar, el cliente con ID: "+id+" no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try{

            clienteUpdate.setNombre(cliente.getNombre());
            clienteUpdate.setApellido(cliente.getApellido());
            clienteUpdate.setEmail(cliente.getEmail());
            clienteUpdate.setTelefono(cliente.getTelefono());
            clienteUpdate.setCreatedAt(cliente.getCreatedAt());
            clienteService.saveAndReturnCliente(clienteUpdate);

        }catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", Objects.requireNonNull(e.getMessage()).concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente ha sido actualizado con exito");
        response.put("cliente", clienteUpdate);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    /*
    @DeleteMapping("/cliente/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable long id){
        return new ResponseEntity<>(clienteService.deleteById(id), HttpStatus.OK);
    }*/

    @DeleteMapping("/cliente/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        Cliente cliente = null;

        cliente = clienteService.findById(id);

        if(cliente == null){
            response.put("mensaje", "El usario con id: "+id+" no existe en la base de datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try{
            clienteService.deleteById(id);
        }catch (DataAccessException e) {
            response.put("mensaje", "El cliente con id: "+id+" no pudo ser eliminado");
            response.put("error", Objects.requireNonNull(e.getMessage()).concat(": ").concat(e.getMostSpecificCause().getMessage()));
        }

        response.put("mensaje", "El cliente con id: "+id+" se elimino correctamente");
        response.put("cliente", cliente);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }


}
