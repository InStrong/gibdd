package com.example.nasa_telegram_spring.Model.Entity.Nasa;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "translations"
})
@Generated("jsonschema2pojo")
public class TranslationBody {

    @JsonProperty("translations")
    public List<Translation> translations = new ArrayList<Translation>();

}