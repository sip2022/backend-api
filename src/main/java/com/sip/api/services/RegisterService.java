package com.sip.api.services;

import com.sip.api.domains.user.User;
import com.sip.api.dtos.user.UserCreationDto;

public interface RegisterService {
    User register(UserCreationDto userCreationDto);

    void sendActivationMail(User user);

    void confirmEmail(String token);

    void resendActivationEmail(String userId);
}
