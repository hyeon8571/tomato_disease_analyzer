package tomato.classifier.config.jwt;

/**
 * SECRET은 노출되면 안된다. 클라우드 AWS - 환경변수, 파일에 있는 것을 읽을 수도 있고)
 */
public interface JwtVO {
    public static final String SECRET = "classifierSecret"; // HS256(대칭키)

    public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 ; // 하루
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
}
