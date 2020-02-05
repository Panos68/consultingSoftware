package com.consultant.model.services;

import com.consultant.model.dto.VacationDTO;
import com.consultant.model.entities.User;
import com.consultant.model.entities.Vacation;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.VacationRepository;
import com.consultant.model.services.impl.VacationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VacationServiceShould {

    @Mock
    private VacationRepository vacationRepository;

    @Mock
    private UserService userService;

    private VacationService vacationService;

    private Long vacationId = 1L;

    private Vacation vacation1 = new Vacation();

    private Vacation vacation2 = new Vacation();

    private List<Vacation> vacationList = new ArrayList<>();

    private VacationDTO vacationDTO = new VacationDTO();

    private User user = new User();

    @Before
    public void setUp() {
        vacationService = new VacationService(vacationRepository,userService);
        user.setUsername("username");
        user.setId(1L);

        vacation1 = new Vacation();
        vacation1.setId(vacationId);
        when(vacationRepository.findAll()).thenReturn(vacationList);

        vacationDTO = new VacationDTO();
    }

    @Test
    public void returnListOfAllExistingVacations() throws Exception {
        vacationList.add(vacation1);
        vacationList.add(vacation2);
        when(userService.getUserByVacationId(Mockito.any())).thenReturn(user);

        Set<VacationDTO> vacationDTOS = vacationService.getAll();

        Assert.assertThat(vacationDTOS.size(), is(2));
    }

    @Test
    public void returnEmptyListIfThereAreNoVacations() throws Exception {
        when(vacationRepository.findAll()).thenReturn(vacationList);

        Set<VacationDTO> vacationDTOS = vacationService.getAll();

        Assert.assertThat(vacationDTOS.isEmpty(), is(true));
    }

    @Test
    public void saveToRepositoryWhenCreatingVacation() throws Exception {
        vacationDTO = new VacationDTO();
        vacationDTO.setId(vacationId);

        vacationService.create(vacationDTO);

        verify(userService, times(1)).updateUserVacations(vacation1);
    }

    @Test
    public void deleteInRepositoryWhenDeletingExistingVacation() throws Exception {
        Mockito.when(vacationRepository.findById(vacationId)).thenReturn(Optional.ofNullable(vacation1));

        vacationService.delete(vacationId);

        verify(vacationRepository, times(1)).delete(vacation1);
    }

    @Test(expected = NoMatchException.class)
    public void throwExceptionWhenDeletingNonExistingVacation() throws Exception {
        Mockito.when(vacationRepository.findById(vacationId)).thenReturn(Optional.empty());

        vacationService.delete(vacationId);
    }

    @Test
    public void updateOnEditingExistingCandidate() throws Exception {
        vacationDTO.setDescription("updated");
        vacationDTO.setId(vacationId);
        Mockito.when(vacationRepository.findById(vacationId)).thenReturn(Optional.ofNullable(vacation1));

        vacationService.edit(vacationDTO);

        verify(vacationRepository, times(1)).saveAndFlush(vacation1);
    }

    @Test(expected = NoMatchException.class)
    public void throwExceptionWhenUpdatingNonExistingCandidate() throws Exception {
        vacationDTO.setDescription("updated");
        vacationDTO.setId(vacationId);

        Mockito.when(vacationRepository.findById(vacationId)).thenReturn(Optional.empty());

        vacationService.edit(vacationDTO);
    }

    @Test
    public void returnListOfUserVacations() throws Exception {
        vacationList.add(vacation1);
        vacationList.add(vacation2);

        when(vacationRepository.findByUserId(user.getId())).thenReturn(vacationList);
        when(userService.getUserByVacationId(Mockito.any())).thenReturn(user);
        Set<VacationDTO> vacationDTOS = vacationService.getVacationsOfUser(user.getId());

        Assert.assertThat(vacationDTOS.size(), is(2));
    }

}
