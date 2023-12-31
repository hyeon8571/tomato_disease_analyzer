package tomato.classifier.domain.type;

import lombok.Getter;

public enum SearchType {

    TITLE("제목"),
    CONTENT("본문"),
    NICKNAME("닉네임");

    @Getter
    private final String description;

    SearchType(String description) {
        this.description = description;
    }
}
