package com.consultant.model.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@Data
@AllArgsConstructor
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;
    private final Collection<? extends GrantedAuthority> roles;
}
