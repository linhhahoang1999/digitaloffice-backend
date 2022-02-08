package com.datn.doffice.service;

import com.datn.doffice.entity.MailEntity;
import com.datn.doffice.entity.UserEntity;

public interface MailService {

  void notifyForReceiver(UserEntity receiver);

  void sendMail(MailEntity mailEntity);

}
