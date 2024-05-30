package org.linhart.ppj.sem.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CityRepository extends CrudRepository<City, Integer> {
    List<City> findCitiesByStateIsoEquals(State stateIso);
    City findCitiesByNameEquals(String name);
    City findCityByStateIsoEqualsAndNameEquals(State stateIso, String name);
}
