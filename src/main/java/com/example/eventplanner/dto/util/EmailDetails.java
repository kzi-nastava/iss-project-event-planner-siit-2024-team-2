package com.example.eventplanner.dto.util;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class EmailDetails {
    private String sender = "Event Planner";
    private final String recipient;
    private final String subject;
    private final String body;
    private boolean html = false;
    private String[] attachmentPaths = null;
    private byte[][] attachmentsBinary = null;
    private String[] attachmentNames = null;

    public EmailDetails withAttachments(String[] attachmentPaths) {
        this.attachmentPaths = attachmentPaths;
        return this;
    }

    public EmailDetails withAttachments(byte[][] attachmentsBinary, String[] attachmentNames) {
        this.attachmentsBinary = attachmentsBinary;
        this.attachmentNames = attachmentNames;
        return this;
    }

    public EmailDetails withAttachment(String attachmentPath) {
        this.attachmentPaths = new String[]{attachmentPath};
        return this;
    }

    public EmailDetails withAttachment(byte[] attachmentBinary, String attachmentName) {
        this.attachmentsBinary = new byte[][]{attachmentBinary};
        this.attachmentNames = new String[]{attachmentName};
        return this;
    }

    public EmailDetails withHtml(boolean html) {
        this.html = html;
        return this;
    }

    public EmailDetails withSender(String sender) {
        this.sender = sender;
        return this;
    }
}
