package com.consultant.model.dto;

import com.consultant.model.entities.Vacation;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;

    private String username;

    private String password;

    private List<String> roles = new ArrayList<>();

    private Set<Vacation> vacations = new HashSet<>();

    private Long consultantId;
}
