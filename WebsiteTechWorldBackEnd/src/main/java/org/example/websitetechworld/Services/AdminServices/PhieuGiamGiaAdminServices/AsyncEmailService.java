package org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices;

import org.example.websitetechworld.Services.LoginServices.MailService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncEmailService {

    private final MailService mailService;

    public AsyncEmailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Async("emailExecutor")
    public void sendEmailAsync (String to, String subject, String htmlBody) {
        mailService.sendMail(to, subject, htmlBody);
    }
}
