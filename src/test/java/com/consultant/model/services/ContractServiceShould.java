package com.consultant.model.services;

import com.consultant.model.dto.ContractDTO;
import com.consultant.model.entities.Client;
import com.consultant.model.entities.Consultant;
import com.consultant.model.entities.Contract;
import com.consultant.model.repositories.ContractRepository;
import com.consultant.model.services.impl.ClientService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ContractServiceShould {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ClientService clientService;

    private ContractService contractService;

    private Consultant consultant = new Consultant();

    private Contract activeContract = new Contract();

    private Contract expiredContract = new Contract();

    private List<Contract> contracts = new ArrayList<>();

    private LocalDate endDate = LocalDate.of(2020, 10, 10);

    private Client activeClient = new Client();

    private Client oldClient = new Client();

    @Before
    public void setUp() throws Exception {
        activeClient.setId(1L);
        oldClient.setId(2L);

        contractService = new ContractService(contractRepository, clientService);
        expiredContract.setActive(false);
        activeContract.setActive(true);
        contracts.add(expiredContract);
        contracts.add(activeContract);
        activeContract.setClient(activeClient);
        expiredContract.setClient(oldClient);
        activeContract.setEndDate(endDate);
        consultant.setContracts(contracts);
    }

    @Test
    public void returnActiveContract() {
        Contract activeContractByConsultant = contractService.getActiveContractByConsultant(consultant);
        Assert.assertEquals(activeContractByConsultant,activeContract);
    }

    @Test
    public void createEmptyContractWithNullClient() {
        Contract emptyContract = contractService.createEmptyContract(LocalDate.now());
        assertNull(emptyContract.getClient());
    }

    @Test
    public void terminateActiveContract() {
        contractService.terminateActiveContract(null,consultant);
        Optional<Contract> activeContract = consultant.getContracts().stream().filter(Contract::getActive).findFirst();
        assertFalse(activeContract.isPresent());
    }

    @Test
    public void keepEndDateIfTerminatedDateIsNull() {
        contractService.terminateActiveContract(null,consultant);
        Optional<Contract> terminatedContract = consultant.getContracts().stream().filter(c -> c.getClient().equals(activeClient)).findFirst();
        assertEquals(terminatedContract.get().getEndDate(),endDate);
    }

    @Test
    public void changeToTerminatedDateOnTermination() {
        LocalDate terminatedDate = LocalDate.of(2022, 10, 10);
        contractService.terminateActiveContract(terminatedDate,consultant);
        Optional<Contract> terminatedContract = consultant.getContracts().stream().filter(c -> c.getClient().equals(activeClient)).findFirst();
        assertEquals(terminatedContract.get().getEndDate(),terminatedDate);
    }

    @Test
    public void saveToRepositoryWhenUpdatingContract() {
        ContractDTO contractDTO = new ContractDTO();

        contractService.updateContract(activeContract,contractDTO);

        Mockito.verify(contractRepository,Mockito.times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    public void notSaveToRepositoryWhenUpdatingContractIfDTOisNull() {
        ContractDTO contractDTO = null;

        contractService.updateContract(activeContract,contractDTO);

        Mockito.verify(contractRepository,Mockito.times(0)).saveAndFlush(Mockito.any());
    }

    @Test
    public void createNewContractWithGivenValues() {
        ContractDTO contractDTO = new ContractDTO();
        Integer discount = 20;
        contractDTO.setDiscount(discount);

        Contract newContract = contractService.createNewContract(contractDTO);

        Assert.assertEquals(newContract.getDiscount(),discount);
    }
}