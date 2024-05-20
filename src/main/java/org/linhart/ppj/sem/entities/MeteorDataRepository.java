package org.linhart.ppj.sem.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeteorDataRepository extends CrudRepository<MeteorData, Long> {
    List<MeteorData> findMeteorDataByCityIDEquals(City city);
    List<MeteorData> findByCityIDEqualsAndTimestampBetween(City city, Long from, Long to);
}
