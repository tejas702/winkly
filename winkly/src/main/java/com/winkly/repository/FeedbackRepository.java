package com.winkly.repository;

import com.winkly.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, String> {
    List<Optional<FeedbackEntity>> findByEmail(String email);


}
