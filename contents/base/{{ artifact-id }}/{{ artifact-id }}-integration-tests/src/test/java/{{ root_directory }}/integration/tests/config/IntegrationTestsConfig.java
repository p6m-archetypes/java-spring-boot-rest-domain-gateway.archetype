package {{root_package}}.integration.tests.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import {{ root_package }}.server.{{ ProjectPrefix }}{{ ProjectSuffix }}Server;

@Configuration
public class IntegrationTestsConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server {{ projectPrefix }}{{ ProjectSuffix }}Server() {
        return new {{ ProjectPrefix }}{{ ProjectSuffix }}Server()
                .withRandomPorts()
                ;
    }

}
