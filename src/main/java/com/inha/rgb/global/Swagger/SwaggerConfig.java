package com.inha.rgb.global.Swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Team RGB " ,
                description = "인하대학교 25-2 캡스톤 디자인 RGB팀 backend swagger 문서입니다.",
                version = "V1.0"
        ))
@Configuration
public class SwaggerConfig {

}
