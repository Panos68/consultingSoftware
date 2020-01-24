package com.consultant.model.controller;

import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.services.ClientTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/teams")
public class ClientTeamsController {

    private ClientTeamService clientTeamService;

    @Autowired
    public ClientTeamsController(ClientTeamService clientTeamService) {
        this.clientTeamService = clientTeamService;
    }

    @GetMapping
    public ResponseEntity<Set<ClientTeamDTO>> getAllTeams(){
        return ResponseEntity.ok(clientTeamService.getAllTeams());
    }

    @PostMapping
    public void createCompany(@RequestBody ClientTeamDTO clientTeamDTO) throws Exception {
        clientTeamService.createTeam(clientTeamDTO);
    }

    @PutMapping
    public void editCompany(@RequestBody ClientTeamDTO clientTeamDTO) throws Exception {
        clientTeamService.editTeam(clientTeamDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCompany(@PathVariable Long id) throws Exception {
        clientTeamService.deleteTeam(id);
    }
}
