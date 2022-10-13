package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employee_certificate")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EmployeeCertificate extends BaseObject {
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "employee_id", nullable = false)
    Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "certificate_id", nullable = false)
    Certificate certificate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "province_id", nullable = false, referencedColumnName = "id")
    Province province;

}
