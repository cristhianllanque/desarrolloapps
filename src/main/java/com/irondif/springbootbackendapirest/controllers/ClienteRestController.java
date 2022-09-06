package com.irondif.springbootbackendapirest.controllers;

import com.irondif.springbootbackendapirest.models.entity.Cliente;
import com.irondif.springbootbackendapirest.models.services.IClienteService;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    @ResponseStatus(HttpStatus.OK)
    public List <Cliente> index() {
        return clienteService.findAll();
    }

    @GetMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> show(@PathVariable Long id){

        Cliente cliente = null;
        Map<String, Object> response = new HashMap<>();

        try {
            cliente = clienteService.findById(id);

        } catch (DataAccessException e){
            response.put("mensaje", "Error al realizar la consulta en la base de datos!");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(cliente == null){
            response.put("mensaje", "El cliente con el ID: ".concat(id.toString()).concat(" no existe!"));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
    }

    @PostMapping("/clientes")
    public ResponseEntity<?> create(@RequestBody Cliente cliente){

        Cliente clienteNew = null;
        Map<String, Object> response = new HashMap<>();

        try {
            clienteNew = clienteService.save(cliente);
        } catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos!");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente ha sido creado con éxito!!!");
        response.put("cliente", clienteNew);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable Long id){
        Cliente clienteActual = clienteService.findById(id);
        Cliente clienteUpdated = null;

        Map<String, Object> response = new HashMap<>();

        if(clienteActual == null){
            response.put("mensaje", "Error, no se puede editar, el cliente con el ID: ".concat(id.toString()).concat(" no existe!"));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            clienteActual.setApellido(cliente.getApellido());
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setEmail(cliente.getEmail());
            clienteActual.setCreateAt(cliente.getCreateAt());

            clienteUpdated = clienteService.save(clienteActual);
        } catch (DataAccessException e){
            response.put("mensaje", "Error al actualizar el cliente en la base de datos!");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente ha sido actualizado con éxito!!!");
        response.put("cliente", clienteUpdated);

        return new ResponseEntity<Map <String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();

        try {
            clienteService.delete(id);
        } catch (DataAccessException e){
            response.put("mensaje", "Error al eliminar al cliente en la base de datos!");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente ha sido eliminado con éxito!");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
