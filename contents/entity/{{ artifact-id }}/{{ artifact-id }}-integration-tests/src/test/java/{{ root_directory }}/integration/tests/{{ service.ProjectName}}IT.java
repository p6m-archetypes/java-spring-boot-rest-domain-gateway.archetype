package {{ root_package }}.integration.tests;

import com.google.protobuf.StringValue;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import {{ root_package }}.core.dto.Create{{ EntityName }}RequestDto;
import {{ root_package }}.core.dto.{{ EntityName }};
import {{ root_package }}.core.dto.Update{{ EntityName }}RequestDto;{% if use-default-service == false %}
import {{ service.root_package }}.grpc.v1.*;{% endif %}

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.grpcmock.GrpcMock.*;{% if use-default-service == false %}
import static {{ service.root_package }}.grpc.v1.{{ service.ProjectName}}Grpc.*;{% endif %}

public class {{ service.ProjectName }}IT extends BaseIntegrationTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final ParameterizedTypeReference<List<{{ EntityName }}>> {{ ENTITY_NAME }}_LIST_TYPEREF = new ParameterizedTypeReference<List<{{ EntityName }}>>() {
    };

    private final String id   = UUID.randomUUID()
                              .toString();
    private final String name = "name_" + UUID.randomUUID();

    {% if use-default-service == false %}
    @Test
