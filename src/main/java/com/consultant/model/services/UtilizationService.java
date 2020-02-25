package com.consultant.model.services;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.dto.UtilizationDTO;
import com.consultant.model.entities.Contract;
import com.consultant.model.entities.Utilization;
import com.consultant.model.mappers.UtilizationMapper;
import com.consultant.model.repositories.UtilizationRepository;
import com.consultant.model.services.impl.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
public class UtilizationService {

    UtilizationRepository utilizationRepository;

    ConsultantService consultantService;

    @Autowired
    public UtilizationService(UtilizationRepository utilizationRepository, ConsultantService consultantService) {
        this.utilizationRepository = utilizationRepository;
        this.consultantService = consultantService;
    }

    @Scheduled(cron = "0 0 9 1 * ?")
    public void saveUtToDb() {
        LocalDate firstDayOfPreviousMonth = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().minusMonths(1).getMonthValue(), 1);
        Optional<Utilization> optionalUtilization = utilizationRepository.findByDate(firstDayOfPreviousMonth);
        Utilization utilization;
        if (optionalUtilization.isPresent()) {
            utilization = optionalUtilization.get();
        } else {
            utilization = new Utilization();
            utilization.setDate(firstDayOfPreviousMonth);
        }
        utilization.setAidedUt(calculateAidedUtilizationPercentageOfCurrentMonth());
        utilization.setUt(calculateUtilizationPercentageOfCurrentMonth());
        CalculateThreeMonthsUtilizations(firstDayOfPreviousMonth, utilization);
        utilizationRepository.saveAndFlush(utilization);
    }

    public UtilizationDTO getCurrentUtilization() {
        LocalDate firstDayOfPreviousMonth = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().minusMonths(1).getMonthValue(), 1);
        Optional<Utilization> optionalOneMonthAgoUtilization = utilizationRepository.findByDate(firstDayOfPreviousMonth);
        Utilization utilization;
        if (optionalOneMonthAgoUtilization.isPresent()) {
            utilization = optionalOneMonthAgoUtilization.get();
        } else {
            utilization = new Utilization();
            utilization.setDate(LocalDate.now());
            utilization.setAidedUt(calculateAidedUtilizationPercentageOfCurrentMonth());
            utilization.setUt(calculateUtilizationPercentageOfCurrentMonth());
            CalculateThreeMonthsUtilizations(firstDayOfPreviousMonth, utilization);
        }
        return UtilizationMapper.INSTANCE.utilizationToUtilizationDTO(utilization);
    }

    private void CalculateThreeMonthsUtilizations(LocalDate firstDayOfPreviousMonth, Utilization utilization) {
        Optional<Utilization> optionalTwoMonthsAgoUtilization = utilizationRepository.findByDate(firstDayOfPreviousMonth.minusMonths(1));
        Optional<Utilization> optionalThreeMonthsAgoUtilization = utilizationRepository.findByDate(firstDayOfPreviousMonth.minusMonths(2));
        if (optionalTwoMonthsAgoUtilization.isPresent() && optionalThreeMonthsAgoUtilization.isPresent()) {
            utilization.setThreeMonthsUt((utilization.getUt() + optionalTwoMonthsAgoUtilization.get().getUt() + optionalThreeMonthsAgoUtilization.get().getUt()) / 3);
            utilization.setThreeMonthsAidedUt((utilization.getAidedUt() + optionalTwoMonthsAgoUtilization.get().getAidedUt() + optionalThreeMonthsAgoUtilization.get().getAidedUt()) / 3);
        }
    }

    double calculateUtilizationPercentageOfCurrentMonth() {
        Set<ConsultantDTO> consultants = consultantService.getAll();
        int previousMonthMaxDays = getPastMonthsMaxDays(1);
        AtomicInteger assignedDays = new AtomicInteger();

        LocalDate firstDayOfCurrentMonth = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);

        List<Contract> contractsToCheck = new ArrayList<>();

        consultants
                .forEach(consultantDTO -> {
                    Optional<Contract> consultantContractOfLastMonth = consultantDTO.getContracts().stream().filter(contract -> contract.getStartedDate().isBefore(firstDayOfCurrentMonth))
                            .sorted(Comparator.comparing(Contract::getStartedDate).reversed())
                            .findFirst();
                    consultantContractOfLastMonth.ifPresent(contractsToCheck::add);
                });

        contractsToCheck.stream()
                .filter(contract -> !contract.getClientName().equals(ContractService.OFFICE_NAME))
                .forEach(c -> {
                    if (c.getStartedDate().getMonthValue() == LocalDate.now().minusMonths(1).getMonthValue()) {
                        assignedDays.addAndGet(previousMonthMaxDays - c.getStartedDate().getDayOfMonth());
                    } else {
                        assignedDays.addAndGet(previousMonthMaxDays);
                    }
                });

        double maxAssignedDays = previousMonthMaxDays * contractsToCheck.size();

        return assignedDays.get() / maxAssignedDays * 100;
    }

    double calculateAidedUtilizationPercentageOfCurrentMonth() {

        Set<ConsultantDTO> consultants = consultantService.getAll();
        int previousMonthMaxDays = getPastMonthsMaxDays(1);
        AtomicInteger assignedDays = new AtomicInteger();

        LocalDate firstDayOfCurrentMonth = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);

        List<Contract> contractsToCheck = new ArrayList<>();

        List<ConsultantDTO> consultantsJoinedBeforeCurrentMonth = consultants.stream()
                .filter(consultantDTO -> consultantDTO.getDateJoined().isBefore(firstDayOfCurrentMonth))
                .collect(Collectors.toList());

        consultantsJoinedBeforeCurrentMonth
                .forEach(consultantDTO -> {
                    Optional<Contract> consultantContractOfLastMonth = consultantDTO.getContracts().stream()
                            .sorted(Comparator.comparing(Contract::getStartedDate).reversed())
                            .findFirst();
                    consultantContractOfLastMonth.ifPresent(contractsToCheck::add);
                });

        List<Contract> nonFullyCoveredAidedContracts = consultantsJoinedBeforeCurrentMonth.stream()
                .filter(c -> c.getDateJoined().getMonthValue() == LocalDate.now().minusMonths(4).getMonthValue())
                .flatMap(con -> con.getContracts().stream())
                .filter(contract -> contract.getActive() && contract.getClientName().equals(ContractService.OFFICE_NAME))
                .collect(Collectors.toList());

        List<Contract> aidedActiveContracts = consultantsJoinedBeforeCurrentMonth.stream()
                .filter(c -> c.getDateJoined().isAfter(LocalDate.now().minusMonths(4)) && c.getDateJoined().getMonthValue() != LocalDate.now().minusMonths(1).getMonthValue())
                .flatMap(con -> con.getContracts().stream())
                .collect(Collectors.toList());


        contractsToCheck
                .forEach(c -> {
                    if ((c.getStartedDate().getMonthValue() == LocalDate.now().minusMonths(1).getMonthValue() || nonFullyCoveredAidedContracts.contains(c))
                            && !aidedActiveContracts.contains(c)) {
                        assignedDays.addAndGet(previousMonthMaxDays - c.getStartedDate().getDayOfMonth());
                    } else if (c.getStartedDate().getMonthValue() != LocalDate.now().minusMonths(1).getMonthValue() || aidedActiveContracts.contains(c)) {
                        assignedDays.addAndGet(previousMonthMaxDays);
                    }
                    //if contract is in the office but it's not aided then it doesn't add any days
                });

        double maxAssignedDays = previousMonthMaxDays * contractsToCheck.size();

        return assignedDays.get() / maxAssignedDays * 100;
    }

    private int getPastMonthsMaxDays(int monthsInThePast) {
        YearMonth yearMonth = YearMonth.now().minusMonths(monthsInThePast);
        return yearMonth.lengthOfMonth();
    }

    public Set<UtilizationDTO> getAllUtilization() {
        List<Utilization> utilizationList = utilizationRepository.findAll();
        Set<UtilizationDTO> utilizationDTOS = new HashSet<>();
        utilizationList.forEach(utilization -> {
            UtilizationDTO utilizationDTO = UtilizationMapper.INSTANCE.utilizationToUtilizationDTO(utilization);
            utilizationDTOS.add(utilizationDTO);
        });
        return utilizationDTOS;
    }
}
