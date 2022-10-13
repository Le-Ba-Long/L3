package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "province")
public class Province extends BaseObject {

    @Column(name = "code", columnDefinition = "nvarchar(255)", nullable = false)
    private String code;

    @Column(name = "name", columnDefinition = "nvarchar(255)", nullable = false)
    private String name;

    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<District> districts = new ArrayList<>();

//    @OneToOne(cascade = CascadeType.ALL)
//    Certificate certificate;

//    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    List<Employee> employees = new ArrayList<>();

}
