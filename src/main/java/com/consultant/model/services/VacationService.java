package com.consultant.model.services;

import com.consultant.model.dto.VacationDTO;
import com.consultant.model.exception.NoMatchException;

import java.util.Set;

public interface VacationService {
    Set<VacationDTO> getAllVacations();

    void createVacation(VacationDTO vacationDTO) throws NoMatchException;

    void editVacation(VacationDTO vacationDTO) throws NoMatchException;

    void deleteVacation(Long id) throws NoMatchException;

    Set<VacationDTO> getVacationsOfUser(Long userId);
}
