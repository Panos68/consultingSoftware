package com.consultant.model.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final OAuth2 oauth2 = new OAuth2();

    @Getter
    @Setter
    public static final class OAuth2 {

        private String authorizedRedirectUri;
        private String logoutRedirectUri;

    }
}