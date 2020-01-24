package com.consultant.model.services.impl;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.entities.Consultant;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.ConsultantRepository;
import com.consultant.model.services.ClientTeamService;
import com.consultant.model.services.ConsultantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ConsultantServiceImpl implements ConsultantsService {

    ConsultantRepository consultantRepository;

    ConversionService conversionService;

    ClientTeamService clientTeamService;

    @Autowired
    public ConsultantServiceImpl(ConsultantRepository consultantRepository, ConversionService conversionService, ClientTeamService clientTeamService) {
        this.consultantRepository = consultantRepository;
        this.conversionService = conversionService;
        this.clientTeamService = clientTeamService;
    }

    @Override
    public Set<ConsultantDTO> getAllConsultants() {
        List<Consultant> clientCompanyList = consultantRepository.findAll();
        Set<ConsultantDTO> clientCompanyDTOS = new HashSet<>();
        clientCompanyList.forEach(consultant -> {
            final ConsultantDTO consultantDTO = conversionService.convert(consultant, ConsultantDTO.class);
            clientCompanyDTOS.add(consultantDTO);
        });

        return clientCompanyDTOS;
    }

    @Override
    public void createConsultant(ConsultantDTO consultantDTO) throws NoMatchException {
        final Consultant consultant = conversionService.convert(consultantDTO, Consultant.class);
        clientTeamService.assignConsultantToTeam(consultant, consultantDTO.getTeamId());
        consultantRepository.saveAndFlush(consultant);
    }

    @Override
    public void editConsultant(ConsultantDTO consultantDTO) throws NoMatchException {
        Consultant existingConsultant = getExistingConsultantById(consultantDTO.getId());

        Consultant updatedConsultant = updateConsultant(existingConsultant, consultantDTO);

        clientTeamService.assignConsultantToTeam(existingConsultant, consultantDTO.getTeamId());

        consultantRepository.saveAndFlush(updatedConsultant);
    }

    private Consultant updateConsultant(Consultant existingConsultant, ConsultantDTO consultantDTO) {
        existingConsultant.setId(consultantDTO.getId());
        existingConsultant.setStatus(consultantDTO.getStatus());
        existingConsultant.setFirstName(consultantDTO.getFirstName());
        existingConsultant.setLastName(consultantDTO.getLastName());
        existingConsultant.setPrice(consultantDTO.getPrice());
        existingConsultant.setListPrice(consultantDTO.getListPrice());
        existingConsultant.setDiscount(consultantDTO.getDiscount());
        existingConsultant.setContractStarted(consultantDTO.getContractStarted());
        existingConsultant.setContractEnding(consultantDTO.getContractEnding());
        existingConsultant.setUpdatedContractEnding(consultantDTO.getUpdatedContractEnding());
        existingConsultant.setSigned(consultantDTO.getSigned());
        existingConsultant.setOther(consultantDTO.getOther());

        return existingConsultant;
    }

    @Override
    public void deleteConsultant(Long id) throws NoMatchException {
        Consultant existingConsultant = getExistingConsultantById(id);
        consultantRepository.delete(existingConsultant);
    }

    private Consultant getExistingConsultantById(Long id) throws NoMatchException {
        Optional<Consultant> existingConsultant = consultantRepository.findById(id);
        if (!existingConsultant.isPresent()) {
            throw new NoMatchException("The id provided doesn't match any consultant");
        }

        return existingConsultant.get();
    }
}
