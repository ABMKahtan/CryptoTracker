package com.coinmonitor.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "settings")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "coin")
    private String coinId;

    @Column(name = "currency")
    private String currency;

    @Column(name = "max_value")
    private int maxValue;

    @Column(name = "min_value")
    private int minValue;

    @Column(name = "email_to_notify")
    private String emailToNotify;

}
