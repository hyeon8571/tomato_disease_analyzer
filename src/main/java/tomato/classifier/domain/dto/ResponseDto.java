package tomato.classifier.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResponseDto<T> {
    private final Integer code; // 1 성공, -1 실패
    private final String msg;
    private final T data; // 에러 메시지가 될수도 있고, json이 될수도 있으므로 제네릭 타입
}