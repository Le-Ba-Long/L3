package com.globits.da.repository;

import com.globits.da.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, UUID> {

    Province getById(UUID id);

    Boolean existsProvinceById(UUID id);

    Boolean existsProvinceByCode(String code);

    Boolean existsProvinceByName(String name);
}
