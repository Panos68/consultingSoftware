package com.consultant.model.services.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AbstractService {
    public UserDetails getUserInfo() {
        Authentication curAuth = SecurityContextHolder.getContext().getAuthentication();
        if (null == curAuth)
            return null;

        return (UserDetails) curAuth.getPrincipal();
    }
}
