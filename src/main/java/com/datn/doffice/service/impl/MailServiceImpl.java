package com.datn.doffice.service.impl;

import com.datn.doffice.defines.MailConstants;
import com.datn.doffice.entity.MailEntity;
import com.datn.doffice.entity.UserEntity;
import com.datn.doffice.enums.MailTemplate;
import com.datn.doffice.exceptions.MailHandleException;
import com.datn.doffice.service.MailService;
import com.datn.doffice.utils.ThreadPoolUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private Configuration freemarkerConfig;

    private static final String MAIL_TEMPLATE_RELATIVE_PATH_FOLDER = "/templates/email/";

    @Value("${spring.mail.hotline}")
    private String sysHotLine;

    @Value("${spring.mail.system-email}")
    private String sysEmail;

    @Value("${spring.mail.domain}")
    private String sysDomain;

    @Value("${spring.mail.system-name}")
    private String sysName;

    @Value("${spring.mail.system-address}")
    private String sysAddress;

    @Value("${sys.app-name}")
    private String sysAppName;

    private Map<String, String> mailDataContent() {
        Map<String, String> mailDataContent = new HashMap<>();
        mailDataContent.put("sysPhone", sysHotLine);
        mailDataContent.put("sysMail", sysEmail);
        mailDataContent.put("sysName", sysName);
        mailDataContent.put("sysAddress", sysAddress);
        mailDataContent.put("sysDomain", sysDomain);
        mailDataContent.put("sysAppName", sysAppName);
        return mailDataContent;
    }

    @Override
    public void notifyForReceiver(UserEntity receiver) {
        MailEntity mail = new MailEntity();
        mail.setTemplate(MailTemplate.MAIL_TEST);
        mail.setMailTo(receiver.getEmail());
        Map<String, String> mailDataContent = mailDataContent();
        mailDataContent.put(MailConstants.MAIL_FULL_NAME_DATA, receiver.getFullName());
        mail.setMailDataContent(mailDataContent);
        sendMail(mail);
    }

    @Override
    public void sendMail(MailEntity mailEntity) {
        if (!isCheckValidBeforeSendMail(mailEntity)) {
            throw new MailHandleException();
        }
        String textHtmlContent;
        try {
            // Send mail
            textHtmlContent = buildContentSendMail(mailEntity.getMailDataContent(), mailEntity.getTemplate().getFileTemplateName());
            mailEntity.setTextContent(textHtmlContent);
            MimeMessage message = emailSender.createMimeMessage();
            buildMimeHelperNoAttachment(mailEntity, message);

            ThreadPoolUtil.quickService.submit(() -> {
                try {
                    // --->Send mail
                    emailSender.send(message);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        } catch (MailException e) {
            log.error(e.getMessage(), e);
        }
    }

    private static boolean isCheckValidBeforeSendMail(MailEntity mailEntity) {
        return !(mailEntity == null || mailEntity.getMailTo() == null || mailEntity.getMailTo().isEmpty());
    }

    private String buildContentSendMail(Map<String, String> model, String fileName) {
        try {
            Template temp = buildTemplate(fileName);

            Map<String, String> tmpModel = model;

            if (tmpModel == null) {
                tmpModel = new HashMap<>();
            }

            return FreeMarkerTemplateUtils.processTemplateIntoString(temp, tmpModel);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
            throw new MailHandleException(e.getMessage(), e);
        }
    }

    private Template buildTemplate(String name) {
        Template templateSendMail = null;
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), MAIL_TEMPLATE_RELATIVE_PATH_FOLDER);
            templateSendMail = freemarkerConfig.getTemplate(name, "utf-8");
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            throw new MailHandleException(e.getMessage(), e);
        }
        return templateSendMail;
    }

    private MimeMessageHelper buildMimeHelperNoAttachment(MailEntity mailEntity, MimeMessage message) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            if (emailSender instanceof JavaMailSenderImpl) {
                JavaMailSenderImpl emailSenderImp = (JavaMailSenderImpl) emailSender;
                helper.setFrom(emailSenderImp.getUsername());
            }

            helper.setTo(mailEntity.getMailTo());
            Locale locale = LocaleContextHolder.getLocale();
            ResourceBundle labels = ResourceBundle.getBundle("message.message", locale);
            String mailSubject = labels.getString(mailEntity.getTemplate().getMailSubject());
            helper.setSubject(mailSubject);

            helper.setText(mailEntity.getTextContent(), true);

            ClassPathResource logo = new ClassPathResource("/templates/images/meta-logo.png");
            helper.addInline("logo", logo);

            ClassPathResource iconWebsite = new ClassPathResource("/templates/images/icon-website.png");
            helper.addInline("iconWebsite", iconWebsite);

            ClassPathResource iconHotLine = new ClassPathResource("/templates/images/icon-hotline.png");
            helper.addInline("iconHotLine", iconHotLine);

            return helper;
        } catch (MessagingException e) {
            log.warn(e.getMessage(), e);
            throw new MailHandleException(e.getMessage(), e);
        }
    }
}
