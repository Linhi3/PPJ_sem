package org.linhart.ppj.sem;
import org.linhart.ppj.sem.entities.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.http.ResponseEntity;


@SpringBootApplication
public class PpjSemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PpjSemApplication.class, args);
    }
}
