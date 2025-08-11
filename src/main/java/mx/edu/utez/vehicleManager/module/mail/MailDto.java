package mx.edu.utez.vehicleManager.module.mail;

public class MailDto {

    private String toUser;
    private String subject;
    private String message;

    public String getToUser() {
        return toUser;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public MailDto() {
    }

    public MailDto(String toUser, String subject, String message) {
        this.toUser = toUser;
        this.subject = subject;
        this.message = message;
    }

}
