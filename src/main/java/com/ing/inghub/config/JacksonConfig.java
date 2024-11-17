package com.ing.inghub.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.timeZone(TimeZone.getTimeZone("Europe/Istanbul"));
            SimpleModule module = new SimpleModule();
            module.addSerializer(Instant.class, new StdSerializer<Instant>(Instant.class) {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss xxx")
                        .withZone(ZoneId.of("Europe/Istanbul"));

                @Override
                public void serialize(Instant value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                    gen.writeString(formatter.format(value));
                }
            });
            builder.modules(module);
            builder.featuresToDisable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        };
    }

}