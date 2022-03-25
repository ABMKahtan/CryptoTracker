package com.coinmonitor.app.service;

import com.coinmonitor.app.config.PriceTrackerConfigProps;
import com.coinmonitor.app.model.NotificationType;
import com.coinmonitor.app.model.Settings;
import com.litesoftwares.coingecko.CoinGeckoApiClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PriceTrackerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceTrackerService.class);
    private static boolean lowEmailSent = false;
    private static boolean highEmailSent = false;

    private final CoinGeckoApiClient coinGeckoApiClient;
    private final PriceTrackerConfigProps priceTrackerConfigProps;
    private final EmailService emailService;
    private final SettingsService settingsService;
    private final CryptoPriceService cryptoPriceService;

    public void monitor() {

        String coinId = priceTrackerConfigProps.getCoinId();
        String currency = priceTrackerConfigProps.getCurrency();
        int minValue = priceTrackerConfigProps.getMinValue();
        int maxValue = priceTrackerConfigProps.getMaxValue();
        String emailToNotify = priceTrackerConfigProps.getEmailToNotify();

        Optional<Settings> settingsOpt = settingsService.loadSettings();

        if (settingsOpt.isPresent()) {
            Settings settings = settingsOpt.get();
            LOGGER.info("Using settings found on database {}", settings);
            coinId = settings.getCoinId().toLowerCase();
            currency = settings.getCurrency().toLowerCase();
            minValue = settings.getMinValue();
            maxValue = settings.getMaxValue();
            emailToNotify = settings.getEmailToNotify();
        }

        Map<String, Map<String, Double>> apiResponse = coinGeckoApiClient.getPrice(coinId, currency);

        if (apiResponse.get(coinId) != null && apiResponse.get(coinId).get(currency) != null) {
            Integer currentPrice = apiResponse.get(coinId).get(currency).intValue();
            LOGGER.info("Current price for {} is {} {}", coinId, currentPrice, currency.toUpperCase());

            cryptoPriceService.save(coinId, currency.toUpperCase(), currentPrice);

            if (currentPrice < minValue) {
                LOGGER.info("Current price {} is lower than minimum price {}", currentPrice, minValue);
                if (!highEmailSent) {
                    emailService.sendEmailNotification(
                            currentPrice,
                            coinId,
                            currency,
                            emailToNotify,
                            NotificationType.LOWER_THAN_MINIMUM
                    );
                }
                highEmailSent = true;
                lowEmailSent = false;
            }

            if (currentPrice > maxValue) {
                LOGGER.info("Current price {} is higher than maximum price {}", currentPrice, maxValue);
                if (!lowEmailSent) {
                    emailService.sendEmailNotification(
                            currentPrice,
                            coinId,
                            currency,
                            emailToNotify,
                            NotificationType.HIGHER_THAN_MAX
                    );
                    lowEmailSent = true;
                    highEmailSent = false;
                }
            }
        }
    }
}
