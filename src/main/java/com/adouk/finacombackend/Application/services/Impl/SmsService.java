package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.infrastructure.rest.dto.SmsRequest;
import com.adouk.finacombackend.infrastructure.rest.dto.SmsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SmsService {

    @Value("${app.aqilas.token}")
    private String AUTH_TOKEN;

    @Value("${app.aqilas.url}")
    private String API_URL;

    @Value("${app.aqilas.sender}")
    private String SENDER;


    public SmsResponse sendSms(SmsRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-AUTH-TOKEN", AUTH_TOKEN);

        HttpEntity<SmsRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<SmsResponse> response = restTemplate.exchange(
                API_URL + "/sms",
                HttpMethod.POST,
                entity,
                SmsResponse.class
        );

        return response.getBody();
    }

    public SmsResponse sendSmsToPhone(String phone, String message) {
        SmsRequest sms = new SmsRequest();
        sms.setFrom(SENDER);
        sms.setText(message);
        sms.setTo(List.of("+226".concat(phone)));
        return sendSms(sms); // méthode déjà implémentée
    }

}

