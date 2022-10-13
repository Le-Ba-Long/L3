package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "employee")
public class Employee extends BaseObject {

    @Column(name = "code", columnDefinition = "nvarchar(100)")
    private String code;

    @Column(name = "name", columnDefinition = "nvarchar(100)")
    private String name;

    @Column(name = "email", columnDefinition = "nvarchar(100)")
    private String email;

    @Column(name = "phone", columnDefinition = "nvarchar(20)")
    private String phone;

    @Column(name = "age", columnDefinition = "int(11)")
    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "province_id")
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "commune_id")
    private Commune commune;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<EmployeeCertificate> employeeCertificates;
}
