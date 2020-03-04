package com.consultant.model.services.impl;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.dto.ContractDTO;
import com.consultant.model.entities.Client;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.entities.Consultant;
import com.consultant.model.entities.Contract;
import com.consultant.model.entities.TechnologyRating;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.mappers.ConsultantMapper;
import com.consultant.model.mappers.TechnologyMapper;
import com.consultant.model.repositories.ConsultantRepository;
import com.consultant.model.services.BasicOperationsService;
import com.consultant.model.services.ContractService;
import com.consultant.model.services.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.consultant.model.services.ContractService.OFFICE_NAME;

@Service
public class ConsultantService implements BasicOperationsService<ConsultantDTO> {

    ConsultantRepository consultantRepository;

    ClientTeamService clientTeamService;

    ClientService clientService;

    ContractService contractService;

    TechnologyService technologyService;


    @Autowired
    public ConsultantService(ConsultantRepository consultantRepository, ClientTeamService clientTeamService,
                             ClientService clientService, ContractService contractService, TechnologyService technologyService) {
        this.consultantRepository = consultantRepository;
        this.clientTeamService = clientTeamService;
        this.clientService = clientService;
        this.contractService = contractService;
        this.technologyService = technologyService;
    }

    @Override
    public Set<ConsultantDTO> getAll() {
        List<Consultant> consultantsList = consultantRepository.findAll();
        Set<ConsultantDTO> consultantsDTOS = new HashSet<>();
        consultantsList.forEach(consultant -> {
            setTeamAndClientOfConsultant(consultant);
            final ConsultantDTO consultantDTO = ConsultantMapper.INSTANCE.consultantToConsultantDTO(consultant);
            consultant.getRatings().forEach(r -> consultantDTO.getRatings().add(TechnologyMapper.INSTANCE.technologyRatingToTechnologyRatingDTO(r)));
            consultantsDTOS.add(consultantDTO);
        });

        return consultantsDTOS;
    }

    @Override
    public void create(ConsultantDTO consultantDTO) throws NoMatchException {
        final Consultant consultant = ConsultantMapper.INSTANCE.consultantDTOToConsultant(consultantDTO);

        if (consultantDTO.getActiveContract() != null) {
            consultantDTO.getActiveContract().setTeamId(consultantDTO.getTeamId());
            Contract newContract = contractService.createNewContract(consultantDTO.getActiveContract());
            consultant.setContracts(Collections.singletonList(newContract));
        } else {
            Contract emptyContract = contractService.createEmptyContract();
            consultant.setContracts(Collections.singletonList(emptyContract));
        }
        consultantRepository.saveAndFlush(consultant);

        if (consultant.getRatings() != null) {
            consultant.getRatings().forEach(rating -> {
                technologyService.saveRating(rating.getTechnology(), consultant.getId(), rating);
            });
        }

        assignConsultantToTeam(consultantDTO.getTeamId(), consultant);
    }

    @Override
    public void edit(ConsultantDTO consultantDTO) throws NoMatchException {
        Consultant existingConsultant = getExistingConsultantById(consultantDTO.getId());

        Consultant updatedConsultant = Consultant.updateConsultant(existingConsultant, consultantDTO);
        if (consultantDTO.getRatings() != null) {
            List<TechnologyRating> technologyRatings = consultantDTO.getRatings().stream()
                    .map(TechnologyMapper.INSTANCE::technologyRatingDTOToTechnologyRating)
                    .collect(Collectors.toList());
            technologyService.updateRatings(technologyRatings, consultantDTO.getId());
        }

        Contract activeContract = contractService.getActiveContractByConsultant(updatedConsultant);

        if (consultantDTO.getActiveContract() != null) {
            consultantDTO.getActiveContract().setTeamId(consultantDTO.getTeamId());
            contractService.updateContract(activeContract, consultantDTO.getActiveContract());
        }
        consultantRepository.saveAndFlush(updatedConsultant);

        assignConsultantToTeam(consultantDTO.getTeamId(), updatedConsultant);
    }

    @Override
    public void delete(Long id) throws NoMatchException {
        Consultant existingConsultant = getExistingConsultantById(id);
        consultantRepository.delete(existingConsultant);
    }

    public void createNewContractForExistingConsultant(ContractDTO contractDTO) throws NoMatchException {
        Consultant consultant = getExistingConsultantById(contractDTO.getConsultantId());

        Contract activeContract = contractService.getActiveContractByConsultant(consultant);
        if (activeContract.getClientName().equals(OFFICE_NAME)) {
            contractService.updateContract(activeContract, contractDTO);
        } else {
            contractService.terminateActiveContract(contractDTO.getTerminatedDate(), consultant);
            Contract newContract = contractService.createNewContract(contractDTO);
            consultant.getContracts().add(newContract);
        }
        consultantRepository.saveAndFlush(consultant);

        assignConsultantToTeam(contractDTO.getTeamId(), consultant);
    }

    public void terminateContract(Long consultantId, LocalDate terminatedDate) throws NoMatchException {
        Consultant existingConsultant = getExistingConsultantById(consultantId);

        Contract activeContract = contractService.getActiveContractByConsultant(existingConsultant);
        if (!activeContract.getClientName().equals(OFFICE_NAME)) {
            contractService.terminateActiveContract(terminatedDate, existingConsultant);
            Contract emptyContract = contractService.createEmptyContract();
            existingConsultant.getContracts().add(emptyContract);
            clientTeamService.unassignedConsultantFromTeam(existingConsultant);
            consultantRepository.saveAndFlush(existingConsultant);
        }
    }

    private Consultant getExistingConsultantById(Long id) throws NoMatchException {
        Optional<Consultant> existingConsultant = consultantRepository.findById(id);
        if (!existingConsultant.isPresent()) {
            throw new NoMatchException("The id provided doesn't match any consultant");
        }

        return existingConsultant.get();
    }

    /**
     * Checks if an id was sent to be saved for the consultant. If exists it assigns the consultant to that team
     *
     * @param teamId     the id of the team to be assigned to
     * @param consultant the updated consultant to be saved
     * @throws NoMatchException
     */
    private void assignConsultantToTeam(Long teamId, Consultant consultant) throws NoMatchException {
        if (teamId != null) {
            clientTeamService.assignConsultantToTeam(consultant, teamId);
        }
    }

    private void setTeamAndClientOfConsultant(Consultant consultant) {
        Optional<ClientTeam> consultantTeam = clientTeamService.getAssignedTeamOfConsultant(consultant.getId());
        if (consultantTeam.isPresent()) {
            consultant.setTeamName(consultantTeam.get().getName());
            consultant.setTeamId(consultantTeam.get().getId());
            Optional<Client> clientOfTeam = clientService.getClientOfTeam(consultantTeam.get().getId());
            clientOfTeam.ifPresent(client -> consultant.setClientName(client.getName()));
        }
    }
}
