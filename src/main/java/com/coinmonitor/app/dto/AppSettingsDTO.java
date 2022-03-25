package com.coinmonitor.app.dto;


import com.coinmonitor.app.model.Settings;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AppSettingsDTO {
    private String crypto;
    private String currency;
    private int maxValue;
    private int minValue;
    private String emailToNotify;

    public AppSettingsDTO(Settings settings){
        if(settings != null){
            currency = settings.getCurrency().toUpperCase();
            emailToNotify = settings.getEmailToNotify();
            maxValue = settings.getMaxValue();
            minValue = settings.getMinValue();
            crypto = settings.getCoinId().toUpperCase();
        }
    }
}
