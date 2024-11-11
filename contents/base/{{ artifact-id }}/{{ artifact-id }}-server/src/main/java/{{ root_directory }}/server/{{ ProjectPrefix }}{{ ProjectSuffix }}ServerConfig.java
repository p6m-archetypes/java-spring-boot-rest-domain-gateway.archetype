package {{ root_package }}.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.Import;
import {{ root_package }}.core.{{ ProjectPrefix }}{{ ProjectSuffix }}CoreConfig;

@SpringBootApplication(exclude = {
        LiquibaseAutoConfiguration.class,
        DataSourceAutoConfiguration.class
})
@Import({
        {{ ProjectPrefix }}{{ ProjectSuffix }}CoreConfig.class,
})
public class {{ ProjectPrefix }}{{ ProjectSuffix }}ServerConfig {

}
