package com.globits.da.repository;

import com.globits.da.domain.EmployeeCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeCertificateRepository extends JpaRepository<EmployeeCertificate, UUID> {
    EmployeeCertificate getById(UUID id);

    @Query(value = "SELECT count(id) FROM  employee_certificate  "
            + "WHERE id != :id "
            + "and employee_Id = :employee_id "
            + "and certificate_Id = :employee_certificate_id "
            + "and province_Id = :province_id "
            + "and datediff(curdate(),expirationDate) < 0", nativeQuery = true)
    int countExpireCertificateOfTheProvinceExcept(@Param("id") String id,@Param("employee_id") String employeeId,@Param("employee_certificate_id") String employeeCertificateId,@Param("province_id") String provinceId);

    @Query(value = "SELECT count(id) FROM  employee_certificate "
            + "WHERE id != :id "
            + "and employee_Id = :employee_id "
            + "and certificate_Id = :employee_certificate_id "
            + "and datediff(curdate(),expirationDate) < 0", nativeQuery = true)
    Integer countMoreThan3ValidCertificatesHaveTheSameTypeExcept(@Param("id") String id,@Param("employee_id") String employeeId,@Param("employee_certificate_id") String employeeCertificateId);

    @Query(value = "SELECT count(id) FROM  employee_certificate "
            + "WHERE employee_Id = :employee_id "
            + "and certificate_Id = :employee_certificate_id "
            + "and province_Id = :province_id "
            + "and datediff(curdate(),expirationDate) < 0", nativeQuery = true)
    int countExpireCertificateOfTheProvince(@Param("employee_id") String employeeId, @Param("employee_certificate_id") String employeeCertificateId,@Param("province_id") String provinceId);

    @Query(value = "SELECT count(id) FROM  employee_certificate "
            + "WHERE employee_Id = :employee_id "
            + "and certificate_Id = :employee_certificate_id "
            + "and datediff(curdate(),expirationDate) < 0  ", nativeQuery = true)
    Integer countMoreThan3ValidCertificatesHaveTheSameType(@Param("employee_id") String employeeId,@Param("employee_certificate_id") String employeeCertificateId);

}
