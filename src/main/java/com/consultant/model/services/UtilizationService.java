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

    /**
     * Calculates every 1st of the month at 9am the utilization and saves it to the db
     */
    @Scheduled(cron = "0 0 9 1 * ?")
    public void saveUtToDb() {
        LocalDate firstDayOfPreviousMonth = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().minusMonths(1).getMonthValue(), 1);
        Utilization utilization = getUtilizationObject(firstDayOfPreviousMonth);
        calcCompleteUtil(firstDayOfPreviousMonth, utilization);
        utilizationRepository.saveAndFlush(utilization);
    }

    /**
     * Returns the utilization for the given date if it exists on the db or creates a new one if it doesn't
     *
     * @param firstDayOfPreviousMonth given date to search in the db
     * @return utilization object
     */
    private Utilization getUtilizationObject(LocalDate firstDayOfPreviousMonth) {
        Optional<Utilization> optionalUtilization = utilizationRepository.findByDate(firstDayOfPreviousMonth);
        Utilization utilization;
        if (optionalUtilization.isPresent()) {
            utilization = optionalUtilization.get();
        } else {
            utilization = new Utilization();
            utilization.setDate(firstDayOfPreviousMonth);
        }
        return utilization;
    }

    /**
     * Calculates the complete utilization object for given month and adds it to the utilization parameter
     */
    private Utilization calcCompleteUtil(LocalDate firstDayOfGivenMonth, Utilization utilization) {
        utilization.setAidedUt(calculateAidUtilPercentOfGivenMonth(firstDayOfGivenMonth));
        utilization.setUt(calcUtilPercentOfGivenMonth(firstDayOfGivenMonth));
        calcThreeMonthsUtil(firstDayOfGivenMonth, utilization);
        return utilization;
    }

    public UtilizationDTO getCurrentCompleteUtil() {
        LocalDate firstDayOfPreviousMonth = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().minusMonths(1).getMonthValue(), 1);
        Optional<Utilization> optionalOneMonthAgoUtilization = utilizationRepository.findByDate(firstDayOfPreviousMonth);
        Utilization utilization;
        if (optionalOneMonthAgoUtilization.isPresent()) {
            utilization = optionalOneMonthAgoUtilization.get();
        } else {
            utilization = new Utilization();
            utilization.setDate(LocalDate.now());
            calcCompleteUtil(firstDayOfPreviousMonth, utilization);
        }
        return UtilizationMapper.INSTANCE.utilizationToUtilizationDTO(utilization);
    }

    /**
     * calculates three months Aid Ut and three months Ut and adds it to the utilization
     *
     * @param firstDayOfGivenMonth first day of month to calculate for
     * @param utilization          object to add the calculated values
     */
    private void calcThreeMonthsUtil(LocalDate firstDayOfGivenMonth, Utilization utilization) {
        Optional<Utilization> optionalTwoMonthsAgoUtilization = utilizationRepository.findByDate(firstDayOfGivenMonth.minusMonths(1));
        Optional<Utilization> optionalThreeMonthsAgoUtilization = utilizationRepository.findByDate(firstDayOfGivenMonth.minusMonths(2));
        if (optionalTwoMonthsAgoUtilization.isPresent() && optionalThreeMonthsAgoUtilization.isPresent()) {
            int oneMonthAgoMaxDays = getMonthMaxDays(firstDayOfGivenMonth.minusMonths(1));
            int twoMonthsAgoMaxDays = getMonthMaxDays(firstDayOfGivenMonth.minusMonths(2));
            int threeMonthsAgoMaxDays = getMonthMaxDays(firstDayOfGivenMonth.minusMonths(3));
            int lastThreeMonthsTotalDays = oneMonthAgoMaxDays + twoMonthsAgoMaxDays + threeMonthsAgoMaxDays;
            utilization.setThreeMonthsUt((utilization.getUt() * oneMonthAgoMaxDays + optionalTwoMonthsAgoUtilization.get().getUt()
                    * twoMonthsAgoMaxDays + optionalThreeMonthsAgoUtilization.get().getUt() * threeMonthsAgoMaxDays) / lastThreeMonthsTotalDays);
            utilization.setThreeMonthsAidedUt((utilization.getAidedUt() * oneMonthAgoMaxDays +
                    optionalTwoMonthsAgoUtilization.get().getAidedUt() * twoMonthsAgoMaxDays +
                    optionalThreeMonthsAgoUtilization.get().getAidedUt() * threeMonthsAgoMaxDays) / lastThreeMonthsTotalDays);
        }
    }

    /**
     * Deletes all ut from the db and calculates again since the day that the first consultant joined until today.
     */
    public void reCalcAllUtil() {
        Set<ConsultantDTO> consultants = consultantService.getActiveConsultants();
        ConsultantDTO firstConsultantJoinedDTO = consultants.stream().min((Comparator.comparing(ConsultantDTO::getDateJoined))).get();
        LocalDate earliestCalculatingDate = firstConsultantJoinedDTO.getDateJoined().plusMonths(1);
        LocalDate firstDayOfCalculatingMonth = LocalDate.of(earliestCalculatingDate.getYear(), earliestCalculatingDate.getMonthValue(), 1);
        utilizationRepository.deleteAll();
        while (firstDayOfCalculatingMonth.isBefore(LocalDate.now())) {
            Utilization utilization = getUtilizationObject(firstDayOfCalculatingMonth);
            utilization.setDate(firstDayOfCalculatingMonth);
            calcCompleteUtil(firstDayOfCalculatingMonth, utilization);
            utilizationRepository.saveAndFlush(utilization);
            firstDayOfCalculatingMonth = firstDayOfCalculatingMonth.plusMonths(1);
        }
    }

    double calcUtilPercentOfGivenMonth(LocalDate givenDate) {
        Set<ConsultantDTO> consultants = consultantService.getActiveConsultants();

        AtomicInteger totalAssignedDays = new AtomicInteger();
        AtomicInteger maxAssignedDays = new AtomicInteger();

        LocalDate firstDayOfGivenMonth = LocalDate.of(givenDate.getYear(), givenDate.getMonthValue(), 1);

        List<ConsultantDTO> consultantsJoinedBeforeGivenMonth = consultants.stream()
                .filter(consultantDTO -> consultantDTO.getDateJoined().isBefore(firstDayOfGivenMonth))
                .collect(Collectors.toList());

        consultantsJoinedBeforeGivenMonth
                .forEach(consultantDTO -> calculateAssignDays(givenDate, totalAssignedDays, maxAssignedDays, firstDayOfGivenMonth, consultantDTO.getContracts()));

        if (totalAssignedDays.get() == 0) {
            return 0;
        }

        return (double) totalAssignedDays.get() / maxAssignedDays.get() * 100;
    }

    private boolean areDatesOnSameMonthAndYear(LocalDate firstDate, LocalDate secondDate) {
        return firstDate.getMonthValue() == secondDate.getMonthValue() && firstDate.getYear() == secondDate.getYear();
    }

    private int getMonthMaxDays(LocalDate givenDate) {
        YearMonth yearMonth = YearMonth.of(givenDate.getYear(), givenDate.getMonth());
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

    double calculateAidUtilPercentOfGivenMonth(LocalDate givenDate) {
        Set<ConsultantDTO> consultants = consultantService.getActiveConsultants();
        int monthMaxDays = getMonthMaxDays(givenDate.minusMonths(1));
        AtomicInteger totalAssignedDays = new AtomicInteger();
        AtomicInteger maxAssignedDays = new AtomicInteger();

        LocalDate firstDayOfGivenMonth = LocalDate.of(givenDate.getYear(), givenDate.getMonthValue(), 1);

        List<ConsultantDTO> consultantsJoinedBeforeGivenMonth = consultants.stream()
                .filter(consultantDTO -> consultantDTO.getDateJoined().isBefore(firstDayOfGivenMonth))
                .collect(Collectors.toList());

        consultantsJoinedBeforeGivenMonth.forEach(consultantDTO -> {
            boolean joinedOnCalculatedMonth = areDatesOnSameMonthAndYear(consultantDTO.getDateJoined(), firstDayOfGivenMonth.minusMonths(1));
            boolean isPartiallyAided = areDatesOnSameMonthAndYear(consultantDTO.getDateJoined(), firstDayOfGivenMonth.minusMonths(4));
            boolean isFullyAided = !isPartiallyAided && consultantDTO.getDateJoined().isAfter(firstDayOfGivenMonth.minusMonths(4));
            int consultantAidedDays;

            if (joinedOnCalculatedMonth) {
                //adds only the dates that consultant was in the company
                consultantAidedDays = monthMaxDays - consultantDTO.getDateJoined().getDayOfMonth() + 1;
                totalAssignedDays.addAndGet(consultantAidedDays);
                maxAssignedDays.addAndGet(consultantAidedDays);
            } else if (isFullyAided) {
                totalAssignedDays.addAndGet(monthMaxDays);
                maxAssignedDays.addAndGet(monthMaxDays);
            } else if (isPartiallyAided) {
                Optional<Contract> consultantLatestContract = consultantDTO.getContracts().stream()
                        .filter(contract -> contract.getStartedDate().isBefore(firstDayOfGivenMonth))
                        .max(Comparator.comparing(Contract::getStartedDate));
                if (consultantLatestContract.isPresent() && !consultantLatestContract.get().getClientName().equals(ContractService.OFFICE_NAME)) {
                    if (consultantLatestContract.get().getStartedDate().getMonthValue() != givenDate.minusMonths(1).getMonthValue()) {
                        //consultant was assigned before the calculated month
                        totalAssignedDays.addAndGet(monthMaxDays);
                    } else {
                        int daysWorked = monthMaxDays - consultantLatestContract.get().getStartedDate().getDayOfMonth() + 1;
                        int aidedDays = consultantDTO.getDateJoined().getDayOfMonth();
                        totalAssignedDays.addAndGet(Math.min(daysWorked+aidedDays, monthMaxDays));
                    }
                } else {
                    //if consultants has still office contract then it adds only the aided days
                    totalAssignedDays.addAndGet(Math.min(consultantDTO.getDateJoined().getDayOfMonth(), monthMaxDays));
                }
                maxAssignedDays.addAndGet(monthMaxDays);
            } else {
                calculateAssignDays(givenDate, totalAssignedDays, maxAssignedDays, firstDayOfGivenMonth, consultantDTO.getContracts());
            }
        });

        if (totalAssignedDays.get() == 0) {
            return 0;
        }

        return (double) totalAssignedDays.get() / maxAssignedDays.get() * 100;
    }

    private void calculateAssignDays(LocalDate givenDate, AtomicInteger totalAssignedDays, AtomicInteger maxAssignedDays, LocalDate firstDayOfGivenMonth, List<Contract> contracts) {
        Optional<Contract> previousMonthsContractEndedOnGivenMonth = contracts.stream()
                .filter(c -> (!areDatesOnSameMonthAndYear(givenDate.minusMonths(1), c.getStartedDate())
                        && c.getEndDate() != null && areDatesOnSameMonthAndYear(givenDate.minusMonths(1), c.getEndDate())))
                .findFirst();

        List<Contract> contractsStartedOnGivenMonth = contracts.stream()
                .filter(c -> areDatesOnSameMonthAndYear(givenDate.minusMonths(1), c.getStartedDate()))
                .collect(Collectors.toList());

        int monthMaxDays = getMonthMaxDays(givenDate.minusMonths(1));

        if (!contractsStartedOnGivenMonth.isEmpty() || previousMonthsContractEndedOnGivenMonth.isPresent()) {
            contractsStartedOnGivenMonth.forEach(contract -> {
                int consultantAssignedDays;

                if (contract.getEndDate() != null && areDatesOnSameMonthAndYear(contract.getEndDate(), givenDate.minusMonths(1))) {
                    consultantAssignedDays = contract.getEndDate().getDayOfMonth() - contract.getStartedDate().getDayOfMonth() + 1;
                } else {
                    consultantAssignedDays = monthMaxDays - contract.getStartedDate().getDayOfMonth() + 1;
                }
                if (!contract.getClientName().equals(ContractService.OFFICE_NAME)) {
                    totalAssignedDays.addAndGet(consultantAssignedDays);
                }
                maxAssignedDays.addAndGet(consultantAssignedDays);
            });
            if (previousMonthsContractEndedOnGivenMonth.isPresent()) {
                Contract finishedContract = previousMonthsContractEndedOnGivenMonth.get();
                if (!finishedContract.getClientName().equals(ContractService.OFFICE_NAME)) {
                    totalAssignedDays.addAndGet(finishedContract.getEndDate().getDayOfMonth());
                }
                maxAssignedDays.addAndGet(finishedContract.getEndDate().getDayOfMonth());
            }
        } else {
            //contracts assigned before current month
            Optional<Contract> consultantLatestContractOnGivenDate = contracts.stream()
                    .filter(contract -> contract.getStartedDate().isBefore(firstDayOfGivenMonth))
                    .max(Comparator.comparing(Contract::getStartedDate));
            if (consultantLatestContractOnGivenDate.isPresent()) {
                if (!consultantLatestContractOnGivenDate.get().getClientName().equals(ContractService.OFFICE_NAME)) {
                    totalAssignedDays.addAndGet(monthMaxDays);
                }
                maxAssignedDays.addAndGet(monthMaxDays);
            }
        }
    }
}
