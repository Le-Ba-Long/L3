package com.globits.da.domain;


import com.globits.core.domain.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    //@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    // @JoinColumn(name = "province_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "province_id" ,nullable = false)
    private Province province;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "district",cascade = CascadeType.ALL)
    List<Commune> communes = new ArrayList<>();
 //   private Set<Commune> communes = new HashSet<>();


}
