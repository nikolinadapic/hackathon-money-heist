package com.ag04.sbss.hackathon.app.services;

/**
 * Created by Vitomir M on 23.8.2020.
 */
public interface EmailService {

    void sendMessage(String to, String subject, String text);
}
