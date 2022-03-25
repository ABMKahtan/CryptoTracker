package com.coinmonitor.app.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@ToString
public class CoinPriceResponse {
    private Map<String, Object> supplementalFields = new HashMap<>();

    public Map<String, Object> getSupplementalFields() {
        return supplementalFields;
    }

    @JsonAnySetter
    public void addSupplementalField(String property, Object value) {
        this.supplementalFields.put(property, value);
    }

}
