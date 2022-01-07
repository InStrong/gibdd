package com.example.nasa_telegram_spring.Model.Entity;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "text"
})
@Generated("jsonschema2pojo")
public class Translation {

    @JsonProperty("text")
    public String text;

}
