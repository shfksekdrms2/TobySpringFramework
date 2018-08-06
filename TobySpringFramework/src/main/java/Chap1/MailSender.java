package Chap1;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;


public interface MailSender {
	void send(SimpleMailMessage simpleMessage) throws MailException;
	void send(SimpleMailMessage[] simpleMessage) throws MailException;
}
