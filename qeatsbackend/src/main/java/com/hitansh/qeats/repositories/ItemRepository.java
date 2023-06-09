
package com.hitansh.qeats.repositories;

import com.hitansh.qeats.models.ItemEntity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<ItemEntity, String> {

}

