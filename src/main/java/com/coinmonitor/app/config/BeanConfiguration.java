package com.coinmonitor.app.config;

import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    CoinGeckoApiClient coinGeckoApiClient() {
        return new CoinGeckoApiClientImpl();
    }

    @Bean
    PriceTrackerConfigProps priceTrackerConfigProps() {
        return new PriceTrackerConfigProps();
    }
}
