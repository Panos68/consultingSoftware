package com.consultant.model.dto;

import com.consultant.model.enums.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnologyDTO {
    private Long id;

    private String name;

    private Type type;
}
