package com.di.raine.services;

import com.di.raine.services.UserService;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;


public class UserServiceTest {

    @Test
    public void testLogin() throws Exception {
        ResponseEntity responseEntity = new UserService().attemptLogin("dimitris", "123456");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


}
