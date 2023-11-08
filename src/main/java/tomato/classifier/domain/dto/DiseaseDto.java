package tomato.classifier.domain.dto;

import lombok.*;
import tomato.classifier.domain.entity.Disease;

@NoArgsConstructor
@Getter
@Setter
public class DiseaseDto {

    private String id;

    private String d_name;

    private String src;

    private String symptoms;

    private String solution;

    private Integer prob;


    @Builder
    public DiseaseDto(String id, String d_name, String src, String symptoms, String solution, Integer prob) {
        this.id = id;
        this.d_name = d_name;
        this.src = src;
        this.symptoms = symptoms;
        this.solution = solution;
        this.prob = prob;
    }

    public static DiseaseDto toDto(Disease target, Integer prob) {

        return DiseaseDto.builder()
                .id(target.getId())
                .d_name(target.getD_name())
                .src(target.getSrc())
                .symptoms(target.getSymptoms())
                .solution(target.getSolution())
                .prob(prob)
                .build();
    }
}
