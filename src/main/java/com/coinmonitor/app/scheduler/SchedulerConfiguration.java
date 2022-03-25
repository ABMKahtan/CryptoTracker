package com.coinmonitor.app.scheduler;

import com.coinmonitor.app.config.PriceTrackerConfigProps;
import com.coinmonitor.app.model.Settings;
import com.coinmonitor.app.service.PriceTrackerService;
import com.coinmonitor.app.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.IntervalTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfiguration implements SchedulingConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerConfiguration.class);

    @Value("${scheduler.frequency}")
    private Integer schedulerFrequency;

    private final PriceTrackerConfigProps priceTrackerConfigProps;
    private final PriceTrackerService priceTrackerService;
    private final SettingsService settingsService;

    private boolean firstRun = true;

    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(2);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());

        taskRegistrar.addFixedDelayTask(new IntervalTask(
                () -> {
                    if (firstRun) {
                        LOGGER.debug("First run, trying to fetch settings");
                        Optional<Settings> settingsOptional = settingsService.loadSettings();
                        if (settingsOptional.isEmpty()) {
                            LOGGER.debug("No existing settings found, saving default settings");
                            settingsService.save(
                                    priceTrackerConfigProps.getCoinId(),
                                    priceTrackerConfigProps.getCurrency(),
                                    priceTrackerConfigProps.getEmailToNotify(),
                                    priceTrackerConfigProps.getMinValue(),
                                    priceTrackerConfigProps.getMaxValue()
                            );
                        }
                    }
                    try {
                        priceTrackerService.monitor();
                    } catch (Exception e) {
                        throw new RuntimeException("Unexpected error with monitoring service", e);
                    } finally {
                        firstRun = false;
                    }
                },
                schedulerFrequency
        ));
    }
}