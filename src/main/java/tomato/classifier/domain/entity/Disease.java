package tomato.classifier.domain.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "disease")
@Getter
public class Disease {

    @Id
    private String id;

    @Column
    private String d_name;

    @Column(columnDefinition = "TEXT") // 용량 문제 해결
    private String symptoms; // 증상

    @Column(length = 10000)
    private String solution;

    @Column
    private String src;

}
