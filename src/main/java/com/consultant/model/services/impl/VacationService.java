package com.consultant.model.services.impl;

import com.consultant.model.dto.VacationDTO;
import com.consultant.model.entities.User;
import com.consultant.model.entities.Vacation;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.mappers.VacationMapper;
import com.consultant.model.repositories.VacationRepository;
import com.consultant.model.services.BasicOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class VacationService implements BasicOperationsService<VacationDTO> {

    VacationRepository vacationRepository;

    UserService userService;

    @Autowired
    public VacationService(VacationRepository vacationRepository, UserService userService) {
        this.vacationRepository = vacationRepository;
        this.userService = userService;
    }

    @Override
    public Set<VacationDTO> getAll() {
        List<Vacation> vacationList = vacationRepository.findAll();

        return mapVacationsToDTO(vacationList);
    }

    @Override
    public void create(VacationDTO vacationDTO) throws NoMatchException {
        Vacation vacation = VacationMapper.INSTANCE.vacationDTOToVacation(vacationDTO);

        userService.updateUserVacations(vacation);
    }

    @Override
    public void edit(VacationDTO vacationDTO) throws NoMatchException {
        Optional<Vacation> existingVacation = getExistingVacationById(vacationDTO.getId());

        Vacation updatedVacation = updateVacation(existingVacation.get(), vacationDTO);

        vacationRepository.saveAndFlush(updatedVacation);
    }

    @Override
    public void delete(Long id) throws NoMatchException {
        Optional<Vacation> existingVacation = getExistingVacationById(id);

        vacationRepository.delete(existingVacation.get());
    }

    public Set<VacationDTO> getVacationsOfUser(Long userId) {
        List<Vacation> vacationList = vacationRepository.findByUserId(userId);

        return mapVacationsToDTO(vacationList);
    }

    private Set<VacationDTO> mapVacationsToDTO(List<Vacation> vacationList) {
        Set<VacationDTO> vacationDTOS = new HashSet<>();
        vacationList.forEach(vacation -> {
            setUserDetailsOfVacation(vacation);
            VacationDTO vacationDTO = VacationMapper.INSTANCE.vacationToVacationDTO(vacation);
            vacationDTOS.add(vacationDTO);
        });

        return vacationDTOS;
    }

    private void setUserDetailsOfVacation(Vacation vacation) {
        try {
            User user = userService.getUserByVacationId(vacation.getId());
            vacation.setUserName(user.getUsername());
            vacation.setUserId(user.getId());
        } catch (NoMatchException e) {
            e.printStackTrace();
        }
    }

    private Vacation updateVacation(Vacation existingVacation, VacationDTO vacationDTO) {
        existingVacation.setDescription(vacationDTO.getDescription());
        existingVacation.setEndDate(vacationDTO.getEndDate());
        existingVacation.setStartingDate(vacationDTO.getStartingDate());
        return existingVacation;
    }

    private Optional<Vacation> getExistingVacationById(Long id) throws NoMatchException {
        Optional<Vacation> existingVacation = vacationRepository.findById(id);
        if (!existingVacation.isPresent()) {
            throw new NoMatchException("The id provided doesn't match any vacation");
        }

        return existingVacation;
    }
}