package com.consultant.model.controller;

import com.consultant.model.dto.CandidateDTO;
import com.consultant.model.services.impl.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

    private CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public ResponseEntity<Set<CandidateDTO>> getAllCandidates(){
        return ResponseEntity.ok(candidateService.getAll());
    }

    @PostMapping
    public Long createCandidate(@RequestBody CandidateDTO candidateDTO) throws Exception {
        return candidateService.create(candidateDTO);
    }

    @PutMapping
    public void editCandidate(@RequestBody CandidateDTO candidateDTO) throws Exception {
        candidateService.edit(candidateDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCandidate(@PathVariable Long id) throws Exception {
        candidateService.delete(id);
    }
}
