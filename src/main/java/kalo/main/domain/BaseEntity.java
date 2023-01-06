package kalo.main.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    LocalDateTime createdDate;
    
    @LastModifiedDate
    @Column(nullable = false)
    LocalDateTime lastModifiedDate;

    @Column(columnDefinition = "TINYINT", length = 1)
    Boolean deleted = true;
}
