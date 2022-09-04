package com.irondif.springbootbackendapirest.controllers;

import com.irondif.springbootbackendapirest.models.entity.Cliente;
import com.irondif.springbootbackendapirest.models.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    public List <Cliente> index() {
        return clienteService.findAll();
    }
}
