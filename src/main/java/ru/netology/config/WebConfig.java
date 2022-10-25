package ru.netology.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
public class WebConfig {
  @Bean
  public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
    final var bean = new RequestMappingHandlerAdapter();
    bean.getMessageConverters().add(new GsonHttpMessageConverter(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()));

    return bean;
  }
}
