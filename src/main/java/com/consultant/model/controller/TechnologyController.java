package com.consultant.model.controller;

import com.consultant.model.dto.TechnologyDTO;
import com.consultant.model.services.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/technology")
public class TechnologyController {
    private TechnologyService technologyService;

    @Autowired
    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @GetMapping
    public ResponseEntity<Set<TechnologyDTO>> getAllTechnologies() {
        return ResponseEntity.ok(technologyService.getAll());
    }
}
