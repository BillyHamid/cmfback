package com.adouk.finacombackend.Application.services.Impl;


import com.adouk.finacombackend.domain.aggregates.Otp;
import com.adouk.finacombackend.domain.aggregates.StatusDossierClient;
import com.adouk.finacombackend.infrastructure.repositories.ClientRepository;
import com.adouk.finacombackend.infrastructure.repositories.DossierClientRepository;
import com.adouk.finacombackend.infrastructure.repositories.OtpRepository;
import com.adouk.finacombackend.infrastructure.rest.dto.OtpVerificationRequest;
import com.adouk.finacombackend.infrastructure.rest.dto.SmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DossierClientRepository dossierClientRepository;

    @Autowired
    private SmsService smsService;

    public SmsResponse sendOtp(String phone) {
        if (clientRepository.findByTelephone1(phone).isPresent()) return new SmsResponse(false, "Ce numéro est déjà associé à un compte", null, 0, null, null);
        if (dossierClientRepository.findByTelephone1AndStatutDossier(phone, StatusDossierClient.PENDING).isPresent()) return new SmsResponse(false, "Ce numéro est déjà associé à un dossier en attente", null, 0, null, null);
        String otpCode = String.valueOf(new Random().nextInt(90000) + 10000);
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);
        Otp otp = new Otp();
        otp.setPhone(phone);
        otp.setCode(otpCode);
        otp.setExpiration(expiration);
        otp.setVerified(false);
        otpRepository.save(otp);
        return smsService.sendSmsToPhone(phone, "Votre code OTP est : " + otpCode);
    }

    public String verifyOtp(OtpVerificationRequest request) {
        Otp otp = otpRepository.findTopByPhoneOrderByExpirationDesc(request.getPhone())
                .orElseThrow(() -> new RuntimeException("OTP non trouvé"));

        if (otp.isVerified()) return "Déjà vérifié";
        if (otp.getExpiration().isBefore(LocalDateTime.now())) return "OTP expiré";
        if (!otp.getCode().equals(request.getCode())) return "OTP incorrect";

        otp.setVerified(true);
        otpRepository.save(otp);
        return "OTP vérifié avec succès";
    }
}
