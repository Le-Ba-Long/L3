package com.globits.da.repository;

import com.globits.da.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    //    @Query(value = "SELECT e FROM Employee e WHERE e.code LIKE %" + emp"%"
//            + " OR e.name LIKE %:keyword% "
//            + " OR e.phone LIKE %:keyword% "
//            + " OR e.email LIKE %:keyword% "
//            + " OR e.age LIKE %:keyword% ")
//    public List<Employee> findByAllKeyword(Employee employee);
//
//    @Query("SELECT e  FROM Employee e")
//    Page<EmployeeDTO> getPage(Pageable pageable);
    @Query(value = "SELECT e FROM Employee e INNER JOIN EmployeeCertificate c ON e.id=c.employee.id GROUP BY e HAVING COUNT(c.certificate.id)>2")
    List<Employee> findAllByEmployeeHaveMoreThanTwoCertificate();

    @Query("select e.province.id ,count(e) from Employee e where e.province.id = :province_id group by e.province.id")
    List<Object[]> countEmployeeGroupByProvince(@Param("province_id") UUID id);

    Employee getById(UUID id);

    boolean existsEmployeeByCode(String code);

    boolean existsEmployeeById(UUID uuid);

    boolean existsEmployeeByEmail(String email);

    @Query(value = "select REPLACE(REPLACE(REPLACE(B1.sl,\"1\",\"Mot Van Bang\"),\"2\",\"Hai van bang\"),\"99\",\"Lon hon hai van bang\") " +
            ", ROUND((count(B1.sl)*100)/(select count(*) from employee a where a.id in (select employee_Id from employee_certificate) ),2) as \"SL_NV\" from (\n" +
            "select e.name,case when count(ec.employee_Id) > 2 then 99 else count(ec.employee_Id) end  as sl\n" +
            "from employee as e\n" +
            "inner join employee_certificate as ec \n" +
            "on e.id = ec.employee_Id\n" +
            "group by  ec.employee_Id \n" +
            ") as B1  GROUP BY B1.sl\n", nativeQuery = true)
    List<Object[]> calculatePercentageOfEmployeesWithDiplomas();

}
