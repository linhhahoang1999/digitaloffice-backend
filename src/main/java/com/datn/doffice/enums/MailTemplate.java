package com.datn.doffice.enums;

public enum MailTemplate {

  MAIL_TEST("mail_test.html", "mail.001"),

  DEFAULT("", "");

  private String fileTemplateName;

  private String mailSubject;

  MailTemplate(String fileTemplateName, String mailSubject) {
    this.fileTemplateName = fileTemplateName;
    this.mailSubject = mailSubject;
  }

  public static MailTemplate fromName(String fileTemplateName) {
    for (MailTemplate type : MailTemplate.values()) {
      if (type.getFileTemplateName().equals(fileTemplateName)) {
        return type;
      }
    }
    return DEFAULT;
  }

  public String getFileTemplateName() {
    return fileTemplateName;
  }

  public String getMailSubject() {
    return mailSubject;
  }

}
