package com.consultant.model.services;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.dto.ContractDTO;
import com.consultant.model.entities.Consultant;
import com.consultant.model.entities.Contract;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.ConsultantRepository;
import com.consultant.model.services.impl.ClientService;
import com.consultant.model.services.impl.ClientTeamService;
import com.consultant.model.services.impl.ConsultantService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConsultantServiceShould {

    @Mock
    private ClientTeamService clientTeamService;

    @Mock
    private ClientService clientService;

    @Mock
    private ConsultantRepository consultantRepository;

    @Mock
    private ContractService contractService;


    private ConsultantService consultantService;

    private Long consultantId = 1L;

    private Consultant consultant1 = new Consultant();

    private Consultant consultant2 = new Consultant();

    private List<Consultant> consultantList = new ArrayList<>();

    private ConsultantDTO consultantDTO = new ConsultantDTO();

    private ContractDTO contractDTO = new ContractDTO();

    private Contract activeContract = new Contract();

    @Before
    public void setUp() {
        consultantService = new ConsultantService(consultantRepository,clientTeamService,clientService,contractService);

        consultant1.setId(consultantId);

        contractDTO.setConsultantId(consultantId);

        when(consultantRepository.findAll()).thenReturn(consultantList);

        Mockito.when(contractService.getActiveContractByConsultant(consultant1)).thenReturn(activeContract);

        Mockito.when(consultantRepository.findById(consultantId)).thenReturn(Optional.ofNullable(consultant1));
    }

    @Test
    public void returnListOfAllExistingConsultants() {
        consultantList.add(consultant1);
        consultantList.add(consultant2);

        Set<ConsultantDTO> consultantDTOS = consultantService.getAll();
        Assert.assertThat(consultantDTOS.size(), is(2));
    }

    @Test
    public void returnEmptyListIfThereAreNoConsultants() {
        when(consultantRepository.findAll()).thenReturn(Collections.emptyList());
        Set<ConsultantDTO> consultantDTOS = consultantService.getAll();

        Assert.assertThat(consultantDTOS.isEmpty(), is(true));
    }

    @Test
    public void createNewContractWhenCreatingConsultantWithContract() throws NoMatchException {
        consultantDTO.setActiveContract(contractDTO);
        consultantService.create(consultantDTO);

        verify(contractService, times(1)).createNewContract(Mockito.any());
    }

    @Test
    public void createEmptyContractWhenCreatingConsultantWithoutContract() throws NoMatchException {
        consultantService.create(consultantDTO);

        verify(contractService, times(1)).createEmptyContract();
    }

    @Test
    public void saveToRepositoryWhenCreatingConsultant() throws NoMatchException {
        consultantService.create(consultantDTO);

        verify(consultantRepository, times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    public void deleteInRepositoryWhenDeletingExistingConsultant() throws Exception {
        consultantService.delete(consultantId);

        verify(consultantRepository, times(1)).delete(consultant1);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenDeletingNonExistingConsultant() throws Exception {
        Mockito.when(consultantRepository.findById(consultantId)).thenReturn(Optional.empty());

        consultantService.delete(consultantId);
    }

    @Test
    public void updateOnEditingExistingConsultant() throws Exception {
        consultantDTO.setFirstName("Test");
        consultantDTO.setId(consultantId);
        consultantService.edit(consultantDTO);

        verify(consultantRepository, times(1)).saveAndFlush(consultant1);
    }

    @Test
    public void updateContractWhenEditConsultantsContract() throws NoMatchException {
        consultantDTO.setId(consultantId);
        consultantDTO.setActiveContract(contractDTO);
        consultantService.edit(consultantDTO);

        verify(contractService, times(1)).updateContract(Mockito.any(),any());
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenUpdatingNonExistingConsultant() throws Exception {
        consultantDTO.setFirstName("Test");
        consultantDTO.setId(consultantId);
        Mockito.when(consultantRepository.findById(consultantId)).thenReturn(Optional.empty());

        consultantService.edit(consultantDTO);
    }

    @Test
    public void updateContractWhenCreatingNewContractForUnassignedConsultant() throws Exception {
        activeContract.setClientName(ContractService.OFFICE_NAME);

        consultantService.createNewContractForExistingConsultant(contractDTO);

        verify(contractService, times(1)).updateContract(Mockito.any(),Mockito.any());
    }

    @Test
    public void terminateOldContractWhenCreatingNewOneForAssignedConsultant() throws Exception {
        activeContract.setClientName("Client");

        consultantService.createNewContractForExistingConsultant(contractDTO);

        verify(contractService, times(1)).terminateActiveContract(Mockito.any(),Mockito.any());
    }

    @Test
    public void createNewContractForAssignedConsultant() throws Exception {
        activeContract.setClientName("Client");

        consultantService.createNewContractForExistingConsultant(contractDTO);

        verify(contractService, times(1)).createNewContract(Mockito.any());
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenCreatingContractForNonExistingConsultant() throws Exception {
        Mockito.when(consultantRepository.findById(consultantId)).thenReturn(Optional.empty());

        consultantService.createNewContractForExistingConsultant(contractDTO);
    }
}