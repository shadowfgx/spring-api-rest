package com.fernando.rest.springjavarest.controller;
import com.fernando.rest.springjavarest.model.Cliente;
import com.fernando.rest.springjavarest.model.Region;
import com.fernando.rest.springjavarest.service.ClienteService;
import com.fernando.rest.springjavarest.service.RegionService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ClienteController {

    private final ClienteService clienteService;
    private final RegionService regionService;

    public ClienteController(ClienteService clienteService, RegionService regionService) {
        this.clienteService = clienteService;
        this.regionService = regionService;
    }


    @GetMapping("/clientesJSON")
    public ResponseEntity<List<Cliente>> showClientesJSON() {
        return new ResponseEntity<>(clienteService.findAll(), HttpStatus.OK);
    }

    /*WITH REQUEST PARAM*/
    @GetMapping("/cliente")
    public ResponseEntity<Cliente> clienteJSON(@RequestParam("id") Long id) {
        return new ResponseEntity<>(clienteService.findById(id), HttpStatus.OK);
    }

    /*WITH PATH VARIABLE*/
    /*WITH HANDLE ERRORS*/
    @GetMapping("/cliente/{id}")
    public ResponseEntity<?> findCliente(@PathVariable Long id) {

        Cliente cliente = null;
        Map<String, Object> response = new HashMap<>();

        try {
            cliente = clienteService.findById(id);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar consulta a base de datos");
            response.put("error", Objects.requireNonNull(e.getMessage()).concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (cliente == null) {
            response.put("mensaje", "El cliente con ID: " + id + " no existe");
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
    public ResponseEntity<?> saveClienteWithHandlerErrors(@RequestBody Cliente cliente) {

        Cliente newCliente = null;
        Map<String, Object> response = new HashMap<>();

        try {
            newCliente = clienteService.saveAndReturnCliente(cliente);
        } catch (DataAccessException e) {
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
    public ResponseEntity<?> updateClienteWithHandlerErrors(@RequestBody Cliente cliente, @PathVariable long id) {
        Cliente clienteUpdate = null;

        clienteUpdate = clienteService.findById(id);

        Map<String, Object> response = new HashMap<>();

        if (clienteUpdate == null) {
            response.put("mensaje", "Error: no se puedo editar, el cliente con ID: " + id + " no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {

            clienteUpdate.setNombre(cliente.getNombre());
            clienteUpdate.setApellido(cliente.getApellido());
            clienteUpdate.setEmail(cliente.getEmail());
            clienteUpdate.setTelefono(cliente.getTelefono());
            clienteUpdate.setCreatedAt(cliente.getCreatedAt());
            clienteUpdate.setRegion(cliente.getRegion());
            clienteService.saveAndReturnCliente(clienteUpdate);

        } catch (DataAccessException e) {
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
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Cliente cliente = null;

        cliente = clienteService.findById(id);

        if (cliente == null) {
            response.put("mensaje", "El usario con id: " + id + " no existe en la base de datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            clienteService.deleteById(id);

            String nombreImagenAnterior = cliente.getImagen();

            if(nombreImagenAnterior != null && nombreImagenAnterior.length() > 0) {
                //accedemos a la ruta y al archivo como tal guardada en uploads
                Path rutaImagenAnterior = Paths.get("uploads").resolve(nombreImagenAnterior).toAbsolutePath();
                File archivoImagenAnterior = rutaImagenAnterior.toFile();
                //comprobamos la presencia fisica del archivo dentro del directorio
                if(archivoImagenAnterior.exists() && archivoImagenAnterior.canRead() ) {
                    //borramos el archivo
                    archivoImagenAnterior.delete();
                }
            }
        } catch (DataAccessException e) {
            response.put("mensaje", "El cliente con id: " + id + " no pudo ser eliminado");
            response.put("error", Objects.requireNonNull(e.getMessage()).concat(": ").concat(e.getMostSpecificCause().getMessage()));
        }

        response.put("mensaje", "El cliente con id: " + id + " se elimino correctamente");
        response.put("cliente", cliente);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    /*Method to upload image*/
    @PostMapping("/cliente/upload")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") long id) {
        Map<String, Object> response = new HashMap<>();

        Cliente cliente = clienteService.findById(id);

        if (!archivo.isEmpty()) {
            //guarda el nombre de la imagen
            String nombreArchivo =  UUID.randomUUID().toString()+"_"+archivo.getOriginalFilename().replace(" ","");
            //guarda el path de ruta donde guardar la imagen
            Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();

            try {
                //copia la imagen recibida al directorio de path
                Files.copy(archivo.getInputStream(), rutaArchivo);

            } catch (IOException e) {
                //controlamos las excepciones que podamos tener al subir archivos
                response.put("mensaje", "Error al subir la imagen del cliente");
                response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String nombreImagenAnterior = cliente.getImagen();

            if(nombreImagenAnterior != null && nombreImagenAnterior.length() > 0) {
                //accedemos a la ruta y al archivo como tal guardada en uploads
                Path rutaImagenAnterior = Paths.get("uploads").resolve(nombreImagenAnterior).toAbsolutePath();
                File archivoImagenAnterior = rutaImagenAnterior.toFile();
                //comprobamos la presencia fisica del archivo dentro del directorio
                if(archivoImagenAnterior.exists() && archivoImagenAnterior.canRead() ) {
                    //borramos el archivo
                    archivoImagenAnterior.delete();
                }
            }

            cliente.setImagen(nombreArchivo);
            clienteService.save(cliente);
            response.put("cliente",cliente);
            response.put("mensaje","Imagen subida con éxito: "+nombreArchivo);
        }
        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
    }

    @GetMapping("cliente/upload/img/{nombreImagen:.+}")
    public ResponseEntity<Resource> showImage(@PathVariable String nombreImagen){
        //accedemos a la ruta y al archivo como tal guardada en uploads
        Path rutaImagen = Paths.get("uploads").resolve(nombreImagen).toAbsolutePath();
        //comprobamos la presencia fisica del archivo dentro del directorio
        if(!rutaImagen.toFile().exists()){
            throw new RuntimeException("Error: La imagen no existe");
        }
        //creamos un objeto resource para poder mostrar la imagen
        Resource imagen = null;
        try {
            imagen = new UrlResource(rutaImagen.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //comprobamos que la imagen exista y no este vacia
        if(imagen == null || !imagen.exists() || !imagen.isReadable()){
            throw new RuntimeException("Error: La imagen no existe");
        }
        //retornamos la imagen
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+imagen.getFilename()+"\"")
                .body(imagen);
    }
    
    @GetMapping("/clientesByRegion/{idRegion}")
    public ResponseEntity<?> findClientesByRegion(@PathVariable Long idRegion){
        Region region = regionService.findById(idRegion);
        return new ResponseEntity<>(clienteService.findAllByRegion(region), HttpStatus.OK);
    }

}