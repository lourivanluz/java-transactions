package br.com.lourivanrluz.tutorial.mailSend;

public class Email {
    private String to;
    private String subject;
    private String body;

    public Email() {
    }

    public Email(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public Email(String emailAsString) {
        String[] parts = emailAsString.split(";");
        if (parts.length == 3) {
            this.to = parts[0];
            this.subject = parts[1];
            this.body = parts[2];
        } else {
            throw new IllegalArgumentException("Invalid email string format: " + emailAsString);
        }
    }

    @Override
    public String toString() {
        return to + ";" + subject + ";" + body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}