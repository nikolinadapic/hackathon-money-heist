package com.ag04.sbss.hackathon.app.services;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public interface SchedulingService {
    ThreadPoolTaskScheduler getScheduler();
}
