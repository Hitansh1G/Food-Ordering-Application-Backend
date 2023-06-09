/*
 *
 *  * Copyright (c) Temp.Do 2019. All rights reserved
 *
 */

package com.hitansh.qeats.repositoryservices;

import com.hitansh.qeats.dto.Restaurant;
import com.hitansh.qeats.models.RestaurantEntity;
import com.hitansh.qeats.repositories.RestaurantRepository;
import com.hitansh.qeats.utils.GeoUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Provider;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


@Service
public class RestaurantRepositoryServiceImpl implements RestaurantRepositoryService {




  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private Provider<ModelMapper> modelMapperProvider;

  @Autowired
  private RestaurantRepository dataSet;

  private boolean isOpenNow(LocalTime time, RestaurantEntity res) {
    LocalTime openingTime = LocalTime.parse(res.getOpensAt());
    LocalTime closingTime = LocalTime.parse(res.getClosesAt());

    return time.isAfter(openingTime) && time.isBefore(closingTime);
  }

  // Objectives:
  // 1. Implement findAllRestaurantsCloseby.
  // 2. Remember to keep the precision of GeoHash in mind while using it as a key.
  // Check RestaurantRepositoryService.java file for the interface contract.
  public List<Restaurant> findAllRestaurantsCloseBy(Double latitude,
                                                    Double longitude, LocalTime currentTime, Double servingRadiusInKms)
      throws NullPointerException {

    List<Restaurant> restaurants = new ArrayList<>();
    List<RestaurantEntity> tmp = new ArrayList<RestaurantEntity>();
    List<Restaurant> myList = new ArrayList<Restaurant>();
    try {
      tmp = dataSet.findAll();
      
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    ModelMapper mapperClass = modelMapperProvider.get();
    for (RestaurantEntity ent : tmp) {
      restaurants.add(mapperClass.map(ent, Restaurant.class));
    }
    // System.out.println();

    for (Restaurant res : restaurants) {
      double restaurantLat = res.getLatitude();
      double restaurantLon = res.getLongitude();
      LocalTime openAt = LocalTime.parse(res.getOpensAt());
      LocalTime closeAt = LocalTime.parse(res.getClosesAt());
      double distanceKm = GeoUtils.findDistanceInKm(
                            latitude, longitude, restaurantLat, restaurantLon);
      if (Double.compare(distanceKm, servingRadiusInKms) > 0) {
        continue;
      }
      boolean result = isValidTime(currentTime, openAt, closeAt);
      if (result == false) {
        continue;
      }
      myList.add(res);

    }
    return myList;
  }

  private boolean isValidTime(LocalTime current, 
                              LocalTime open, LocalTime close) {
    boolean result = false;
    if (current.equals(open) || current.equals(close)) {
      result = true;
    }
    if (current.isAfter(open) && current.isBefore(close)) {
      result = true;
    }
    return result;
  }



  // Check if a restaurant is nearby and open. If so, it is a candidate to be returned.
  // NOTE: How far exactly is "nearby"?

  /**
   * Utility method to check if a restaurant is within the serving radius at a given time.
   * @return boolean True if restaurant falls within serving radius and is open, false otherwise
   */
  private boolean isRestaurantCloseByAndOpen(RestaurantEntity restaurantEntity,
      LocalTime currentTime, Double latitude, Double longitude, Double servingRadiusInKms) {
    if (isOpenNow(currentTime, restaurantEntity)) {
      return GeoUtils.findDistanceInKm(latitude, longitude,
          restaurantEntity.getLatitude(), restaurantEntity.getLongitude())
          < servingRadiusInKms;
    }

    return false;
  }



}
