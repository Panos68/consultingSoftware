package com.consultant.model.controller;

import com.consultant.model.dto.ClientCompanyDTO;
import com.consultant.model.services.ClientCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/companies")
public class ClientCompaniesController {

    private ClientCompanyService clientCompanyService;

    @Autowired
    public ClientCompaniesController(ClientCompanyService clientCompanyService) {
        this.clientCompanyService = clientCompanyService;
    }

    @GetMapping
    public ResponseEntity<Set<ClientCompanyDTO>> getAllCompanies(){
        return ResponseEntity.ok(clientCompanyService.getAllCompanies());
    }

    @PostMapping
    public void createCompany(@RequestBody ClientCompanyDTO clientCompanyDTO) throws Exception {
        clientCompanyService.createCompany(clientCompanyDTO);
    }

    @PutMapping
    public void editCompany(@RequestBody ClientCompanyDTO clientCompanyDTO) throws Exception {
        clientCompanyService.editCompany(clientCompanyDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCompany(@PathVariable Long id) throws Exception {
        clientCompanyService.deleteCompany(id);
    }
}
