package com.coinmonitor.app.repository;

import com.coinmonitor.app.model.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoPriceRepository extends JpaRepository<CryptoPrice, Integer> {
}
