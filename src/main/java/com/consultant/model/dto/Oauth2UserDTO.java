package com.consultant.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class Oauth2UserDTO {

    private final Map<String, Object> attributes;

    public String getEmail() {
        return (String) attributes.get("email");
    }

}
