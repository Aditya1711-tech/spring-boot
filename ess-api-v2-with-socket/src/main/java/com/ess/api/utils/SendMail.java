package com.ess.api.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SendMail {

    private final String basePath = "/home/aditya/Documents/Spring-Boot/ess-api-v2/src/main/java/com/ess/api/utils/mailTemplates";
    private final String baseUrl = "http://localhost:5173";

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeMail(String fullName, String email, String password) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress("aditya@demomailtrap.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("Welcome to DRC System");

        String htmlTemplate = getTemplate(basePath+"/welcome.html");
        String htmlContent = htmlTemplate.replace("${name}", fullName);
        htmlContent = htmlContent.replace("${email}", email);
        htmlContent = htmlContent.replace("${password}", password);

        message.setContent(htmlContent, "text/html; charset=utf-8");
        mailSender.send(message);
    }

    public void sedNewMemberMail(String email, String assignByName, String projectName, Long projectId) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress("aditya@demomailtrap.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("You are added to project " + projectName);

        String htmlTemplate = getTemplate(basePath+"/addProject.html");
        String htmlContent = htmlTemplate.replace("${projectName}", projectName);
        htmlContent = htmlContent.replace("${assignByName}", assignByName);
        htmlContent = htmlContent.replace("${projectUrl}", baseUrl+"/listOfTasks/"+projectId);

        message.setContent(htmlContent, "text/html; charset=utf-8");
        mailSender.send(message);
    }

    public void sendApproveOrRejectedRequestMail(String email, String status, String doneBy, String note) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress("aditya@demomailtrap.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("Your leave request " + status.toLowerCase());

        String htmlTemplate = getTemplate(basePath+"/approveOrReject.html");
        String htmlContent = htmlTemplate.replace("${status}", status);
        htmlContent = htmlContent.replace("${doneBy}", doneBy);
        htmlContent = htmlContent.replace("${note}", note);
        htmlContent = htmlContent.replace("${pageUrl}", baseUrl+"/");

        message.setContent(htmlContent, "text/html; charset=utf-8");
        mailSender.send(message);
    }

    public void sendResetPasswordMail(String email, String employeeName) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress("aditya@demomailtrap.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("Update password");

        String htmlTemplate = getTemplate(basePath+"/updatePassword.html");
        String htmlContent = htmlTemplate.replace("${employeeName}", employeeName);
        htmlContent = htmlContent.replace("${pageUrl}", baseUrl+"/updatePassword");

        message.setContent(htmlContent, "text/html; charset=utf-8");
        mailSender.send(message);
    }

    private String getTemplate(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}