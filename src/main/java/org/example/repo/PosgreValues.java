package org.example.repo;

import org.example.model.USER;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosgreValues extends JpaRepository<USER, Long> {
    List<USER> findByName(String name);

    @Query(value = "SELECT * FROM values WHERE surname = :value", nativeQuery = true)
    List<USER> findByYourColumn(@Param("value") String value);
}
