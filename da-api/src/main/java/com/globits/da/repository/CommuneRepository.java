package com.globits.da.repository;

import com.globits.da.domain.Commune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommuneRepository extends JpaRepository<Commune, UUID> {

    Commune getById(UUID id);

    Commune findByIdAndDistrictId(UUID communeId, UUID districtId);

    Boolean existsCommuneById(UUID id);

    Boolean existsCommuneByCode(String code);

    Boolean existsCommuneByName(String name);
}
