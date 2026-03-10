package org.fn.persistence.json;

import org.fn.core.common.Const;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalTimeSerializer;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
public class JacksonConfig {
    @Bean
    public JsonMapperBuilderCustomizer localDateTimeCustomizer() {
        return builder -> {
            SimpleModule module = new SimpleModule();

            module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(Const.Datetime.DATETIME));
            module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(Const.Datetime.DATETIME));
            module.addSerializer(LocalDate.class, new LocalDateSerializer(Const.Datetime.DATE));
            module.addDeserializer(LocalDate.class, new LocalDateDeserializer(Const.Datetime.DATE));
            module.addSerializer(LocalTime.class, new LocalTimeSerializer(Const.Datetime.TIME));
            module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(Const.Datetime.TIME));

            builder.addModule(module);
        };
    }
}