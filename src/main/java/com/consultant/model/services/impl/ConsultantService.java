package com.consultant.model.services.impl;

import com.consultant.model.mappers.ConsultantMapper;
import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.entities.Client;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.entities.Consultant;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.ConsultantRepository;
import com.consultant.model.services.BasicOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ConsultantService implements BasicOperationsService<ConsultantDTO> {

    ConsultantRepository consultantRepository;

    ClientTeamService clientTeamService;

    ClientService clientService;

    @Autowired
    public ConsultantService(ConsultantRepository consultantRepository, ClientTeamService clientTeamService,
                             ClientService clientService) {
        this.consultantRepository = consultantRepository;
        this.clientTeamService = clientTeamService;
        this.clientService = clientService;
    }

    @Override
    public Set<ConsultantDTO> getAll() {
        List<Consultant> consultantsList = consultantRepository.findAll();
        Set<ConsultantDTO> consultantsDTOS = new HashSet<>();
        consultantsList.forEach(consultant -> {
            setTeamAndClientOfConsultant(consultant);
            final ConsultantDTO consultantDTO = ConsultantMapper.INSTANCE.consultantToConsultantDTO(consultant);
            consultantsDTOS.add(consultantDTO);
        });

        return consultantsDTOS;
    }

    @Override
    public void create(ConsultantDTO consultantDTO) throws NoMatchException {
        final Consultant consultant = ConsultantMapper.INSTANCE.consultantDTOToConsultant(consultantDTO);

        assignConsultantToTeam(consultantDTO.getTeamId(), consultant);
    }

    @Override
    public void edit(ConsultantDTO consultantDTO) throws NoMatchException {
        Consultant existingConsultant = getExistingConsultantById(consultantDTO.getId());

        Consultant updatedConsultant = updateConsultant(existingConsultant, consultantDTO);

        assignConsultantToTeam(consultantDTO.getTeamId(), updatedConsultant);
    }

    @Override
    public void delete(Long id) throws NoMatchException {
        Consultant existingConsultant = getExistingConsultantById(id);
        consultantRepository.delete(existingConsultant);
    }


    private void setTeamAndClientOfConsultant(Consultant consultant) {
        Optional<ClientTeam> consultantTeam = clientTeamService.getAssignedTeamOfConsultant(consultant.getId());
        if (consultantTeam.isPresent()) {
            consultant.setTeamName(consultantTeam.get().getName());
            Optional<Client> clientOfTeam = clientService.getClientOfTeam(consultantTeam.get().getId());
            clientOfTeam.ifPresent(client -> consultant.setClientName(client.getName()));
        }
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
        existingConsultant.setMainTechnologies(consultantDTO.getMainTechnologies());

        return existingConsultant;
    }

    private Consultant getExistingConsultantById(Long id) throws NoMatchException {
        Optional<Consultant> existingConsultant = consultantRepository.findById(id);
        if (!existingConsultant.isPresent()) {
            throw new NoMatchException("The id provided doesn't match any consultant");
        }

        return existingConsultant.get();
    }

    /**
     * Checks if an id was sent to be saved for the consultant. If exists it assigns the consultant to that team, if not it just
     * saves the updated consultant.
     *
     * @param teamId     the id of the team to be assigned to
     * @param consultant the updated consultant to be saved
     * @throws NoMatchException
     */
    private void assignConsultantToTeam(Long teamId, Consultant consultant) throws NoMatchException {
        if (teamId != null) {
            clientTeamService.assignConsultantToTeam(consultant, teamId);
        } else {
            consultantRepository.saveAndFlush(consultant);
        }
    }
}
