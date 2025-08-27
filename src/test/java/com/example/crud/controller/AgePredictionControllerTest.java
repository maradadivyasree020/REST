package com.example.crud.controller;

import com.example.crud.model.AgePrediction;
import com.example.crud.service.AgePredictionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgePredictionController.class)
public class AgePredictionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgePredictionService agePredictionService;

    @Test
    public void testGetAgePrediction_success() throws Exception {
        String name = "Alice";
        AgePrediction prediction = new AgePrediction();
        prediction.setName("Alice");
        prediction.setAge(30);

        when(agePredictionService.predictAge(name)).thenReturn(prediction);

        mockMvc.perform(get("/age-prediction")
                .param("name", name)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.age").value(30));

        verify(agePredictionService, times(1)).predictAge(name);
    }

    @Test
    public void testGetAgePrediction_missingNameParam_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/age-prediction")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(agePredictionService, never()).predictAge(anyString());
    }

    // @Test
    // public void testGetAgePrediction_serviceThrows_shouldReturnServerError() throws Exception {
    //     String name = "Bob";
    //     when(agePredictionService.predictAge(name)).thenThrow(new RuntimeException("API Error"));

    //     mockMvc.perform(get("/age-prediction")
    //             .param("name", name)
    //             .accept(MediaType.APPLICATION_JSON))
    //             .andExpect(status().isInternalServerError());

    //     verify(agePredictionService, times(1)).predictAge(name);
    // }

}
