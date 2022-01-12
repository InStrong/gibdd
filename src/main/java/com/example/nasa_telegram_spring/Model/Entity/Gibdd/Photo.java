//package com.example.nasa_telegram_spring.Model.Entity.Gibdd;
//
//import javax.annotation.Generated;
//import javax.persistence.*;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//import lombok.Data;
//import org.hibernate.engine.jdbc.ClobProxy;
//
//import java.sql.Clob;
//
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown = true)
//@Data
//@Entity
//@JsonPropertyOrder({
//        "base64Value",
//        "type"
//})
//@Generated("jsonschema2pojo")
//public class Photo {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    public Integer id;
//    @JsonProperty("base64Value")
//    @Column(name = "base64value")
//    public String base64Value;
//    public Byte[] imageFile;
//    @Column(name = "num_post")
//    public String numPost;
//}