package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "certificate")
public class Certificate extends BaseObject {
    private static final long serialVersionUID = 1L;
    @Column(name = "code", columnDefinition = "nvarchar(255)", nullable = false)
    private String code;

    @Column(name = "name", columnDefinition = "nvarchar(255)", nullable = false)
    private String name;

    @Column(name = "granted_by", columnDefinition = "nvarchar(255)", nullable = false)
    private String grantedBy;

    @Column(name = "date_start_effect")
    private LocalDate dateStartEffect;

    @Column(name = "date_end_effect")
    private LocalDate dateEndEffect;
}
