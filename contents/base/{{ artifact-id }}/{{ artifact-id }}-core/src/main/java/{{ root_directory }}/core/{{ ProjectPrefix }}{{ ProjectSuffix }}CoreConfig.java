package {{ root_package }}.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
{% if use-default-service == false %}
{% for service_key in services -%}
{% set service = services[service_key] -%}
import {{ service.root_package }}.client.{{ service['ProjectName'] }}Client;
{% endfor %}{% endif %}

@Configuration
@ComponentScan
public class {{ ProjectPrefix }}{{ ProjectSuffix }}CoreConfig {

    private final Environment env;

    @Autowired
    public {{ ProjectPrefix }}{{ ProjectSuffix }}CoreConfig(final Environment env) {
        this.env = env;
    }
{% if use-default-service == false %}
{%- for service_key in services -%}
{%- set service = services[service_key] -%}
{%- for entity_key in service.model.entities -%}
{%- set entity = service.model.entities[entity_key] %}

    @Bean
    public {{ service["projectName"] | pascal_case }}Client {{ entity_key | camel_case }}ServiceClient() {
        return {{ service["projectName"] | pascal_case }}Client.of(
            env.getRequiredProperty("core.services.{{ service['artifact-id'] }}.host", String.class),
            env.getRequiredProperty("core.services.{{ service['artifact-id'] }}.port", Integer.class)
        );
    }
{%- endfor %}
{%- endfor %}
{% endif %}
}
