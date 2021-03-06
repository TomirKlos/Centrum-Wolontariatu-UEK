package pl.krakow.uek.centrumWolontariatu.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular.EmailCouldNotBeSentException;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class MailService {
    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final MessageSource messageSource;
    private final RetryTemplate retryTemplate;


    public MailService(JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine, MessageSource messageSource, RetryTemplate retryTemplate) {
        this.javaMailSender = javaMailSender;
        this.springTemplateEngine = springTemplateEngine;
        this.messageSource = messageSource;
        this.retryTemplate = retryTemplate;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom("adrian@dupa.pl");
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
                throw new EmailCouldNotBeSentException();
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
                throw new EmailCouldNotBeSentException();
            }
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        Locale locale = Locale.ENGLISH;
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("baseUrl", "http://127.0.0.1:8080");
        String content = springTemplateEngine.process(templateName, context);
//        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), "dupa", content, false, true);

    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        retryTemplate.execute(args0 -> {
            sendEmailFromTemplate(user, "activationEmail", "email.activation.title");
            return null;
        });
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        retryTemplate.execute(args0 -> {
            sendEmailFromTemplate(user, "creationEmail", "email.activation.title");
            return null;
        });

    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        retryTemplate.execute(args0 -> {
            sendEmailFromTemplate(user, "passwordResetEmail", "email.reset.title");
            return null;
        });

    }
}

