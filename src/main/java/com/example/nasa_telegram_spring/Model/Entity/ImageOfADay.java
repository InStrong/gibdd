package com.example.nasa_telegram_spring.Model.Entity;

import javax.annotation.Generated;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.checkerframework.checker.units.qual.C;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("jsonschema2pojo")
@Entity
@Data
public class ImageOfADay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @JsonProperty("copyright")
    public String copyright;
    @JsonProperty("date")
    @Column(unique = true)
    public String date;
    @JsonProperty("explanation")
    @Column(length = 5000)
    public String explanation;
    @JsonProperty("hdurl")
    public String hdurl;
    @JsonProperty("media_type")
    public String mediaType;
    @JsonProperty("service_version")
    public String serviceVersion;
    @JsonProperty("title")
    @Column(length = 5000)
    public String title;
    @JsonProperty("url")
    public String url;

}