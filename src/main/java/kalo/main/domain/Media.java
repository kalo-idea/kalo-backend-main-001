package kalo.main.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Media extends BaseEntity {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String fileName = "";

    
    public Media(String fileName) {
        this.fileName = fileName;
    }
}
