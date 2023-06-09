/*
 *
 *  * Copyright (c) Temp.Do 2019. All rights reserved
 *
 */

package com.hitansh.qeats.repositoryservices;

import com.hitansh.qeats.dto.Restaurant;
import java.time.LocalTime;
import java.util.List;

public interface RestaurantRepositoryService {

  /**
   * Get the list of open restaurants within the specified serving radius.
   *   - Use the Haversine formula to find the distance between two lat/longs.
   *   - Ensure the restaurant is open currently.
   * @param latitude coordinates near which we have to search for restaurant
   * @param longitude coordinates near which we have to search for restaurant
   * @param currentTime current time
   * @param servingRadiusInKms serving radius
   * @return list of open restaurants within the specified radius or
   *     empty list if there is none
   */
  List<Restaurant> findAllRestaurantsCloseBy(Double latitude, Double longitude,
                                             LocalTime currentTime, Double servingRadiusInKms);

}

