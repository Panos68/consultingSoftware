package com.consultant.model.controller;

import com.consultant.model.dto.UtilizationDTO;
import com.consultant.model.services.UtilizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/utilization")
public class UtilizationController {
    UtilizationService utilizationService;

    @Autowired
    public UtilizationController(UtilizationService utilizationService) {
        this.utilizationService = utilizationService;
    }


    @GetMapping()
    public ResponseEntity<UtilizationDTO> getUtilization(){

        return ResponseEntity.ok(utilizationService.getUtilization());
    }

}
