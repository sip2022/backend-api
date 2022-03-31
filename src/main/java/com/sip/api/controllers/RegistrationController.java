package com.sip.api.controllers;

import com.sip.api.dtos.user.UserCreationDto;
import com.sip.api.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody @Validated UserCreationDto userCreationDto) {
        return registrationService.register(userCreationDto);
    }
}
