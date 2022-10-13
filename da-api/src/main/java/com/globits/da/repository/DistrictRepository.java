package com.globits.da.repository;

import com.globits.da.domain.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DistrictRepository extends JpaRepository<District, UUID> {

    District getById(UUID id);

    Boolean existsDistrictById(UUID id);

    Boolean existsDistrictByCode(String code);

    Boolean existsDistrictByName(String name);

    District findByIdAndProvinceId(UUID districtId, UUID provinceId);

    @Query("SELECT d FROM District  d WHERE d.province.id = :province_id")
    List<District> findAllDistrictByProvinceId(@Param("province_id") UUID provinceId);
}
