
/*
 *
 *  * Copyright (c) Temp.Do 2019. All rights reserved
 *
 */

package com.hitansh.qeats.services;

import com.hitansh.qeats.exchanges.GetRestaurantsRequest;
import com.hitansh.qeats.exchanges.GetRestaurantsResponse;
import com.hitansh.qeats.repositoryservices.RestaurantRepositoryService;

import java.time.LocalTime;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;
  // @Autowired(required = true)
  private RestaurantRepositoryService restaurantRepositoryService;


  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(
      GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {
    
    double srcLat = getRestaurantsRequest.getLatitude();
    double srcLon = getRestaurantsRequest.getLongitude();
    GetRestaurantsResponse responseRestraunts = new GetRestaurantsResponse();
    if (currentTime.isAfter(LocalTime.of(7,59,59)) && currentTime.isBefore(LocalTime.of(10,0,1))
        || currentTime.isAfter(LocalTime.of(12,59,59)) && currentTime.isBefore(LocalTime.of(14,0,1))
        || currentTime.isAfter(LocalTime.of(18,59,59)) 
        && currentTime.isBefore(LocalTime.of(21,0,1))) {
    
      responseRestraunts.setRestaurants(
          restaurantRepositoryService.findAllRestaurantsCloseBy(srcLat, 
          srcLon, currentTime, peakHoursServingRadiusInKms));
    } else {
      responseRestraunts.setRestaurants(
          restaurantRepositoryService.findAllRestaurantsCloseBy(srcLat, 
          srcLon, currentTime, normalHoursServingRadiusInKms));
    }

    //double distanceInKM = GeoUtils.findDistanceInKm(srcLat, srcLon, dstLatitude, dstLongitude);

    return responseRestraunts;
  }


}
