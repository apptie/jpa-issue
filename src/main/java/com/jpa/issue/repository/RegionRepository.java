package com.jpa.issue.repository;

import com.jpa.issue.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, String> {

}
