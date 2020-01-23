package com.consultant.model.controller;

import com.consultant.model.dto.CandidateDTO;
import com.consultant.model.dto.ClientDTO;
import com.consultant.model.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/clients")
public class ClientsController {

    private ClientService clientService;

    @Autowired
    public ClientsController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<Set<ClientDTO>> getAllClients(){
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @PostMapping
    public void createCandidate(@RequestBody ClientDTO clientDTO) throws Exception {
        clientService.createClient(clientDTO);
    }

    @PutMapping
    public void editCandidate(@RequestBody ClientDTO clientDTO) throws Exception {
        clientService.editClient(clientDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteClient(@PathVariable Long id) throws Exception {
        clientService.deleteClient(id);
    }
}
