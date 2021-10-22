package com.amsidh.mvc.repository;

import com.amsidh.mvc.entity.Person;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PersonRepository extends ReactiveMongoRepository<Person, Integer> {
}
