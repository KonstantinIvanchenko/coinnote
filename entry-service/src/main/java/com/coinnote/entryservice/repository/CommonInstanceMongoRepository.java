package com.coinnote.entryservice.repository;

import com.coinnote.entryservice.enitity.CommonInstance;
import com.coinnote.entryservice.enitity.User;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;
import java.util.Optional;

public interface CommonInstanceMongoRepository<I extends CommonInstance> extends MongoRepository<I, String> {
    Optional<I> findById(String id);
    //Optional<I> findByName(String name);
    //Optional<List<I>> findByUserName(String userName);
    Optional<List<I>> findByUser(User user);
}
