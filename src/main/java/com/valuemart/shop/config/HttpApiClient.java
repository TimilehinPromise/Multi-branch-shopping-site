package com.valuemart.shop.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@Slf4j
@RequiredArgsConstructor
public abstract class HttpApiClient {

    private final RestTemplate restTemplate;


    public  <T> T sendRequest(HttpMethod method,
                              String urlPath,
                              Object requestEntity,
                              Map<String, String> headers,
                              ParameterizedTypeReference<T> clazz) {
        String url = urlPath;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        httpHeaders.setAccept(singletonList(APPLICATION_JSON));
        httpHeaders.setAll(headers);

        HttpEntity<?> httpEntity = new HttpEntity<>(requestEntity, httpHeaders);
        System.out.println(httpEntity);
        ResponseEntity<T> responseEntity = this.restTemplate.exchange(
                url,
                method,
                httpEntity,
                clazz);
        log.info(responseEntity.toString());

        log.info(" API {} request to {} is successful",  method.name(), urlPath);
        System.out.println(responseEntity.getBody());
        return responseEntity.getBody();
    }
}
