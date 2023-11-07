package tomato.classifier.domain.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import tomato.classifier.domain.dto.ArticleDto;

@Setter
@Getter
@RequiredArgsConstructor
public class ArticleRequest {

    private String title;

    private String content;

    private String nickname;

    @Builder
    public ArticleRequest(String title, String content, String nickname) {
        this.title = title;
        this.content = content;
        this.nickname = nickname;
    }

    public ArticleDto toDto() {
        return ArticleDto.builder()
                .title(this.title)
                .content(this.content)
                .nickname(this.nickname)
                .build();
    }
}
