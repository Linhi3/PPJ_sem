package org.linhart.ppj.sem.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.linhart.ppj.sem.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class CityController {

    @Value("${api_geo_loc_url}")
    private String ApiGeoLocUrl;

    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final MeteorDataRepository meteorDataRepository;
    final private Logger logger = LoggerFactory.getLogger(CityController.class);

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
    public String addCity(@RequestParam String stateIso, @RequestParam String cityName){
        State state = stateRepository.findStateByIsoCodeEquals(stateIso);
        String url = ApiGeoLocUrl;
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(String.format(url, cityName, stateIso), String.class);
            if (response.getBody() != null && !response.getBody().isEmpty()) {
                JsonNode root = mapper.readTree(response.getBody());
                double lat = root.findValue("lat").asDouble();
                double lon = root.findValue("lon").asDouble();
                cityRepository.save(new City(cityName, lat, lon, state));
            } else {
                logger.error("Mesto se nepodarilo pridat");
                return "Mesto se nepodarilo pridat";

            }
            logger.info("Pridano mesto {} do statu {}", cityName, state.getIsoCode());
            return "Pridano mesto " + cityName + " do statu " + state.getIsoCode();
        }catch (JsonProcessingException | RestClientException e  ){
            logger.error("Chyba spojeni se serverem openweather");
            return "Chyba spojeni se serverem openweather";
        }
    }
    @DeleteMapping("/cities")
    public String deleteCity(@RequestParam String cityName, @RequestParam String stateIso){
        State state = stateRepository.findStateByIsoCodeEquals(stateIso);
        City city = cityRepository.findCityByStateIsoEqualsAndNameEquals(state,cityName);
        if (city != null) {
            List<MeteorData> data = meteorDataRepository.findMeteorDataByCityIDEquals(city);
            meteorDataRepository.deleteAll(data);
            cityRepository.delete(city);
            logger.info("Odstraneno mesto {} ze statu {}", cityName, state.getIsoCode());
            return "Odstraneno mesto " + cityName + " ze statu " + state.getIsoCode();
        }else{
            logger.error("Mesto se nepodarilo odstranit");
            return "Mesto se nepodarilo odstranit";
        }
    }
}
