package org.linhart.ppj.sem.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "meteor_data")
public class MeteorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "time_stamp")
    private Long timestamp;

    @Column(name= "temperature")
    private double temperature;

    @Column(name = "pressure")
    private double pressure;

    @Column (name = "humidity")
    private double humidity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "city_id")
    private City cityID;

    public MeteorData() {
    }

    public MeteorData(long timestamp, double temperature, double pressure, double humidity, City cityID) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.cityID = cityID;
    }



    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "MeteorData{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", temperature=" + temperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", city=" + cityID +
                '}';
    }
}
