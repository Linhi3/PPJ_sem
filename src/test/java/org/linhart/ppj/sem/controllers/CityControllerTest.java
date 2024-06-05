package org.linhart.ppj.sem.controllers;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void addCity() throws Exception {
        this.mockMvc.perform(put("/states")
                .param("stateName","cesko")
                .param("stateIso","CZ"));
        this.mockMvc.perform(put("/cities")
                        .param("stateIso","CZ")
                        .param("cityName","Liberec"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("Pridano mesto Liberec do statu CZ"));
    }

    @Test
    @Order(2)
    void addCityFail() throws Exception {
        this.mockMvc.perform(put("/cities")
                        .param("stateIso","USA")
                        .param("cityName","New York"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("Stat nenalezen"));
    }

    @Test
    @Order(3)
    void getCities() throws Exception {
        this.mockMvc.perform(get("/cities")
                        .param("stateIso","CZ"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("Dostupna mesta v CZ:\nLiberec\n"));
    }

    @Test
    @Order(4)
    void deleteCityFail() throws Exception {
        this.mockMvc.perform(delete("/cities")
                        .param("stateIso","CZ")
                        .param("cityName","AAA"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("Mesto se nepodarilo odstranit"));
    }

    @Test
    @Order(5)
    void deleteCity() throws Exception {
        this.mockMvc.perform(delete("/cities")
                        .param("stateIso","CZ")
                        .param("cityName","Liberec"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("Odstraneno mesto Liberec ze statu CZ"));
        this.mockMvc.perform(delete("/states")
                .param("stateIso","CZ"));
    }


}