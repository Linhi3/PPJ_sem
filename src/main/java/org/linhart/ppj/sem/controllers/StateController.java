package org.linhart.ppj.sem.controllers;


import org.linhart.ppj.sem.entities.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StateController {

    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final MeteorDataRepository meteorDataRepository;

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
        return "Added state "+stateName+" ["+stateIso+"]";
    }
    @DeleteMapping("/states")
    public String deleteState(@RequestParam String stateIso){
        State state = stateRepository.findStateByIsoCodeEquals(stateIso);
        List<City> cities = cityRepository.findCitiesByStateIsoEquals(state);
        for (City city : cities) {
            List<MeteorData> data = meteorDataRepository.findMeteorDataByCityIDEquals(city);
            meteorDataRepository.deleteAll(data);
            cityRepository.delete(city);
        }
        stateRepository.delete(state);
        return "Deleted state "+state.getStateName()+" ["+stateIso+"]";
    }
}
