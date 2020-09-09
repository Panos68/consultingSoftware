package com.consultant.model.entities;

import java.util.Map;

public class Oauth2UserInfo {
    private Map<String, Object> attributes;

    public Oauth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public String getId() {
        return (String) attributes.get("sub");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

}
