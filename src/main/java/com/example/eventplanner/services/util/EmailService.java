package com.example.eventplanner.services.util;

import com.example.eventplanner.dto.util.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}") private String senderMail;

    /**
     * Sends a simple email with the given details. Not capable of sending attachments, HTML content or sender name.
     * @param emailDetails The details of the email
     */
    @Async
    public void sendSimpleMessage(EmailDetails emailDetails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderMail);
        message.setTo(emailDetails.getRecipient());
        message.setSubject(emailDetails.getSubject());
        message.setText(emailDetails.getBody());
        emailSender.send(message);
    }

    /**
     * Sends a MIME email with the given details. Email can contain attachments and HTML content.
     * Attachments can either be files from the file system, or binary file data.
     * Fails silently.
     *
     * @param emailDetails The details of the email
     */
    @Async
    public void sendMimeMessage(EmailDetails emailDetails) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(senderMail, emailDetails.getSender());
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getBody(), emailDetails.isHtml());
            mimeMessageHelper.setSubject(emailDetails.getSubject());

            if (emailDetails.getAttachmentPaths() != null) {
                for (String path : emailDetails.getAttachmentPaths()) {
                    FileSystemResource file = new FileSystemResource(new File(path));
                    if (file.exists() && file.isReadable() && file.getFilename() != null)
                        mimeMessageHelper.addAttachment(file.getFilename(), file);
                }
            } else if (emailDetails.getAttachmentsBinary() != null && emailDetails.getAttachmentNames() != null
                    && emailDetails.getAttachmentsBinary().length == emailDetails.getAttachmentNames().length) {
                for (int i = 0; i < emailDetails.getAttachmentsBinary().length; i++) {
                    ByteArrayDataSource attachment = new ByteArrayDataSource(
                            emailDetails.getAttachmentsBinary()[i], "application/octet-stream");
                    mimeMessageHelper.addAttachment(emailDetails.getAttachmentNames()[i], attachment);
                }
            }
            emailSender.send(mimeMessage);
        }
        catch (MessagingException | UnsupportedEncodingException e) {
            return; // Simplifies async execution, can be changed to throw exception
        }
    }
}
