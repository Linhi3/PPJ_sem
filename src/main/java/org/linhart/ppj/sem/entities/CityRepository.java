package org.linhart.ppj.sem.entities;

import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<City, Integer> {
    City findCitiesByStateIsoEquals(State state);
    City findCitiesByNameEquals(String name);
}
