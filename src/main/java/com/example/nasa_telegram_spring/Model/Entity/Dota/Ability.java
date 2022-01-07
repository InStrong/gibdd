package com.example.nasa_telegram_spring.Model.Entity.Dota;

import javax.annotation.Generated;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("jsonschema2pojo")
@Data
@Entity
public class Ability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @JsonProperty("dname")
    public String dname;
    @JsonProperty("img")
    public String img;

    public Ability(String dname, String img) {
        this.dname = dname;
        this.img = img;
    }

    public Ability() {
    }
}
