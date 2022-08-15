package com.globits.da.repository;

import com.globits.da.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, UUID> {
    @Query("SELECT p FROM Province p WHERE p.code = ?1")
    Province findByCode(String code);

    Boolean existsProvinceById(UUID id);

    Boolean existsProvinceByCode(String code);

    Boolean existsProvinceByName(String name);
}
