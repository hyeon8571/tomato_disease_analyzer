package tomato.classifier.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    USER("일반이용자"), MANAGER("매니저"), ADMIN("관리자");

    public String value;
}
