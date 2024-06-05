package org.linhart.ppj.sem.controllers;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StateControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void addState() throws Exception {
    this.mockMvc.perform(put("/states")
            .param("stateName","cesko")
            .param("stateIso","CZ"))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(content().string("Pridan stat cesko [CZ]"));
   }
    @Test
    @Order(2)
    void showStates() throws Exception {
        this.mockMvc.perform(get("/states"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("Dostupne staty:\ncesko [CZ]\n"));
    }
    @Test
    @Order(3)
    void deleteStateFail() throws Exception {
        this.mockMvc.perform(delete("/states")
                        .param("stateIso","AA"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("Stat se nepodarilo odstranit"));
    }


    @Test
    @Order(4)
    void deleteState() throws Exception {
        this.mockMvc.perform(delete("/states")
                        .param("stateIso","CZ"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("Odstranen stat cesko [CZ]"));
    }

}