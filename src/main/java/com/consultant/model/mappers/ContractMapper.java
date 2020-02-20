package com.consultant.model.mappers;

import com.consultant.model.dto.ContractDTO;
import com.consultant.model.entities.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContractMapper {
    ContractMapper INSTANCE = Mappers.getMapper(ContractMapper.class);

    Contract contractDTOToContract(ContractDTO contractDTO);
}
