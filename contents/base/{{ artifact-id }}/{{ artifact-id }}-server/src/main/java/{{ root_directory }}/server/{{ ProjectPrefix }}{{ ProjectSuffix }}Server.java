package {{ root_package }}.server;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import {{ group-id }}.platform.tracing.TracingInitializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class {{ ProjectPrefix }}{{ ProjectSuffix }}Server {

    static {
        TracingInitializer.initialize();
        if (System.getProperty("logging.config") == null) {
            System.setProperty("logging.config", "classpath:{{ artifact-id}}-server-logback.xml");
        }
    }

    private static final String SPRING_CONFIG_NAME = "spring.config.name";
    private static final String SPRING_APPLICATION_NAME = "spring.application.name";
    private static final String SPRING_JMX_DEFAULT = "spring.jmx.default";
    private static final String SPRING_JMX_ENABLED = "spring.jmx.enabled";
    private static final String APPLICATION_NAME = "{{ artifact-id }}-server";

    private final SpringApplication springApplication;
    private final Properties overrides = new Properties();
    private ConfigurableApplicationContext context;
    private final List<String> arguments = new ArrayList<>();
    private boolean disableJmx = false;

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server() {
        this.springApplication = new SpringApplication({{ ProjectPrefix }}{{ ProjectSuffix }}ServerConfig.class);
        if (System.getProperty("logging-structured") != null
            || System.getenv("logging-structured") != null
            || System.getenv("LOGGING_STRUCTURED") != null
        ) {
            // Don't pollute the log stream with unstructured text.
            springApplication.setBannerMode(Banner.Mode.OFF);
        }
        springApplication.addInitializers(applicationContext -> applicationContext
                .getEnvironment()
                .getPropertySources()
                .addFirst(new PropertiesPropertySource("overrides", overrides)));
    }

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server start() {
        initializeSystemProperties();
        String[] args = new String[arguments.size()];
        args = arguments.toArray(args);
        this.context = springApplication.run(args);
        clearSystemProperties();
        return this;
    }

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server stop() {
        context.close();
        return this;
    }

    private void initializeSystemProperties() {
        System.setProperty(SPRING_CONFIG_NAME, APPLICATION_NAME);
        System.setProperty(SPRING_APPLICATION_NAME, APPLICATION_NAME);
        System.setProperty(SPRING_JMX_DEFAULT, APPLICATION_NAME);
        if (disableJmx) {
            System.setProperty(SPRING_JMX_ENABLED, "false");
        }
    }

    private void clearSystemProperties() {
        System.clearProperty(SPRING_CONFIG_NAME);
        System.clearProperty(SPRING_APPLICATION_NAME);
        System.clearProperty(SPRING_JMX_DEFAULT);
        if (disableJmx) {
            System.clearProperty(SPRING_JMX_ENABLED);
        }
    }

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server disableJmx() {
        this.disableJmx = true;
        return this;
    }

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server withArguments(String... args) {
        arguments.addAll(Arrays.asList(args));
        return this;
    }

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server withProperty(String key, String value) {
        this.overrides.setProperty(key, value);
        return this;
    }

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Server withRandomPorts() {
        return withProperty("server.port", "0")
              .withProperty("management.server.port", "0")
              ;
    }

    public ApplicationContext getContext() {
        if (context == null) {
            throw new RuntimeException("ApplicationContext has not been initialized, yet!");
        }
        return context;
    }

    public static void main(String[] args) {
        new {{ ProjectPrefix }}{{ ProjectSuffix }}Server().withArguments(args).start();
    }

    public int getManagementPort() {
        Integer serverPort = context.getEnvironment().getProperty("local.management.port", Integer.class);
        if (serverPort == null) {
            throw new RuntimeException("Server not started!");
        }
        return serverPort;
    }

    public int getServerPort() {
        Integer serverPort = context.getEnvironment().getProperty("local.server.port", Integer.class);
        if (serverPort == null) {
            throw new RuntimeException("Server not started!");
        }
        return serverPort;
    }
}
