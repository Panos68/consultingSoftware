package com.consultant.model.converters.consultants;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.entities.Consultant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DTOToConsultant implements Converter<ConsultantDTO, Consultant> {

    @Override
    public Consultant convert(ConsultantDTO consultantDTO) {
        Consultant consultant = new Consultant();

        consultant.setId(consultantDTO.getId());
        consultant.setStatus(consultantDTO.getStatus());
        consultant.setFirstName(consultantDTO.getFirstName());
        consultant.setLastName(consultantDTO.getLastName());
        consultant.setPrice(consultantDTO.getPrice());
        consultant.setListPrice(consultantDTO.getListPrice());
        consultant.setDiscount(consultantDTO.getDiscount());
        consultant.setContractStarted(consultantDTO.getContractStarted());
        consultant.setContractEnding(consultantDTO.getContractEnding());
        consultant.setUpdatedContractEnding(consultantDTO.getUpdatedContractEnding());
        consultant.setSigned(consultantDTO.getSigned());
        consultant.setOther(consultantDTO.getOther());

        return consultant;
    }
}
