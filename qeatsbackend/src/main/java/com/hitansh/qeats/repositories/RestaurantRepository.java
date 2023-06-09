/*
 *
 *  * Copyright (c) Temp.Do 2019. All rights reserved
 *
 */

package com.hitansh.qeats.repositories;

import com.hitansh.qeats.models.RestaurantEntity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepository extends MongoRepository<RestaurantEntity, String> {

}

