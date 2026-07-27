package com.saas.springbackend.security.password.service;

import com.saas.springbackend.security.password.dto.ForgotPasswordRequestDto;
import com.saas.springbackend.security.password.dto.ResetPasswordRequestDto;

public interface  PasswordResetService {
    void forgotPassword(ForgotPasswordRequestDto request);

    boolean validateResetToken(String token);

    void resetPassword(ResetPasswordRequestDto request);
}