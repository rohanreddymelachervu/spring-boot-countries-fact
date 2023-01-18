package com.example.counties.service;
import com.example.counties.controller.Controller;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Service
public class ControllerService {
    Logger logger = LoggerFactory.getLogger(ControllerService.class);
    JSONArray jsonArray;
    ControllerService(){}
    public ControllerService(JSONArray jsonArray){
        this.jsonArray = jsonArray;
    }
    public List<Object> getCountryByName(String name){
        logger.info("Get Country by name endpoint hit");
        List<Object> countriesList = new ArrayList<>();
        for(Object jsonObject : jsonArray){
            jsonObject = (JSONObject) jsonObject;
            JSONObject jsonName = ((JSONObject) jsonObject).getJSONObject("name");
            String commonName = jsonName.getString("common");
            if(commonName.equalsIgnoreCase(name)){
                logger.info("Found matching country");
                countriesList.add(((JSONObject) jsonObject).toMap());
            }
        }
        if(countriesList.isEmpty()){
            logger.info("No matching country found for name " + name);
        }
        return countriesList;
    }
    public List<Object> filterByRegion(String region){
        logger.info("Filter Regions endpoint hit");
        List<Object> regionsList = new ArrayList<>();
        for(Object jsonObject : jsonArray){
            jsonObject = (JSONObject) jsonObject;
            String regionName = ((JSONObject) jsonObject).getString("region");
            if(region.equalsIgnoreCase(regionName)){
                regionsList.add(((JSONObject) jsonObject).toMap());
            }
        }
        if(regionsList.isEmpty()){
            logger.info("No countries found for this region " + region);
        }
        return regionsList;
    }
    public List<Object> sortByCountryName(){
        logger.info("Sort name endpoint hit");
        List<Object> countryNamesList = new ArrayList<>();
        for(Object jsonObject : jsonArray){
            jsonObject = (JSONObject) jsonObject;
            JSONObject jsonName = ((JSONObject) jsonObject).getJSONObject("name");
            countryNamesList.add(jsonName.toMap());
        }
        Collections.sort(countryNamesList, (a,b) -> a.toString().compareTo(b.toString()));
        logger.info("Completed sorting countries by name");
        return countryNamesList;
    }
    public List<Object> paginateResponse(String page){
        logger.info("Paginate endpoint hit");
        List<Object> paginatedResponse = new ArrayList<>();
        int length = 250, pageSize = 25, numberOfObjectsPerPage = length / pageSize, pageNumber = 0;
        try {
            pageNumber = Integer.parseInt(page);
        }catch(Exception e){
            logger.info(e.toString());
            return null;
        }
        if(pageNumber <= 0 || pageNumber > numberOfObjectsPerPage){
            logger.info("Invalid page number");
            return null;
        }
        int startIndex = (pageNumber-1) * numberOfObjectsPerPage;
        int endIndex = startIndex + numberOfObjectsPerPage;
        List<Object> countriesList = new ArrayList<>();
        for(Object jsonObject : jsonArray){
            countriesList.add(jsonObject);
        }
        while(startIndex <= endIndex){
            JSONObject jsonObject = (JSONObject) countriesList.get(startIndex);
            paginatedResponse.add(jsonObject.toMap());
            startIndex++;
        }
        logger.info("Paginated the response");
        return paginatedResponse;
    }
    public List<Object> findPropertyForCountry(String country, String property){
        logger.info("Find property for country endpoint hit");
        List<Object> propertiesList = new ArrayList<>();
        if(property.isEmpty()){
            logger.info("Empty query parameter property");
            return null;
        }
        if(country.isEmpty()){
            logger.info("Country empty, returning list of countries");
            return jsonArray.toList();
        }
        for(Object jsonObject : jsonArray){
            jsonObject = (JSONObject) jsonObject;
            JSONObject jsonName = ((JSONObject) jsonObject).getJSONObject("name");
            String commonName = jsonName.getString("common");
            if(commonName.equalsIgnoreCase(country)){
                try{
                    Object jsonProperty = ((JSONObject) jsonObject).get(property);
                    logger.info("Found property for the country");
                    if(jsonProperty instanceof JSONObject){
                        propertiesList.add(((JSONObject) jsonProperty).toMap());
                    }
                    else{
                        propertiesList.add(jsonProperty);
                    }
                    return propertiesList;
                }catch(Exception e){
                    logger.info(e.toString());
                    return null;
                }
            }
        }
        logger.info("No country found with this name");
        return null;
    }

}
