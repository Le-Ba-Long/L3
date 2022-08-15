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
@Table(name = "commune")
public class Commune extends BaseObject {
    @Column(name = "code", columnDefinition = "nvarchar(255)", nullable = false)
    private String code;
    @Column(name = "name", columnDefinition = "nvarchar(255)", nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;


}
