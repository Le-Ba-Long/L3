package com.globits.da.repository;

import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DistrictRepository extends JpaRepository<District, UUID> {
    @Query("SELECT d FROM District d  WHERE d.code = ?1")
    District findByCode(String code);

    Boolean existsDistrictById(UUID id);

    Boolean existsDistrictByCode(String code);

    Boolean existsDistrictByName(String name);

    @Query("SELECT p  from Province as p ,District  as d WHERE p.id = ?1 AND d.province.id = ?1  AND d.id=?2")
    Province findDistinctInProvince(UUID provinceId, UUID districtId);

    @Query("SELECT d FROM District  d WHERE d.province.id = ?1")
    List<District> findAllDistrictByProvinceId(UUID province_id);
}
