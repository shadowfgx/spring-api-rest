package com.fernando.rest.springjavarest.controller;

import com.fernando.rest.springjavarest.model.Producto;
import com.fernando.rest.springjavarest.service.ProductoService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ProductoController{

    private ProductoService productoService;

    public ProductoController(ProductoService productoService){
        this.productoService = productoService;
    }

    @GetMapping("/productosJSON")
    public ResponseEntity<List<Producto>> showProductosJSON(){
        return new ResponseEntity<>(productoService.findAll(), HttpStatus.OK);
    }

    /*WITH REQUEST PARAM*/
    @GetMapping("/producto")
    public ResponseEntity<Producto> productoJSON(@RequestParam("id") Long id){
        return new ResponseEntity<>(productoService.findById(id), HttpStatus.OK);
    }

    /*WITH PATH VARIABLE*/
    /*WITH HANDLE ERRORS*/
    @GetMapping("/producto/{id}")
    public ResponseEntity<?> findProducto(@PathVariable Long id){

        Producto producto = null;
        Map<String, Object> response = new HashMap<>();

        try{
            producto = productoService.findById(id);

        }catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar consulta a base de datos");
            response.put("error", Objects.requireNonNull(e.getMessage()).concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(producto == null){
            response.put("mensaje", "El producto con ID: "+ id+" no existe");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(producto, HttpStatus.OK);
    }



    @PostMapping("/producto")
    public ResponseEntity<?> saveProductoWithHandlerErrors(@RequestBody Producto producto){

        Producto newProducto = null;
        Map<String, Object> response = new HashMap<>();

        try {
            newProducto = productoService.saveAndReturnProducto(producto);
        }catch (DataAccessException e) {
            response.put("mensaje", "Error al insertar base de datos");
            response.put("error", Objects.requireNonNull(e.getMessage()).concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El producto se ha creado exitosamente");
        response.put("producto", newProducto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




    @PutMapping("/producto/{id}")
    public ResponseEntity<?> updateProductoWithHandlerErrors(@RequestBody Producto producto, @PathVariable long id){
        Producto productoUpdate = null;

        productoUpdate = productoService.findById(id);

        Map<String, Object> response = new HashMap<>();

        if(productoUpdate == null){
            response.put("mensaje", "Error: no se puedo editar, el producto con ID: "+id+" no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try{
            productoUpdate.setCodigo(producto.getCodigo());
            productoUpdate.setMarca(producto.getMarca());
            productoUpdate.setPrecio(producto.getPrecio());
            productoUpdate.setFechaIngreso(producto.getFechaIngreso());
            productoUpdate.setDescripcion(producto.getDescripcion());
            productoUpdate.setCantidad(producto.getCantidad());
            productoUpdate.setTipo(producto.getTipo());
            productoService.saveAndReturnProducto(productoUpdate);


        }catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", Objects.requireNonNull(e.getMessage()).concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El producto ha sido actualizado con exito");
        response.put("producto", productoUpdate);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }



    @DeleteMapping("/producto/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        Producto producto = null;

        producto = productoService.findById(id);

        if(producto == null){
            response.put("mensaje", "El producto con id: "+id+" no existe en la base de datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try{
            productoService.delete(id);
        }catch (DataAccessException e) {
            response.put("mensaje", "El producto con id: "+id+" no pudo ser eliminado");
            response.put("error", Objects.requireNonNull(e.getMessage()).concat(": ").concat(e.getMostSpecificCause().getMessage()));
        }

        response.put("mensaje", "El producto con id: "+id+" se elimino correctamente");
        response.put("producto", producto);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    

}
