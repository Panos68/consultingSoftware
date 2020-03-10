package com.consultant.model.services;

import com.consultant.model.dto.ContractDTO;
import com.consultant.model.entities.Consultant;
import com.consultant.model.entities.Contract;
import com.consultant.model.mappers.ContractMapper;
import com.consultant.model.repositories.ContractRepository;
import com.consultant.model.services.impl.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContractService {
    private ContractRepository contractRepository;

    private ClientService clientService;

    public static final String OFFICE_NAME = "Mirado";

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
        contract.setClientName(OFFICE_NAME);
        contract.setStartedDate(startingDate);

        return contract;
    }

    public void updateContract(Contract activeContract, ContractDTO contractDTO) {
        if (contractDTO !=null) {

            activeContract.setEndDate(contractDTO.getEndDate());
            activeContract.setStartedDate(contractDTO.getStartedDate());
            activeContract.setUpdatedContractEnding(contractDTO.getUpdatedContractEnding());
            activeContract.setPrice(contractDTO.getPrice());
            activeContract.setSigned(contractDTO.getSigned());
            activeContract.setDiscount(contractDTO.getDiscount());
            activeContract.setClientName(clientService.getClientNameOfTeam(contractDTO.getTeamId()));

            contractRepository.saveAndFlush(activeContract);
        }
    }

    public Contract createNewContract(ContractDTO contractDTO) {
        Contract contract = ContractMapper.INSTANCE.contractDTOToContract(contractDTO);
        contract.setClientName(clientService.getClientNameOfTeam(contractDTO.getTeamId()));
        contract.setActive(true);

        return contract;
    }
}
