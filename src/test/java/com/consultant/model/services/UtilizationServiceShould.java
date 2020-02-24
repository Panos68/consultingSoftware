package com.consultant.model.services;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.entities.Contract;
import com.consultant.model.repositories.UtilizationRepository;
import com.consultant.model.services.impl.ConsultantService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class UtilizationServiceShould {

    private UtilizationService utilizationService;

    @Mock
    private UtilizationRepository utilizationRepository;

    @Mock
    private ConsultantService consultantService;

    private Set<ConsultantDTO> consultantDTOS = new HashSet<>();

    private ConsultantDTO consultantDTO1 = new ConsultantDTO();

    private ConsultantDTO consultantDTO2 = new ConsultantDTO();

    private ConsultantDTO consultantDTO3 = new ConsultantDTO();

    private ConsultantDTO consultantDTO4 = new ConsultantDTO();

    @Before
    public void setUp() throws Exception {
        Contract contract1 = new Contract();
        consultantDTO1.setDateJoined(LocalDate.now().minusMonths(2));
        initializeContract(contract1, LocalDate.now().minusMonths(1), consultantDTO1);

        Contract contract2 = new Contract();
        consultantDTO2.setDateJoined(LocalDate.now().minusMonths(2));
        initializeContract(contract2, LocalDate.now().minusMonths(2), consultantDTO2);
        Contract contract5 = new Contract();
        initializeContract(contract5, LocalDate.now().minusDays(2), consultantDTO2);

        Contract contract3 = new Contract();
        consultantDTO3.setDateJoined(LocalDate.now().minusMonths(2));
        initializeContract(contract3, LocalDate.now().minusMonths(1).minusDays(1), consultantDTO3);
        contract3.setClientName(ContractService.OFFICE_NAME);

        Contract contract4 = new Contract();
        consultantDTO4.setDateJoined(LocalDate.now().minusMonths(1));
        initializeContract(contract4, LocalDate.now().minusDays(9), consultantDTO4);

        utilizationService = new UtilizationService(utilizationRepository, consultantService);
        Mockito.when(consultantService.getAll()).thenReturn(consultantDTOS);
    }

    private void initializeContract(Contract contract, LocalDate startDate, ConsultantDTO consultantDTO) {
        contract.setStartedDate(startDate);
        contract.setActive(true);
        contract.setClientName("Client");
        consultantDTO.getContracts().add(contract);
        consultantDTOS.add(consultantDTO);
    }

    @Test
    public void returnUtilizationPercentage() {
        System.out.println(utilizationService.calculateUtilizationPercentageOfCurrentMonth());
        Assert.assertTrue(utilizationService.calculateUtilizationPercentageOfCurrentMonth()<=100);
    }

    @Test
    public void returnAidedUtilizationPercentage() {
        System.out.println(utilizationService.calculateAidedUtilizationPercentageOfCurrentMonth());
        Assert.assertTrue(utilizationService.calculateAidedUtilizationPercentageOfCurrentMonth()<=100);
    }
}