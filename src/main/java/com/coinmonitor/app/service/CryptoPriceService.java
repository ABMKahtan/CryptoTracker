package com.coinmonitor.app.service;

import com.coinmonitor.app.model.CryptoPrice;
import com.coinmonitor.app.repository.CryptoPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class CryptoPriceService {

    private final CryptoPriceRepository cryptoPriceRepository;

    public void save(String coinId, String currencyValue, Integer currentPrice) {
        CryptoPrice cryptoPrice = CryptoPrice.builder()
                .coin(coinId)
                .currency(currencyValue)
                .price(currentPrice)
                .date(Instant.now())
                .build();
        cryptoPriceRepository.save(cryptoPrice);
    }

}
