package com.consultant.model.converters.consultants;

import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.entities.Consultant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ConsultantToDTO implements Converter<Consultant, ConsultantDTO> {

    @Override
    public ConsultantDTO convert(Consultant consultant) {
        ConsultantDTO consultantDTO = new ConsultantDTO();

        consultantDTO.setId(consultant.getId());
        consultantDTO.setStatus(consultant.getStatus());
        consultantDTO.setFirstName(consultant.getFirstName());
        consultantDTO.setLastName(consultant.getLastName());
        consultantDTO.setPrice(consultant.getPrice());
        consultantDTO.setListPrice(consultant.getListPrice());
        consultantDTO.setDiscount(consultant.getDiscount());
        consultantDTO.setContractStarted(consultant.getContractStarted());
        consultantDTO.setContractEnding(consultant.getContractEnding());
        consultantDTO.setUpdatedContractEnding(consultant.getUpdatedContractEnding());
        consultantDTO.setSigned(consultant.getSigned());
        consultantDTO.setOther(consultant.getOther());
        consultantDTO.setTeamName(consultant.getTeamName());
        consultantDTO.setCompanyName(consultant.getCompanyName());
        return consultantDTO;
    }
}
