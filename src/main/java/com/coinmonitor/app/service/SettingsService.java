package com.coinmonitor.app.service;

import com.coinmonitor.app.model.Settings;
import com.coinmonitor.app.repository.SettingsRepository;
import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.domain.Coins.CoinList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettingsService {

    private final SettingsRepository settingsRepository;
    private final CoinGeckoApiClient coinGeckoApiClient;

    public Optional<Settings> loadSettings() {
        return settingsRepository.findAll().stream().findFirst();
    }

    public void save(Settings settings) {
        settingsRepository.save(settings);
    }

    public void save(String coinId, String currency, String email, int minValue, int maxValue) {

        Optional<CoinList> coinItemOpt = coinGeckoApiClient.getCoinList().stream().filter(coinItem -> coinItem.getId().equalsIgnoreCase(coinId)).findFirst();
        boolean supportsCurrency = coinGeckoApiClient.getSupportedVsCurrencies().contains(currency.toLowerCase());

        Settings settings = Settings.builder()
                .minValue(minValue)
                .maxValue(maxValue)
                .emailToNotify(email)
                .build();

        if (coinItemOpt.isPresent()) {
            settings.setCoinId(coinId.toUpperCase());
        }

        if(supportsCurrency){
            settings.setCurrency(currency);
        }

        loadSettings().ifPresentOrElse(
                existingSettings -> {
                    existingSettings.setCoinId(settings.getCoinId());
                    existingSettings.setCurrency(settings.getCurrency());
                    existingSettings.setEmailToNotify(settings.getEmailToNotify());
                    existingSettings.setMaxValue(settings.getMaxValue());
                    existingSettings.setMinValue(settings.getMinValue());

                    settingsRepository.save(existingSettings);
                },
                () -> {
                    settingsRepository.save(settings);
                }
        );


    }
}
