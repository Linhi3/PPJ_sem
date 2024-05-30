package org.linhart.ppj.sem.entities;

import org.springframework.data.repository.CrudRepository;


public interface StateRepository extends CrudRepository<State, String> {
    State findStateByIsoCodeEquals(String isoCode);
}
