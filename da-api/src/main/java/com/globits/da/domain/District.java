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
@Table(name = "district")
public class District extends BaseObject {
    @Column(name = "code", columnDefinition = "nvarchar(255)", nullable = false)
    private String code;

    @Column(name = "name", columnDefinition = "nvarchar(255)", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district", cascade = CascadeType.ALL)
    List<Commune> communes = new ArrayList<>();

//    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    List<Employee> employees = new ArrayList<>();


}
