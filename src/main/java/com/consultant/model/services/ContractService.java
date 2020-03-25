package com.consultant.model.services;

import com.consultant.model.dto.ContractDTO;
import com.consultant.model.entities.Client;
import com.consultant.model.entities.Consultant;
import com.consultant.model.entities.Contract;
import com.consultant.model.mappers.ContractMapper;
import com.consultant.model.repositories.ContractRepository;
import com.consultant.model.services.impl.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {
    private ContractRepository contractRepository;

    private ClientService clientService;

    @Autowired
    public ContractService(ContractRepository contractRepository, ClientService clientService) {
        this.contractRepository = contractRepository;
        this.clientService = clientService;
    }

    public Contract terminateActiveContract(LocalDate terminatedDate, Consultant existingConsultant) {
        Contract activeContract = getActiveContractByConsultant(existingConsultant);
        if (terminatedDate != null) {
            activeContract.setEndDate(terminatedDate);
        }
        if (activeContract.getEndDate() == null) {
            activeContract.setEndDate(LocalDate.now());
        }
        activeContract.setActive(false);

        return activeContract;
    }

    public Contract getActiveContractByConsultant(Consultant consultant) {
        List<Contract> consultantContracts = consultant.getContracts();

        return consultantContracts.stream().filter(Contract::getActive).findFirst().get();
    }

    public Contract createEmptyContract(LocalDate startingDate) {
        Contract contract = new Contract();
        contract.setActive(true);
        contract.setStartedDate(startingDate);

        return contract;
    }

    public void updateContract(Contract activeContract, ContractDTO contractDTO) {
        if (contractDTO != null) {

            activeContract.setEndDate(contractDTO.getEndDate());
            activeContract.setStartedDate(contractDTO.getStartedDate());
            activeContract.setUpdatedContractEnding(contractDTO.getUpdatedContractEnding());
            activeContract.setPrice(contractDTO.getPrice());
            activeContract.setSigned(contractDTO.getSigned());
            activeContract.setDiscount(contractDTO.getDiscount());
            setClient(contractDTO, activeContract);

            contractRepository.saveAndFlush(activeContract);
        }
    }

    public Contract createNewContract(ContractDTO contractDTO) {
        Contract contract = ContractMapper.INSTANCE.contractDTOToContract(contractDTO);
        setClient(contractDTO, contract);
        contract.setActive(true);

        return contract;
    }

    private void setClient(ContractDTO contractDTO, Contract contract) {
        Optional<Client> clientOfTeam = clientService.getClientOfTeam(contractDTO.getTeamId());
        clientOfTeam.ifPresent(contract::setClient);
    }
}
