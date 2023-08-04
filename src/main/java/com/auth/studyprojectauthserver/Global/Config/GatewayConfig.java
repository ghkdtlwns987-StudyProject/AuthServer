package com.auth.studyprojectauthserver.Global.Config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class GatewayConfig {
    @Value("${third.url}")
    public String thirdUrl;
}
