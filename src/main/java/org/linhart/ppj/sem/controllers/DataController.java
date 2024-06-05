package org.linhart.ppj.sem.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.linhart.ppj.sem.entities.*;
import org.linhart.ppj.sem.handlers.MeteorDataHnadler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final MeteorDataHnadler meteorDataHnadler;
    final private Logger logger = LoggerFactory.getLogger(CityController.class);

    public DataController(CityRepository cityRepository, StateRepository stateRepository, MeteorDataHnadler meteorDataHnadler) {
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.meteorDataHnadler = meteorDataHnadler;
    }

    @GetMapping("/data")
    public String getDataAboutCity(@RequestParam String stateIso,@RequestParam String cityName){
        State state = stateRepository.findStateByIsoCodeEquals(stateIso);
        if (state == null) {
            logger.error("Stat nenalezen");
            return "Stat nenalezen";
        }
        City city = cityRepository.findCityByStateIsoEqualsAndNameEquals(state,cityName);
        if (city == null) {
            logger.error("Mesto nenalezeno");
            return "Mesto nenalezeno";

        }
        if (!meteorDataHnadler.DownloadNewData(city)){
            logger.error("Chyba spojeni se serverem openweather");
            return "Chyba spojeni se serverem openweather";
        }
        return meteorDataHnadler.CalculateMeteorData(city);
    }
}
