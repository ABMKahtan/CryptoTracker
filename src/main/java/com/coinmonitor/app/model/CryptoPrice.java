package com.coinmonitor.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Table(name = "crypto_price")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CryptoPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "coin")
    private String coin;

    @Column(name = "price")
    private Integer price;

    @Column(name = "date")
    private Instant date;

    @Column(name = "currency")
    private String currency;
}
