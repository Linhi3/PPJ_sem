package org.linhart.ppj.sem.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.linhart.ppj.sem.controllers.CityController;
import org.linhart.ppj.sem.entities.City;
import org.linhart.ppj.sem.entities.MeteorData;
import org.linhart.ppj.sem.entities.MeteorDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;

@Service
public class MeteorDataHnadler {


    @Value("${api_weather_url_timed}")
    private String meteorApiUrlTimed;
    @Value("${api_weather_url_current}")
    private String meteorApiUrlCurrent;

    private final MeteorDataRepository meteorDataRepository;
    final private Logger logger = LoggerFactory.getLogger(CityController.class);

    public MeteorDataHnadler(MeteorDataRepository meteorDataRepository) {
        this.meteorDataRepository = meteorDataRepository;
    }


    public boolean DownloadNewData(City city){
        LocalDate current_date = LocalDate.now().atStartOfDay().toLocalDate();
        for(int i=0;i<14;i++){
            LocalDate start_date = current_date.minusDays(i+1);
            LocalDate end_date = current_date.minusDays(i);
            long start_dateUNIX = start_date.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
            long end_dateUNIX = end_date.atStartOfDay().toEpochSecond(ZoneOffset.UTC)-1;
            if (meteorDataRepository.findMeteorDataByTimestampEqualsAndCityIDEquals(start_dateUNIX,city)==null){
                logger.info("stahuji data: mesto:{} datum: {}", city.getName(), java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        .format(java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(start_dateUNIX), java.time.ZoneId.systemDefault())));
                ObjectMapper objectMapper = new ObjectMapper();
                RestTemplate restTemplate = new RestTemplate();
                String calledUrl = String.format(Locale.US,this.meteorApiUrlTimed, city.getLat(), city.getLon(),start_dateUNIX,end_dateUNIX);
                try{
                    ResponseEntity<String> response = restTemplate.getForEntity(calledUrl, String.class);
                    String responseBody = response.getBody();
                    JsonNode jsonNode = objectMapper.readTree(responseBody);
                    JsonNode listNode = jsonNode.get("list");
                    for (JsonNode item : listNode) {
                        long dt = item.get("dt").asLong();
                        float temp = (float) item.get("main").get("temp").asDouble();
                        float roundedTemp = Math.round(temp * 100.0f) / 100.0f;
                        float pressure = (float) item.get("main").get("pressure").asDouble();
                        float roundedPressure = Math.round(pressure * 100.0f) / 100.0f;
                        float humidity = (float) item.get("main").get("humidity").asDouble();
                        float roundedHumidity = Math.round(humidity * 100.0f) / 100.0f;
                        meteorDataRepository.save(new MeteorData(dt,roundedTemp,roundedPressure,roundedHumidity,city));
                    }
                    logger.info("stazena data: mesto:{} datum: {}", city.getName(), java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            .format(java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(start_dateUNIX), java.time.ZoneId.systemDefault())));
                }catch (JsonProcessingException | RestClientException e){
                    logger.error("Chyba spojeni se serverem openweather");
                    return false;
                }
            }
        }
        return true;
    }

    public String CalculateMeteorData(City city){
        LocalDate current_date = LocalDate.now().atStartOfDay().toLocalDate();
        LocalDate date1day = current_date.minusDays(1);
        LocalDate date7days = current_date.minusDays(7);
        LocalDate date14days = current_date.minusDays(14);
        long date1dayUNIX = date1day.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        long date7daysUNIX = date7days.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        long date14daysUNIX = date14days.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        long end_dateUNIX = current_date.atStartOfDay().toEpochSecond(ZoneOffset.UTC)-1;
        List<MeteorData>data1day = meteorDataRepository.findByCityIDEqualsAndTimestampBetween(city,date1dayUNIX,end_dateUNIX);
        List<MeteorData>data7days = meteorDataRepository.findByCityIDEqualsAndTimestampBetween(city,date7daysUNIX,end_dateUNIX);
        List<MeteorData>data14days = meteorDataRepository.findByCityIDEqualsAndTimestampBetween(city,date14daysUNIX,end_dateUNIX);
        double day1AVG = 0, day7AVG = 0, day14AVG = 0;
        for (MeteorData data: data1day){day1AVG += data.getTemperature();}
        for (MeteorData data: data7days){day7AVG += data.getTemperature();}
        for (MeteorData data: data14days){day14AVG += data.getTemperature();}

        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder response = new StringBuilder();
        response.append(String.format("Pro mesto %s:\n", city.getName()));
        RestTemplate restTemplate = new RestTemplate();
        String calledUrl = String.format(Locale.US,this.meteorApiUrlCurrent, city.getLat(), city.getLon());
        try{
            ResponseEntity<String> urlResponse = restTemplate.getForEntity(calledUrl, String.class);
            String responseBody = urlResponse.getBody();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode mainNode = jsonNode.get("main");
            float current_temp = (float) mainNode.get("temp").asDouble();
            float current_pressure = (float) mainNode.get("pressure").asDouble();
            float current_humidity = (float) mainNode.get("humidity").asDouble();
            response.append(String.format("Aktualni hodnoty:\nTeplota: %.2f°C  Tlak: %.0fhPa  Vlhkost: %.0f%%\n",current_temp,current_pressure,current_humidity));
        }catch (JsonProcessingException | RestClientException e){
            logger.error("Chyba spojeni se serverem openweather");
            response.append("Aktualni hodnoty se nepodarilo nacist: Chyba spojeni se serverem openweather\n");
        }
        response.append(String.format("Prumery:\nDenni: %.2f°C  Tydeni: %.2f°C  Ctrnactideni: %.2f°C", day1AVG/24,day7AVG/168,day14AVG/336));
        return response.toString();

    }
}
