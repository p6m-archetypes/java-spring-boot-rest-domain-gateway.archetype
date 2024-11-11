package {{ root_package }}.core;

import com.google.protobuf.StringValue;
import {{ root_package }}.core.support.Converters;
{%- for service_key in services -%}
{% set service = services[service_key] %}
import {{ service.root_package }}.api.v1.{{ service['ProjectName'] }};
{%- for entity_key in service.model.entities -%}
{%- set entity = service.model.entities[entity_key] %}
import {{ service.root_package }}.grpc.v1.*;
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
    {% for service_key in services %}
    {% set service = services[service_key] -%}
    final {{ service['ProjectName'] }} {{ service['projectName'] }};
    {%- endfor %}

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Core({%- for service_key in services %}
    {%- set service = services[service_key] %}
        final {{ service['ProjectName'] }} {{ service['projectName'] }}{% if not loop.last %},{% endif %}
    {%- endfor %}
    ) {{'{'}}{%- for service_key in services %}
        {% set service = services[service_key] -%}
        this.{{ service['projectName'] }} = {{ service['projectName'] }};
        {%- endfor %}
    }

{%- for service_key in services -%}
{%- set service = services[service_key] -%}
{%- for entity_key in service.model.entities -%}
{%- set entity = service.model.entities[entity_key] %}

    public {{ entity_key | pascal_case }} {{ entity_key | camel_case }}(String id) {
        Get{{ entity_key | pascal_case }}Response response = {{ entity_key | camel_case }}Service.get{{ entity_key | pascal_case }}(Get{{ entity_key | pascal_case }}Request.newBuilder()
                                                             .setId(id)
                                                             .build());
        return Converters.to{{ entity_key | pascal_case }}(response.get{{ entity_key | pascal_case }}());
    }

    public List<{{ entity_key | pascal_case }}> {{ entity_key | camel_case | pluralize }}() {
        Get{{ entity_key | pascal_case | pluralize }}Request request = Get{{ entity_key | pascal_case | pluralize }}Request.newBuilder()
                                                 .setStartPage(0)
                                                 .setPageSize(10)
                                                 .build();
        Get{{ entity_key | pascal_case | pluralize }}Response response = {{ entity_key | camel_case }}Service.get{{ entity_key | pascal_case | pluralize }}(request);
        return response.get{{ entity_key | pascal_case }}List()
                                   .stream()
                                   .map(Converters::to{{ entity_key | pascal_case }})
                                   .collect(Collectors.toList());
    }

    public {{ entity_key | pascal_case }} create{{ entity_key | pascal_case }}(Create{{ entity_key | pascal_case}}RequestDto {{ entity_key | camel_case }}) {
        Create{{ entity_key | pascal_case }}Response response = {{ entity_key | camel_case }}Service.create{{ entity_key | pascal_case }}({{ entity_key | pascal_case }}Dto.newBuilder()
                                                                 .setName({{ entity_key | camel_case }}.getName())
                                                                 .build());
        return Converters.to{{ entity_key | pascal_case }}(response.get{{ entity_key | pascal_case }}());
    }

    public {{ entity_key | pascal_case }} update{{ entity_key | pascal_case }}(UUID id, Update{{ entity_key | pascal_case}}RequestDto {{ entity_key | camel_case }}) {
        Update{{ entity_key | pascal_case }}Response response = {{ entity_key | camel_case }}Service.update{{ entity_key | pascal_case }}({{ entity_key | pascal_case }}Dto.newBuilder()
                                                                 .setId(StringValue.of(id.toString()))
                                                                 .setName({{ entity_key | camel_case }}.getName())
                                                                 .build());
        return Converters.to{{ entity_key | pascal_case }}(response.get{{ entity_key | pascal_case }}());
    }

    public Boolean delete{{ entity_key | pascal_case }}(UUID id) {
        Delete{{ entity_key | pascal_case }}Response response = {{ entity_key | camel_case }}Service.delete{{ entity_key | pascal_case }}(Delete{{ entity_key | pascal_case }}Request.newBuilder()
                                                                     .setId(id.toString())
                                                                     .build());
        return true;
    }
{%- endfor %}
{%- endfor %}
}
