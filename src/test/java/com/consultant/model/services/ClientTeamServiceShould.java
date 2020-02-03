package com.consultant.model.services;

import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.entities.Consultant;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.ClientTeamRepository;
import com.consultant.model.services.impl.ClientServiceImpl;
import com.consultant.model.services.impl.ClientTeamServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientTeamServiceShould {


    @Mock
    private ClientTeamRepository clientTeamRepository;

    @Mock
    private ClientServiceImpl clientService;

    @InjectMocks
    private ClientTeamServiceImpl clientTeamService;

    private Long clientId = 1L;

    private Long teamId = 1L;

    private Long consultantId = 1L;

    private List<ClientTeam> clientTeamList = new ArrayList<>();

    private ClientTeamDTO clientTeamDTO = new ClientTeamDTO();

    private Consultant consultant = new Consultant();

    private ClientTeam clientTeam1 = new ClientTeam();

    private ClientTeam clientTeam2 = new ClientTeam();

    @Before
    public void setUp() {
        clientTeam1.setId(clientId);

        when(clientTeamRepository.findAll()).thenReturn(clientTeamList);
    }

    @Test
    public void returnListOfAllExistingTeams() {
        clientTeam1.setConsultants(Collections.singletonList(consultant));
        clientTeamDTO.setConsultants(Collections.singletonList(consultant));
        clientTeamList.add(clientTeam1);
        clientTeamList.add(clientTeam2);

        Set<ClientTeamDTO> clientTeamsDTOS = clientTeamService.getAllTeams();
        Assert.assertThat(clientTeamsDTOS.size(), is(2));
    }

    @Test
    public void returnEmptyListIfThereAreNoTeams() {
        when(clientTeamRepository.findAll()).thenReturn(clientTeamList);
        Set<ClientTeamDTO> clientTeamsDTOS = clientTeamService.getAllTeams();

        Assert.assertThat(clientTeamsDTOS.isEmpty(), is(true));
    }

    @Test
    public void saveToRepositoryWhenCreatingTeam() throws NoMatchException {
        clientTeamService.createTeam(clientTeamDTO);

        verify(clientTeamRepository, times(1)).saveAndFlush(clientTeam1);
    }

    @Test
    public void deleteInRepositoryWhenDeletingExistingTeam() throws Exception {
        Mockito.when(clientTeamRepository.findById(teamId)).thenReturn(Optional.ofNullable(clientTeam1));
        clientTeamService.deleteTeam(clientId);

        verify(clientTeamRepository, times(1)).delete(clientTeam1);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenDeletingNonExistingTeam() throws Exception {
        clientTeamService.deleteTeam(teamId);
    }

    @Test
    public void updateOnEditingExistingTeam() throws Exception {
        clientTeamDTO.setLastInteractedBy("name");
        clientTeamDTO.setId(clientId);
        Mockito.when(clientTeamRepository.findById(clientId)).thenReturn(Optional.ofNullable(clientTeam1));
        clientTeamService.editTeam(clientTeamDTO);

        verify(clientTeamRepository, times(1)).saveAndFlush(clientTeam1);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenUpdatingNonExistingTeam() throws Exception {
        clientTeamDTO.setLastInteractedBy("name");
        clientTeamDTO.setId(clientId);
        Mockito.when(clientTeamRepository.findById(clientId)).thenReturn(Optional.empty());
        clientTeamService.editTeam(clientTeamDTO);
    }

    @Test
    public void saveToRepositoryWhenAssigningConsultantToExistingTeam() throws Exception {
        Mockito.when(clientTeamRepository.findById(clientId)).thenReturn(Optional.ofNullable(clientTeam1));
        clientTeamService.assignConsultantToTeam(consultant,teamId);
    }

    @Test(expected = NoMatchException.class)
    public void throwNoMatchExceptionWhenAssigningTeamToNonExistingCandidate() throws Exception {
        Mockito.when(clientTeamRepository.findById(clientId)).thenReturn(Optional.empty());
        clientTeamService.assignConsultantToTeam(consultant,clientId);
    }

    @Test
    public void returnAssignedClientOfTeam() {
        Mockito.when(clientTeamRepository.findByConsultantId(consultantId)).thenReturn(Optional.ofNullable(clientTeam1));
        Optional<ClientTeam> assignedTeamOfConsultant = clientTeamService.getAssignedTeamOfConsultant(consultantId);
        Assert.assertEquals(clientTeam1,assignedTeamOfConsultant.get());
    }
}