package org.linhart.ppj.sem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.linhart.ppj.sem.entities.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@SpringBootApplication
public class PpjSemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PpjSemApplication.class, args);
    }

    private ResponseEntity<String> response;

    @Value("${api_wheather_url_timed}")
    private String meteorApiUrlCurrent;

    @Bean
    public CommandLineRunner run(StateRepository stateRepository, CityRepository cityRepository,MeteorDataRepository meteorDataRepository) {
    return args -> {
        //meteorDataRepository.deleteAll();
        //cityRepository.deleteAll();
        //stateRepository.deleteAll();


        LocalDate current_date = LocalDate.now().atStartOfDay().toLocalDate();
        LocalDate date1day = current_date.minusDays(1);
        long current_timeUNIX = current_date.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        long old_timeUNIX = date1day.atStartOfDay().toEpochSecond(ZoneOffset.UTC);


        //State cesko = new State("CZ","cesko");
        //State usa = new State("USA","usa");
        //City duchcov = new City("Duchcov",50.605540,13.746286,cesko);
        //City liberec = new City("Liberec",50.770309,15.086285,cesko);
        //City newyork = new City("New York",40.769033,-73.985947,usa);
        //MeteorData data1 = new MeteorData(187494,29,1000,69,duchcov);
        //MeteorData data2 = new MeteorData(187495,29,1000,69,duchcov);
        //stateRepository.save(cesko);
        //stateRepository.save(usa);
        //cityRepository.save(duchcov);
        //cityRepository.save(liberec);
        //cityRepository.save(newyork);

        City duchcov = cityRepository.findCitiesByNameEquals("Duchcov");
        City liberec = cityRepository.findCitiesByNameEquals("Liberec");
        City newyork = cityRepository.findCitiesByNameEquals("New York");


//        ObjectMapper objectMapper = new ObjectMapper();
//
//        RestTemplate restTemplate = new RestTemplate();
//        String calledUrl = String.format(Locale.US,this.meteorApiUrlCurrent, liberec.getLat(), liberec.getLon(),old_timeUNIX,current_timeUNIX-1);
//        response = restTemplate.getForEntity(calledUrl, String.class);
//        String responseBody = response.getBody();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        JsonNode listNode = jsonNode.get("list");
//        for (JsonNode item : listNode) {
//            long dt = item.get("dt").asLong();
//            float temp = (float) item.get("main").get("temp").asDouble();
//            float pressure = (float) item.get("main").get("pressure").asDouble();
//            float humidity = (float) item.get("main").get("humidity").asDouble();
//            meteorDataRepository.save(new MeteorData(dt,temp,pressure,humidity,liberec));
//        }
//
//        calledUrl = String.format(Locale.US,this.meteorApiUrlCurrent, duchcov.getLat(), duchcov.getLon(),old_timeUNIX,current_timeUNIX-1);
//        response = restTemplate.getForEntity(calledUrl, String.class);
//        responseBody = response.getBody();
//        jsonNode = objectMapper.readTree(responseBody);
//        listNode = jsonNode.get("list");
//        for (JsonNode item : listNode) {
//            long dt = item.get("dt").asLong();
//            float temp = (float) item.get("main").get("temp").asDouble();
//            float pressure = (float) item.get("main").get("pressure").asDouble();
//            float humidity = (float) item.get("main").get("humidity").asDouble();
//            meteorDataRepository.save(new MeteorData(dt,temp,pressure,humidity,duchcov));
//        }


        //cityRepository.save(teplice);
        //meteorDataRepository.save(data3);

        //System.out.println(stateRepository.findAll());
        //System.out.println(cityRepository.findAll());
        //System.out.println(meteorDataRepository.findAll());
        //System.out.println(meteorDataRepository.findMeteorDataByCityIDEquals(duchcov));
        List<MeteorData> results = meteorDataRepository.findByCityIDEqualsAndTimestampBetween(duchcov, old_timeUNIX, current_timeUNIX);
        for (MeteorData meteorData : results) {
            System.out.println(meteorData);
        }
    };
    }

}