//    @WithJWTUser(userId = USER_ID_STR, identityProvider = IdentityProvider.AUTH0, identityProviderId = "111")
    void test_Get{{ EntityName }}ById() {
        stubGet{{ EntityName }}();

        String baseUrl = "http://localhost:" + {{ projectName }}.getServerPort();

        ResponseEntity<{{ EntityName }}> response = restTemplate.getForEntity(baseUrl + "/api/v1/{{ entityName | pluralize }}/" + id, {{ EntityName }}.class);

        assertThat(response.getStatusCode()
                           .is2xxSuccessful()).isTrue();
        {{ EntityName }} {{ entityName }} = response.getBody();

        assertThat({{ entityName }}.getId().toString()).isEqualTo(id);
        assertThat({{ entityName }}.getName()).isEqualTo(name);
    }

    @Test
    void test_Get{{ EntityName | pluralize }}() {
        stubGet{{ EntityName | pluralize }}();
        String baseUrl = "http://localhost:" + {{ projectName }}.getServerPort();
        ResponseEntity<List<{{ EntityName }}>> response = restTemplate.exchange(baseUrl + "/api/v1/{{ entityName | pluralize }}",
            HttpMethod.GET,
            null,
            {{ ENTITY_NAME }}_LIST_TYPEREF);

        assertThat(response.getStatusCode()
                           .is2xxSuccessful()).isTrue();

        List<{{ EntityName }}> {{ entityName }}s = response.getBody();

        assertThat({{ entityName }}s).hasSize(3);
    }

    @Test
    void test_Create{{ EntityName }}() {
        stubCreate{{ EntityName }}();

        String baseUrl = "http://localhost:" + {{ projectName }}.getServerPort();
        Create{{ EntityName }}RequestDto request = new Create{{ EntityName }}RequestDto();
        request.setName(name);

        ResponseEntity<{{ EntityName }}> response = restTemplate.postForEntity(baseUrl + "/api/v1/{{ entityName | pluralize }}",
            request,
            {{ EntityName }}.class);

        assertThat(response.getStatusCode()
                           .is2xxSuccessful()).isTrue();
        {{ EntityName }} {{ entityName }} = response.getBody();

        assertThat({{ entityName }}.getId().toString()).isEqualTo(id);
        assertThat({{ entityName }}.getName()).isEqualTo(name);

        verifyThat(
            calledMethod(getCreate{{ EntityName }}Method())
                .withStatusOk()
                .withRequest({{ EntityName }}Dto.newBuilder()
                                       .setName(name)
                                       .build()));
    }

    @Test
    void test_Update{{ EntityName }}() {
        stubUpdate{{ EntityName }}();

        String baseUrl = "http://localhost:" + {{ projectName }}.getServerPort();
        Update{{ EntityName }}RequestDto request = new Update{{ EntityName }}RequestDto();
        request.setName(name + "_updated");

        ResponseEntity<{{ EntityName }}> response = restTemplate.exchange(
            baseUrl + "/api/v1/{{ entityName }}/" + id,
            HttpMethod.PUT,
            new HttpEntity<>(request),
            {{ EntityName }}.class);

        assertThat(response.getStatusCode()
                           .is2xxSuccessful()).isTrue();
        {{ EntityName }} {{ entityName }} = response.getBody();

        assertThat({{ entityName }}.getId().toString()).isEqualTo(id);
        assertThat({{ entityName }}.getName()).isEqualTo(name + "_updated");

        verifyThat(
            calledMethod(getUpdate{{ EntityName }}Method())
                .withStatusOk()
                .withRequest({{ EntityName }}Dto.newBuilder()
                                       .setId(StringValue.of(id))
                                       .setName(name + "_updated")
                                       .build()));
    }

    @Test
    void test_Delete{{ EntityName }}() {
        stubDelete{{ EntityName }}();

        String baseUrl = "http://localhost:" + {{ projectName }}.getServerPort();
        Update{{ EntityName }}RequestDto request = new Update{{ EntityName }}RequestDto();
        request.setName(name + "_updated");

        ResponseEntity<Void> response = restTemplate.exchange(
            baseUrl + "/api/v1/{{ entityName | pluralize }}/" + id,
            HttpMethod.DELETE,
            null,
            Void.class);

        assertThat(response.getStatusCode()
                           .is2xxSuccessful()).isTrue();


        verifyThat(
            calledMethod(getDelete{{ EntityName }}Method())
                .withStatusOk()
                .withRequest(Delete{{ EntityName }}Request.newBuilder()
                                                 .setId(id)
                                                 .build()));
    }

    private void stubDelete{{ EntityName }}() {
        stubFor(unaryMethod(getDelete{{ EntityName }}Method()).willReturn(
            Delete{{ EntityName }}Response.newBuilder()
                                 .setMessage("Success")
                                 .build()
        ));

    }

    protected void stubGet{{ EntityName | pluralize }}() {
        stubFor(unaryMethod(getGet{{ EntityName | pluralize }}Method()).willReturn(
            Get{{ EntityName | pluralize }}Response.newBuilder()
                               .addAll{{ EntityName }}(List.of(
                                   {{ entityName }}DtoBuilder().setId(StringValue.of(UUID.randomUUID()
                                                                                .toString()))
                                                      .build(),
                                   {{ entityName }}DtoBuilder().setId(StringValue.of(UUID.randomUUID()
                                                                                .toString()))
                                                      .build(),
                                   {{ entityName }}DtoBuilder().setId(StringValue.of(UUID.randomUUID()
                                                                                .toString()))
                                                      .build()
                               ))
                               .build()
        ));
    }

    protected void stubGet{{ EntityName }}() {
        stubFor(unaryMethod(getGet{{ EntityName }}Method()).willReturn(
            Get{{ EntityName }}Response.newBuilder()
                              .set{{ EntityName }}(
                                  {{ entityName }}DtoBuilder().build()
                              )
                              .build()
        ));
    }

    protected void stubCreate{{ EntityName }}() {
        stubFor(unaryMethod(getCreate{{ EntityName }}Method()).willReturn(
            Create{{ EntityName }}Response.newBuilder()
                                 .set{{ EntityName }}(
                                     {{ entityName }}DtoBuilder().build()
                                 )
                                 .build()
        ));
    }

    protected void stubUpdate{{ EntityName }}() {
        stubFor(unaryMethod(getUpdate{{ EntityName }}Method()).willReturn(
            Update{{ EntityName }}Response.newBuilder()
                                 .set{{ EntityName }}(
                                     {{ entityName }}DtoBuilder().setName(name + "_updated")
                                                        .build()
                                 )
                                 .build()
        ));
    }

    protected {{ EntityName }}Dto.Builder {{ entityName }}DtoBuilder() {
        return {{ EntityName }}Dto.newBuilder()
                         .setId(StringValue.of(id))
                         .setName(name);
    }
    {% endif %}
}
