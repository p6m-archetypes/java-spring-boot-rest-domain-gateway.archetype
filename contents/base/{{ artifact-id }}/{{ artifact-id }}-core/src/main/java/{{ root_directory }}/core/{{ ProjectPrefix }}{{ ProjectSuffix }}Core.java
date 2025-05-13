{% import "macros/java" as java %}
package {{ root_package }}.core;

import com.google.protobuf.StringValue;
import {{ root_package }}.core.support.Converters;
{%- for service_key in services -%}
{% set service = services[service_key] %}{% if use-default-service == false %}
import {{ service.root_package }}.api.v1.{{ service['ProjectName'] }};{% endif %}
{%- for entity_key in service.model.entities -%}
{%- set entity = service.model.entities[entity_key] %}
{% if use-default-service == false %}
import {{ service.root_package }}.grpc.v1.*;{% endif %}
import {{ root_package }}.core.dto.{{ entity_key | pascal_case }};
import {{ root_package }}.core.dto.Create{{ entity_key | pascal_case }}RequestDto;
import {{ root_package }}.core.dto.Update{{ entity_key | pascal_case }}RequestDto;
{%- endfor %}
{%- endfor %}
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class {{ ProjectPrefix }}{{ ProjectSuffix }}Core {{'{'}}
    {% if use-default-service == false %}
    {% for service_key in services %}
    {% set service = services[service_key] -%}
    final {{ service['ProjectName'] }} {{ service['projectName'] }};
    {%- endfor %}{% endif %}

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Core({% if use-default-service == false %}{%- for service_key in services %}
    {%- set service = services[service_key] %}
        final {{ service['ProjectName'] }} {{ service['projectName'] }}{% if not loop.last %},{% endif %}
    {%- endfor %}{% endif %}
    ) {{'{'}}{% if use-default-service == false %}{%- for service_key in services %}
        {% set service = services[service_key] -%}
        this.{{ service['projectName'] }} = {{ service['projectName'] }};
        {%- endfor %}{% endif %}
    }

    {%- for service_key in services -%}
    {% set service = services[service_key] %}
    {%- for entity_key in service.model.entities -%}
    {%- set entity = service.model.entities[entity_key] %}
    {% if use-default-service == false %}
    {{ java.core_implementation_methods(entity_key, service.model.entities[entity_key], service.model, service) }}
    {% else %}
    {{ java.core_implementation_methods_defaults(entity_key, service.model.entities[entity_key], service.model) }}
    {% endif %}
    {% endfor %}
    {% endfor %}
}
