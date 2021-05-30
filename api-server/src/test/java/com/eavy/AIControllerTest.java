package com.eavy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AIControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    AIRepository repository;

    @DisplayName("Get model info")
    @Test
    void getModelInfo() throws Exception {

        AI ai = new AI("MLPClassifier", "logistic", "adaptive", 50, 0.1, "sgd");
        repository.save(ai);

        mockMvc.perform(get("/model/" + ai.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(ai.getName())))
                .andExpect(jsonPath("$.activation", is(ai.getActivation())))
                .andExpect(jsonPath("$.learningRate", is(ai.getLearningRate())))
                .andExpect(jsonPath("$.hiddenLayerSizes", is(ai.getHiddenLayerSizes())))
                .andExpect(jsonPath("$.momentum", is(ai.getMomentum())))
                .andExpect(jsonPath("$.solver", is(ai.getSolver())))
                .andDo(print());
    }

    @DisplayName("Get model info - fail")
    @Test
    void getModelInfoFail() throws Exception {

        mockMvc.perform(get("/model/9999"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}