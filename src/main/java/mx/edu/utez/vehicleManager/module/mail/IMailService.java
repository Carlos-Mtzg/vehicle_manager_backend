package mx.edu.utez.vehicleManager.module.mail;

public interface IMailService {

    void sendEmail(String toUser, String subject, String message);

}
