package com.globits.da.repository;

import com.globits.da.domain.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    Boolean existsCertificateById(UUID id);

    Certificate getById(UUID id);

    boolean existsCertificateByCode(String code);

    boolean existsCertificateByName(String name);
}
