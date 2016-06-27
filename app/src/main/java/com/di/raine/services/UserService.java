package com.di.raine.services;



import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class UserService {
    private static final String baseUrl  = "http://cello.jamwide.com/";
    private static final String loginUrlPostfix = "webserv/api/login";

    public  ResponseEntity attemptLogin(String username, String password) {
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        ResponseEntity response = restTemplate
                .exchange(baseUrl + loginUrlPostfix , HttpMethod.POST,  new HttpEntity<Object>(requestHeaders), String.class);

        System.out.println(response.getBody().toString());
        return response;
    }

}
