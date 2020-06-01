package com.consultant.model.controller;

import com.consultant.model.dto.ClientDTO;
import com.consultant.model.services.impl.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<Set<ClientDTO>> getAllClients(){
        return ResponseEntity.ok(clientService.getAll());
    }

    @PostMapping
    public Long createClient(@RequestBody ClientDTO clientDTO) throws Exception {
        return clientService.create(clientDTO);
    }

    @PutMapping
    public void editClient(@RequestBody ClientDTO clientDTO) throws Exception {
        clientService.edit(clientDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteClient(@PathVariable Long id) throws Exception {
        clientService.delete(id);
    }
}
