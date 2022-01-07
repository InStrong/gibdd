package com.example.nasa_telegram_spring.Model.Entity.Nasa;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "iamToken",
        "expiresAt"
})
@Generated("jsonschema2pojo")
public class IAmToken {

    @JsonProperty("iamToken")
    public String iamToken;
    @JsonProperty("expiresAt")
    public String expiresAt;

}
