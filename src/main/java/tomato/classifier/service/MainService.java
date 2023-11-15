package tomato.classifier.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tomato.classifier.domain.dto.DiseaseDto;
import tomato.classifier.domain.dto.ResultDto;
import tomato.classifier.domain.entity.Disease;
import tomato.classifier.handler.ex.CustomApiException;
import tomato.classifier.repository.DiseaseRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final DiseaseRepository diseaseRepository;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    @Value("/home/ubuntu/ai/inputImg/target/")
    private String fileDir;

    @Value("http://13.209.186.128:5000/predict")
    private String url;

    public void saveImg(List<MultipartFile> files) throws IOException {
/*
        if(!files.isEmpty()) {
            for(MultipartFile file : files) {
                String filePath = fileDir + file.getOriginalFilename();

                file.transferTo(new File(filePath));
            }
        }

 */
        files.stream()
                .filter(file -> !file.isEmpty())
                .forEach(file -> {
                    String filePath = fileDir + file.getOriginalFilename();

                    try {
                        file.transferTo(new File(filePath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
    }

    public ResultDto predict() throws IOException {

        String response = restTemplate.getForObject(url, String.class);
        ResultDto resultDto = objectMapper.readValue(response, ResultDto.class);

        return resultDto;
    }

    public DiseaseDto getDiseaseInfo(ResultDto resultDto) {

        Disease target = diseaseRepository.findById(resultDto.getName())
                .orElseThrow(() -> new CustomApiException("질병 조회를 실패했습니다."));

        return DiseaseDto.toDto(target, resultDto.getProb());
    }
}
