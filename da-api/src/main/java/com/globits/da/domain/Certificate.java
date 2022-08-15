package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "certificate")
public class Certificate extends BaseObject {
    private static final long serialVersionUID = 1L;
    @Column(name = "code",columnDefinition = "nvarchar(255)",nullable = false)
    private String code;
    @Column(name = "name",columnDefinition = "nvarchar(255)",nullable = false)
    private String name;
    @Column(name = "grantedby",columnDefinition = "nvarchar(255)",nullable = false)
    private String grantedBy;
    @Temporal(TemporalType.DATE)
    @Column(name = "dateStartEffect")
    private Date  dateStartEffect;
    @Temporal(TemporalType.DATE)
    @Column(name = "dateEndEffect")
    private Date  dateEndEffect;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", referencedColumnName = "id",nullable = false)
    private Province province;
}
