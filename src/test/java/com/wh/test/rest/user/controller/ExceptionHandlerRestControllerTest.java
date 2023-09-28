package com.wh.test.rest.user.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ExceptionHandlerRestControllerTest {

    @Autowired
    ExceptionHandlerRestController controller;


    @Test
    void handleNoSuchElementException() {
        NoSuchElementException mockException = mock(NoSuchElementException.class);
        when(mockException.getMessage()).thenReturn("Not found");
        ResponseEntity<String> response = controller.handleNoSuchElementException(mockException);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Not found");
    }

    @Test
    void handleIllegalArgumentException() {
        IllegalArgumentException mockException = mock(IllegalArgumentException.class);
        when(mockException.getMessage()).thenReturn("Bad request");
        ResponseEntity<String> response = controller.handleIllegalArgumentException(mockException);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Bad request");
    }


    @Test
    void handleException() {
        Exception mockException = mock(Exception.class);
        ResponseEntity<String> response = controller.handleException(mockException);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
