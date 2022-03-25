package com.coinmonitor.app.repository;

import com.coinmonitor.app.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Integer> {
}
