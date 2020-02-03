package com.consultant.model.services;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.exception.NoMatchException;

import java.util.Set;

public interface ConsultantService {
    Set<ConsultantDTO> getAllConsultants();

    void createConsultant(ConsultantDTO consultantDTO) throws NoMatchException;

    void editConsultant(ConsultantDTO consultantDTO) throws NoMatchException;

    void deleteConsultant(Long id) throws NoMatchException;
}
