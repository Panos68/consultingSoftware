package com.consultant.model.controller;

import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.services.impl.ClientTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/teams")
public class ClientTeamController {

    private ClientTeamService clientTeamService;

    @Autowired
    public ClientTeamController(ClientTeamService clientTeamService) {
        this.clientTeamService = clientTeamService;
    }

    @GetMapping
    public ResponseEntity<Set<ClientTeamDTO>> getAllTeams(){
        return ResponseEntity.ok(clientTeamService.getAll());
    }

    @PostMapping
    public void createTeam(@RequestBody ClientTeamDTO clientTeamDTO) throws Exception {
        clientTeamService.create(clientTeamDTO);
    }

    @PutMapping
    public void editTeam(@RequestBody ClientTeamDTO clientTeamDTO) throws Exception {
        clientTeamService.edit(clientTeamDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteTeam(@PathVariable Long id) throws Exception {
        clientTeamService.delete(id);
    }
}
