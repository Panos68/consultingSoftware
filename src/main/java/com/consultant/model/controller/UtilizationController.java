package com.consultant.model.controller;

import com.consultant.model.dto.UtilizationDTO;
import com.consultant.model.services.UtilizationService;
import com.consultant.model.utils.CsvUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Set;

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
    public ResponseEntity<Set<UtilizationDTO>> getUtilization() {
        return ResponseEntity.ok(utilizationService.getAllUtilization());
    }

    @PostMapping(value = "/calculate/current")
    public void calculateUtilization() {
        utilizationService.saveUtToDb();
    }

    @PostMapping(value = "/calculate/all")
    public void calculateUtilizationOfAllMonths() {
        utilizationService.reCalcAllUtil();
    }

    @GetMapping(value = "/export")
    public void exportCSV(HttpServletResponse response) throws Exception {
        ArrayList<UtilizationDTO> utilizationDTOS = new ArrayList<>(utilizationService.getAllUtilization());


        CsvUtils.downloadCsv(utilizationDTOS, UtilizationDTO.class, response, "utilization.csv");
    }
}
