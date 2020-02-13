package com.consultant.model.services.impl;

import com.consultant.model.entities.HistoricalData;
import com.consultant.model.mappers.ConsultantMapper;
import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.entities.Client;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.entities.Consultant;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.ConsultantRepository;
import com.consultant.model.requests.ContractRequest;
import com.consultant.model.services.BasicOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        Consultant updatedConsultant = Consultant.updateConsultant(existingConsultant, consultantDTO);

        assignConsultantToTeam(consultantDTO.getTeamId(), updatedConsultant);
    }

    @Override
    public void delete(Long id) throws NoMatchException {
        Consultant existingConsultant = getExistingConsultantById(id);
        consultantRepository.delete(existingConsultant);
    }

    public void createNewContract(ContractRequest contractRequest) throws NoMatchException {
        Consultant existingConsultant = getExistingConsultantById(contractRequest.getConsultantId());

        addHistoricalEntry(contractRequest.getTerminatedDate(), existingConsultant);
        Consultant updatedConsultant = setContractData(existingConsultant, contractRequest);

        assignConsultantToTeam(contractRequest.getTeamId(), updatedConsultant);
    }

    public void terminateContract(ContractRequest contractRequest) throws NoMatchException {
        Consultant existingConsultant = getExistingConsultantById(contractRequest.getConsultantId());

        addHistoricalEntry(contractRequest.getTerminatedDate(), existingConsultant);
        clearContractData(existingConsultant);

        consultantRepository.saveAndFlush(existingConsultant);
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

    private void addHistoricalEntry(LocalDate terminatedDate, Consultant existingConsultant) {
        LocalDate endingDate = existingConsultant.getContractEnding();
        if (terminatedDate != null) {
            endingDate = existingConsultant.getContractEnding();
        }
        setTeamAndClientOfConsultant(existingConsultant);
        HistoricalData historicalData = new HistoricalData(existingConsultant.getClientName(), existingConsultant.getContractStarted(), endingDate);
        existingConsultant.getHistoricalDataList().add(historicalData);
    }

    private void setTeamAndClientOfConsultant(Consultant consultant) {
        Optional<ClientTeam> consultantTeam = clientTeamService.getAssignedTeamOfConsultant(consultant.getId());
        if (consultantTeam.isPresent()) {
            consultant.setTeamName(consultantTeam.get().getName());
            Optional<Client> clientOfTeam = clientService.getClientOfTeam(consultantTeam.get().getId());
            clientOfTeam.ifPresent(client -> consultant.setClientName(client.getName()));
        }
    }

    private void clearContractData(Consultant existingConsultant) {
        existingConsultant.setContractEnding(null);
        existingConsultant.setContractStarted(null);
        existingConsultant.setUpdatedContractEnding(null);
        existingConsultant.setPrice(0);
        existingConsultant.setSigned(false);
        existingConsultant.setStatus("Unassigned");
        clientTeamService.unassignedConsultantFromTeam(existingConsultant);
    }

    private Consultant setContractData(Consultant existingConsultant, ContractRequest contractRequest) {
        existingConsultant.setContractEnding(contractRequest.getContractEnding());
        existingConsultant.setContractStarted(contractRequest.getContractStarted());
        existingConsultant.setUpdatedContractEnding(contractRequest.getUpdatedContractEnding());
        existingConsultant.setPrice(contractRequest.getPrice());
        existingConsultant.setSigned(contractRequest.getSigned());
        existingConsultant.setStatus(contractRequest.getStatus());

        return existingConsultant;
    }
}
