package com.ag04.sbss.hackathon.app.services;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

@Service
public class SchedulingServiceImpl implements SchedulingService {

    private final ThreadPoolTaskScheduler scheduler;

    public SchedulingServiceImpl(ThreadPoolTaskScheduler scheduler) {
        this.scheduler = scheduler;

        this.scheduler.initialize();
    }

    @Override
    public ThreadPoolTaskScheduler getScheduler() {
        return scheduler;
    }
}