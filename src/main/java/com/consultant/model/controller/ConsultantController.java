package com.consultant.model.controller;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.services.impl.ConsultantService;
import com.consultant.model.utils.CsvUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/consultants")
public class ConsultantController {

    private ConsultantService consultantService;

    @Autowired
    public ConsultantController(ConsultantService consultantService) {
        this.consultantService = consultantService;
    }

    @GetMapping
    public ResponseEntity<Set<ConsultantDTO>> getAllConsultants() {
        return ResponseEntity.ok(consultantService.getAll());
    }

    @PostMapping
    public void createConsultant(@RequestBody ConsultantDTO consultantDTO) throws NoMatchException {
        consultantService.create(consultantDTO);
    }

    @PutMapping
    public void editConsultant(@RequestBody ConsultantDTO consultantDTO) throws NoMatchException {
        consultantService.edit(consultantDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteConsultant(@PathVariable Long id,
                                 @RequestParam(value = "terminatedDate", required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate terminatedDate)
            throws NoMatchException {
        consultantService.delete(id, terminatedDate);
    }


    @GetMapping("/export")
    public void exportCSV(HttpServletResponse response) throws Exception {
        ArrayList<ConsultantDTO> consultantDTOS = new ArrayList<>(consultantService.getActiveConsultants());

        CsvUtils.downloadCsv(consultantDTOS, ConsultantDTO.class, response, "consultants.csv");
    }

    @PostMapping("/import")
    public void importCSV(@RequestParam("multipartFile") MultipartFile multipartFile) throws Exception {
        List<ConsultantDTO> consultantDTOS = CsvUtils.getEntitiesFromCsv(ConsultantDTO.class, multipartFile, true);
        for (ConsultantDTO consultantDTO : consultantDTOS) {
            consultantService.create(consultantDTO);
        }
    }
}
