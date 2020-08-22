package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.repositories.HeistRepository;
import org.springframework.stereotype.Service;

@Service
public class HeistServiceImpl implements HeistService {

    private final HeistRepository heistRepository;

    public HeistServiceImpl(HeistRepository heistRepository) {
        this.heistRepository = heistRepository;
    }
}
