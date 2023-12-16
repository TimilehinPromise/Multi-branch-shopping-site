package com.valuemart.shop.config;


import com.valuemart.shop.domain.service.concretes.StartUpService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StartUpEvent implements ApplicationListener<ApplicationReadyEvent> {
    private final StartUpService startUpService;



//    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        startUpService.loadDefaultData();

    }
}
