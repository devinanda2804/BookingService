package com.example.bookingService.feign;

import com.example.bookingService.feign.ShowTimeClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ShowTimeClientTest {

    @MockBean
    private ShowTimeClient showTimeClient;

    @Test
    public void checkIfShowTimeExists() {
        Integer showtimeId = 101;
        Boolean exists = true;

        when(showTimeClient.checkIfShowTimeExists(showtimeId)).thenReturn(ResponseEntity.ok(exists));


        ResponseEntity<Boolean> response = showTimeClient.checkIfShowTimeExists(showtimeId);
        assertEquals(ResponseEntity.ok(exists), response);
        assertEquals(exists, response.getBody());
    }
}
