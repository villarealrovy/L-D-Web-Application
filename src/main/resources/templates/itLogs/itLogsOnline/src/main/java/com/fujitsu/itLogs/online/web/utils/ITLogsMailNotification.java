/**
 * Copyright (C) 2017 FUJITSU LIMITED All rights reserved.
 */
package com.fujitsu.itLogs.online.web.utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * @author r.monte@ph.fujitsu.com
 *
 * @version 1.0.0
 *
 */
@Service
public class ITLogsMailNotification {
	private static Logger logger = LoggerFactory.getLogger(ITLogsMailNotification.class);

	@Autowired
	protected final JavaMailSender javaMailSender;

	@Autowired
	ITLogsMailConfiguration iTLogsMailConfiguration;

	@Autowired
	ITLogsMailNotification(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void send(String username, String password, String subject, String msg, String emailApprover, String attachmentPathString)
			throws MailException, MessagingException {

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		String fromEmail = username + iTLogsMailConfiguration.getDomain();
		// will not get from application properties, change to dynamic data
		//String toEmail = iTLogsMailConfiguration.getTo();

		mailMessage.setTo(emailApprover);
		mailMessage.setFrom(fromEmail);
		mailMessage.setCc(fromEmail);
		mailMessage.setSubject(subject);
		mailMessage.setReplyTo(fromEmail);
		mailMessage.setText(msg);

		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			helper.setFrom(mailMessage.getFrom());
			helper.setTo(mailMessage.getTo());
			helper.setSubject(mailMessage.getSubject());
			helper.setText(mailMessage.getText());
			helper.setCc(mailMessage.getCc());
			helper.setReplyTo(mailMessage.getReplyTo());

			if (attachmentPathString != null && !attachmentPathString.isEmpty()) {
				FileSystemResource file = new FileSystemResource(attachmentPathString);
				helper.addAttachment(file.getFilename(), file);
			}

			logger.info("Mail To : " + emailApprover);
			logger.info("Mail From : " + fromEmail);
			logger.info("Mail Reply To : " + fromEmail);
			logger.info("Mail Subject : " + subject);
			logger.info("Mail Message : " + msg);
			iTLogsMailConfiguration.getMailSender().setUsername(username);
			iTLogsMailConfiguration.getMailSender().setPassword(password);

			javaMailSender.send(mimeMessage);

		} catch (MailException | MessagingException e) {
			logger.warn("Sending Mail FAILED!!", e);
			throw e;
		}

		logger.info("Sending Mail SUCCESSFUL");
	}

}
