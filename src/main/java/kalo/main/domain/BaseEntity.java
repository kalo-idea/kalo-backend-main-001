package kalo.main.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    LocalDateTime createdDate;
    
    @LastModifiedDate
    @Column(nullable = false)
    LocalDateTime lastModifiedDate;

    @Column(columnDefinition = "TINYINT", length = 1)
    Boolean deleted = false;
}
