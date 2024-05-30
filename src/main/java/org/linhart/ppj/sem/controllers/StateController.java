package org.linhart.ppj.sem.controllers;


import org.linhart.ppj.sem.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StateController {

    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final MeteorDataRepository meteorDataRepository;
    final private Logger logger = LoggerFactory.getLogger(CityController.class);

    public StateController(StateRepository stateRepository, CityRepository cityRepository, MeteorDataRepository meteorDataRepository) {
        this.stateRepository = stateRepository;
        this.cityRepository = cityRepository;
        this.meteorDataRepository = meteorDataRepository;
    }

    @GetMapping("/states")
    public String showStates(){
        List<State> states = (List<State>) stateRepository.findAll();
        StringBuilder response = new StringBuilder();
        response.append("Dostupne staty:\n");
        for (State state : states) {
            response.append(String.format("%s [%s]\n",state.getStateName(),state.getIsoCode()));
        }
        return response.toString();
    }
    @PutMapping( "/states")
    public String addState(@RequestParam String stateName,@RequestParam String stateIso){
        stateRepository.save(new State(stateIso,stateName));
        logger.info("Pridan stat {} [{}]", stateName, stateIso);
        return "Pridan stat "+stateName+" ["+stateIso+"]";
    }
    @DeleteMapping("/states")
    public String deleteState(@RequestParam String stateIso){
        State state = stateRepository.findStateByIsoCodeEquals(stateIso);
        if (state != null) {
            List<City> cities = cityRepository.findCitiesByStateIsoEquals(state);
            for (City city : cities) {
                List<MeteorData> data = meteorDataRepository.findMeteorDataByCityIDEquals(city);
                meteorDataRepository.deleteAll(data);
                cityRepository.delete(city);
                logger.info("Odstraneno mesto {} ze statu {}", city.getName(), state.getIsoCode());
                return "Odstraneno mesto " + city.getName() + " ze statu " + state.getIsoCode();
            }
            stateRepository.delete(state);
            logger.info("Odstranen stat {} [{}]", state.getStateName(), stateIso);
            return "Odstranen stat "+state.getStateName()+" ["+stateIso+"]";
        }else{
            logger.error("Stat se nepodarilo odstranit");
            return "Stat se nepodarilo odstranit";
        }
    }
}
