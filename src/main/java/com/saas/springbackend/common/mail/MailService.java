package com.saas.springbackend.common.mail;

public interface MailService {

    void sendEmail(String to, String subject, String body);

}