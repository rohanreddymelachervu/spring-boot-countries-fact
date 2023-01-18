package com.example.counties.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.example.counties.service.ControllerService;
import org.json.*;

@RestController
public class Controller{
    Logger logger = LoggerFactory.getLogger(Controller.class);
    String reqUrl = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json";
    String returnedJson;
    JSONArray jsonArray;
    ControllerService controllerService;
    private String makeConnection(){
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int status = connection.getResponseCode();
            logger.info(Integer.toString(status));
            if(status > 299){
                logger.info("Error in receiving JSON from URL");
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while((line = reader.readLine()) != null){
                    responseContent.append(line);
                }
                reader.close();
            }else {
                logger.info("Received JSON from URL");
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
            return responseContent.toString();
        }catch(Exception e) {
            logger.info(e.toString());
        }
        return "Error in making request to URL";
    }
    public Controller(){
        try{
            returnedJson = makeConnection();
        }catch(Exception e){
            logger.info(e.toString());
        }
        jsonArray = new JSONArray(returnedJson);
        logger.info("Converted to Json Array");
        controllerService = new ControllerService(jsonArray);
    }
    @GetMapping("/getJson")
    private String getJson(){
        logger.info("getJson endpoint hit");
        return returnedJson;
    }
    @GetMapping("/country/{name}")
    private List<Object> getCountryByName(@PathVariable String name){
        return controllerService.getCountryByName(name);
    }
    @GetMapping("/filter/{region}")
    private List<Object> filterByRegion(@PathVariable String region){
        return controllerService.filterByRegion(region);
    }
    @GetMapping("/sort")
    private List<Object> sortByCountryName(){
        return controllerService.sortByCountryName();
    }
    @GetMapping("/paginate")
    private List<Object>  paginateResponse(@RequestParam String page){
        return controllerService.paginateResponse(page);
    }
    @GetMapping("/property")
    private List<Object> findPropertyForCountry(@RequestParam String country, @RequestParam String property){
        return controllerService.findPropertyForCountry(country, property);
    }
}

