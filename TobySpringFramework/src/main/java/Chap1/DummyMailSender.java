package Chap1;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements org.springframework.mail.MailSender{
	public void send(SimpleMailMessage mailMessage) throws MailException {

	}

	public void send(SimpleMailMessage[] mailMessage) throws MailException {

	}
}
