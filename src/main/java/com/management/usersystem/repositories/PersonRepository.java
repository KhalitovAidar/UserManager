package com.management.usersystem.repositories;

import com.management.usersystem.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findById(int id);
    void deleteById(int id);
    boolean existsById(int id);
    @Query("FROM Person order by firstname asc")
    List<Person> filterByFirstName();
    @Query("FROM Person order by lastname asc")
    List<Person> filterByLastName();
    @Query("FROM Person order by createdAt asc")
    List<Person> filterByCreatedAt();
}
