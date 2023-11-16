package com.myo.myotutorial.repo;

import com.myo.myotutorial.model.GetValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepoGetValues extends JpaRepository<GetValues, Long> {
    List<GetValues> findByName(String name);

    @Query(value = "SELECT * FROM values WHERE surname = :value", nativeQuery = true)
    List<GetValues> findByYourColumn(@Param("value") String value);
}
