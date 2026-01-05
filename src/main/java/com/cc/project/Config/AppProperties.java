package com.cc.project.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String allowedOrigins;
    private String cloudinaryCloudName;
    private String cloudinaryApiKey;
    private String cloudinaryApiSecret;

    public String getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public String getCloudinaryCloudName() {
        return cloudinaryCloudName;
    }

    public void setCloudinaryCloudName(String cloudinaryCloudName) {
        this.cloudinaryCloudName = cloudinaryCloudName;
    }

    public String getCloudinaryApiKey() {
        return cloudinaryApiKey;
    }

    public void setCloudinaryApiKey(String cloudinaryApiKey) {
        this.cloudinaryApiKey = cloudinaryApiKey;
    }

    public String getCloudinaryApiSecret() {
        return cloudinaryApiSecret;
    }

    public void setCloudinaryApiSecret(String cloudinaryApiSecret) {
        this.cloudinaryApiSecret = cloudinaryApiSecret;
    }
}
