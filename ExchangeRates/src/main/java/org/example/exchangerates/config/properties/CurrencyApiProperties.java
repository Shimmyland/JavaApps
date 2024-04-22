package org.example.exchangerates.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "client")
// https://www.baeldung.com/configuration-properties-in-spring-boot
public class CurrencyApiProperties {
    private String baseUrl;
    private String accessKey;
}
