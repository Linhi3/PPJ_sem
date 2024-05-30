package org.linhart.ppj.sem.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.linhart.ppj.sem.entities.*;
import org.linhart.ppj.sem.handlers.MeteorDataHnadler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final MeteorDataHnadler meteorDataHnadler;

    public DataController(CityRepository cityRepository, StateRepository stateRepository, MeteorDataHnadler meteorDataHnadler) {
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.meteorDataHnadler = meteorDataHnadler;
    }

    @GetMapping("/data")
    public String getCities(@RequestParam String stateIso,@RequestParam String cityName) throws JsonProcessingException {
        State state = stateRepository.findStateByIsoCodeEquals(stateIso);
        City city = cityRepository.findCityByStateIsoEqualsAndNameEquals(state,cityName);
        meteorDataHnadler.DownloadNewData(city);
        return meteorDataHnadler.CalculateMeteorData(city);
    }
}
