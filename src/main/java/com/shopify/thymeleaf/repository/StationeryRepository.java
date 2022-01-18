package com.shopify.thymeleaf.repository;

import com.shopify.thymeleaf.model.Stationery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationeryRepository extends JpaRepository<Stationery, Long> {
   // List<Student> findByName(String name);
}