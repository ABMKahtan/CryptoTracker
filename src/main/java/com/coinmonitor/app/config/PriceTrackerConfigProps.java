package com.coinmonitor.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "crypto")
@Getter
@Setter
public class PriceTrackerConfigProps {
    private String coinId;
    private String apiUrl;
    private String currency;
    private int maxValue;
    private int minValue;
    private String emailToNotify;
}
