package com.consultant.model.services;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.dto.ContractDTO;
import com.consultant.model.dto.TechnologyRatingDTO;
import com.consultant.model.entities.Client;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Mock
    private TechnologyService technologyService;

    private ConsultantService consultantService;

    private Long consultantId = 1L;

    private Consultant consultant1 = new Consultant();

    private Consultant consultant2 = new Consultant();

    private List<Consultant> consultantList = new ArrayList<>();

    private ConsultantDTO consultantDTO = new ConsultantDTO();

    private ContractDTO contractDTO = new ContractDTO();

    private Contract activeContract = new Contract();

    private Client client = new Client();

    @Before
    public void setUp() {
        consultantService = new ConsultantService(consultantRepository, clientTeamService, clientService, contractService, technologyService);

        consultant1.setId(consultantId);

        contractDTO.setConsultantId(consultantId);

        when(consultantRepository.findAll()).thenReturn(consultantList);

        Mockito.when(contractService.getActiveContractByConsultant(consultant1)).thenReturn(activeContract);

        when(contractService.terminateActiveContract(Mockito.any(), Mockito.any())).thenReturn(activeContract);

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
        consultantDTO.setTeamId(1L);
        consultantService.create(consultantDTO);

        verify(contractService, times(1)).createNewContract(Mockito.any());
    }

    @Test
    public void createEmptyContractWhenCreatingConsultantWithoutContract() throws NoMatchException {
        consultantDTO.setDateJoined(LocalDate.of(2020, 1, 1));
        consultantService.create(consultantDTO);

        verify(contractService, times(1)).createEmptyContract(LocalDate.of(2020, 1, 1));
    }

    @Test
    public void saveToRepositoryWhenCreatingConsultant() throws NoMatchException {
        consultantService.create(consultantDTO);

        verify(consultantRepository, times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    public void saveInRepositoryWhenDeletingExistingConsultant() throws Exception {
        consultantService.delete(consultantId,LocalDate.now());

        verify(consultantRepository, times(1)).saveAndFlush(consultant1);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenDeletingNonExistingConsultant() throws Exception {
        Mockito.when(consultantRepository.findById(consultantId)).thenReturn(Optional.empty());

        consultantService.delete(consultantId,LocalDate.now());
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

        verify(contractService, times(1)).updateContract(Mockito.any(), any());
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenUpdatingNonExistingConsultant() throws Exception {
        consultantDTO.setFirstName("Test");
        consultantDTO.setId(consultantId);
        Mockito.when(consultantRepository.findById(consultantId)).thenReturn(Optional.empty());

        consultantService.edit(consultantDTO);
    }

    @Test
    public void terminateOldContractWhenCreatingNewOneForAssignedConsultant() throws Exception {
        activeContract.setClient(client);

        consultantService.createNewContractForExistingConsultant(contractDTO);
        verify(contractService, times(1)).terminateActiveContract(Mockito.any(), Mockito.any());
    }

    @Test
    public void saveToRepositoryWhenTerminateContractForAssignedConsultant() throws Exception {
        activeContract.setClient(client);
        activeContract.setEndDate(LocalDate.of(2020, 2, 1));

        consultantService.terminateContract(consultantId, null);

        verify(consultantRepository, times(1)).saveAndFlush(Mockito.any());
    }


    @Test
    public void createNewContractForAssignedConsultant() throws Exception {
        activeContract.setClient(client);

        consultantService.createNewContractForExistingConsultant(contractDTO);

        verify(contractService, times(1)).createNewContract(Mockito.any());
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenCreatingContractForNonExistingConsultant() throws Exception {
        Mockito.when(consultantRepository.findById(consultantId)).thenReturn(Optional.empty());

        consultantService.createNewContractForExistingConsultant(contractDTO);
    }

    @Test
    public void saveRatingsWhenCreatingConsultant() throws Exception {
        Set<TechnologyRatingDTO> technologyRatingDTOS = new HashSet<>();
        technologyRatingDTOS.add(new TechnologyRatingDTO());
        consultantDTO.setRatings(technologyRatingDTOS);
        consultantService.create(consultantDTO);

        verify(technologyService, times(1)).saveRating(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void returnActiveConsultants() throws Exception {
        consultant1.setDeleted(false);
        consultantList.add(consultant1);
        consultant2.setDeleted(true);
        consultantList.add(consultant2);

        Set<ConsultantDTO> consultantDTOS = consultantService.getActiveConsultants();

        Assert.assertThat(consultantDTOS.size(), is(1));
    }
}