package com.consultant.model.services;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.entities.Consultant;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.ConsultantRepository;
import com.consultant.model.services.impl.ConsultantServiceImpl;
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

    private ConsultantService consultantService;

    private Long consultantId = 1L;

    private Consultant consultant1 = new Consultant();

    private Consultant consultant2 = new Consultant();

    private List<Consultant> consultantList = new ArrayList<>();

    private ConsultantDTO consultantDTO = new ConsultantDTO();


    @Before
    public void setUp() {
        consultantService = new ConsultantServiceImpl(consultantRepository,clientTeamService,clientService);

        consultant1.setId(consultantId);

        when(consultantRepository.findAll()).thenReturn(consultantList);
    }

    @Test
    public void returnListOfAllExistingConsultants() {
        consultantList.add(consultant1);
        consultantList.add(consultant2);

        Set<ConsultantDTO> consultantDTOS = consultantService.getAllConsultants();
        Assert.assertThat(consultantDTOS.size(), is(2));
    }

    @Test
    public void returnEmptyListIfThereAreNoConsultants() {
        when(consultantRepository.findAll()).thenReturn(Collections.emptyList());
        Set<ConsultantDTO> consultantDTOS = consultantService.getAllConsultants();

        Assert.assertThat(consultantDTOS.isEmpty(), is(true));
    }

    @Test
    public void saveToRepositoryWhenCreatingConsultant() throws NoMatchException {
        consultantService.createConsultant(consultantDTO);

        verify(consultantRepository, times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    public void deleteInRepositoryWhenDeletingExistingConsultant() throws Exception {
        Mockito.when(consultantRepository.findById(consultantId)).thenReturn(Optional.ofNullable(consultant1));
        consultantService.deleteConsultant(consultantId);

        verify(consultantRepository, times(1)).delete(consultant1);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenDeletingNonExistingConsultant() throws Exception {
        consultantService.deleteConsultant(consultantId);
    }

    @Test
    public void updateOnEditingExistingConsultant() throws Exception {
        consultantDTO.setPrice(100);
        consultantDTO.setId(consultantId);
        Mockito.when(consultantRepository.findById(consultantId)).thenReturn(Optional.ofNullable(consultant1));
        consultantService.editConsultant(consultantDTO);

        verify(consultantRepository, times(1)).saveAndFlush(consultant1);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenUpdatingNonExistingConsultant() throws Exception {
        consultantDTO.setPrice(100);
        consultantDTO.setId(consultantId);
        Mockito.when(consultantRepository.findById(consultantId)).thenReturn(Optional.empty());

        consultantService.editConsultant(consultantDTO);
    }
}