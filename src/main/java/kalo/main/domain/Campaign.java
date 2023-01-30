package kalo.main.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Campaign extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDate votingDate;
    String title;
    String info;
    String thumbnail;
    String contentImage;
    Long vote;
}