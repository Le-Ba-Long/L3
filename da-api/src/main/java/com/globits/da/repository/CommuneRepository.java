package com.globits.da.repository;

import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommuneRepository extends JpaRepository<Commune, UUID> {
    @Query("SELECT c FROM Commune c WHERE c.code = ?1")
    Commune findByCode(String code);

    @Query("SELECT d  from District as d ,Commune  as c WHERE d.id = ?1 AND c.district.id = ?1 AND c.id = ?2")
    District findCommuneInDistrict(UUID districtId, UUID communeId);

    Boolean existsCommuneById(UUID id);

    Boolean existsCommuneByCode(String code);

    Boolean existsCommuneByName(String name);
}
