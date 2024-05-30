package org.linhart.ppj.sem.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.linhart.ppj.sem.entities.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class CityController {

    @Value("${api_geo_loc_url}")
    private String ApiGeoLocUrl;

    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final MeteorDataRepository meteorDataRepository;

    public CityController(CityRepository cityRepository, StateRepository stateRepository, MeteorDataRepository meteorDataRepository) {
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.meteorDataRepository = meteorDataRepository;
    }

    @GetMapping("/cities")
    public String getCities(@RequestParam String stateIso){
        State state = stateRepository.findStateByIsoCodeEquals(stateIso);
        List<City> cityList = cityRepository.findCitiesByStateIsoEquals(state);
        StringBuilder response = new StringBuilder();
        response.append(String.format("Dostupna mesta v %s:\n",state.getIsoCode()));
        for (City city : cityList) {
            response.append(city.getName()).append("\n");
        }
        return response.toString();
    }
    @PutMapping("/cities")
    public String addCity(@RequestParam String stateIso, @RequestParam String cityName) throws JsonProcessingException {
        State state = stateRepository.findStateByIsoCodeEquals(stateIso);
        String url = ApiGeoLocUrl;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(String.format(url, cityName, stateIso), String.class);
        ObjectMapper mapper = new ObjectMapper();
        if (response.getBody() != null && !response.getBody().isEmpty()) {
            JsonNode root = mapper.readTree(response.getBody());
            double lat = root.findValue("lat").asDouble();
            double lon = root.findValue("lon").asDouble();
            cityRepository.save(new City(cityName, lat, lon, state));
        }
        return "Added city " + cityName + " to state " + state.getIsoCode();
    }
    @DeleteMapping("/cities")
    public String deleteCity(@RequestParam String cityName, @RequestParam String stateIso){
        State state = stateRepository.findStateByIsoCodeEquals(stateIso);
        City city = cityRepository.findCityByStateIsoEqualsAndNameEquals(state,cityName);
        List<MeteorData> data = meteorDataRepository.findMeteorDataByCityIDEquals(city);
        meteorDataRepository.deleteAll(data);
        cityRepository.delete(city);
        return "Deleted city " + cityName + " from state " + state.getIsoCode();
    }

}
