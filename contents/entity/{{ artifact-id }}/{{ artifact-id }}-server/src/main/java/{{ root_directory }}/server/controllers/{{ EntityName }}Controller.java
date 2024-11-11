package {{ root_package }}.server.controllers;

import {{ root_package }}.core.{{ ProjectName }}Core;
import {{ root_package }}.core.dto.Create{{ EntityName }}RequestDto;
import {{ root_package }}.core.dto.{{ EntityName }};
import {{ root_package }}.core.dto.Update{{ EntityName }}RequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/{{ entityName }}")
@Tag(name = "{{ EntityName }} API", description = "{{ EntityName }} API description")
public class {{ EntityName }}Controller {
    private final {{ ProjectName }}Core service;

    public {{ EntityName }}Controller({{ ProjectName }}Core service) {
        this.service = service;
    }

    @Operation(
        summary = "Create a {{ entityName }}",
        description = "Creates a {{ entityName }}",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "{{ EntityName }} information",
            required = true,
            content = @Content(
                schema = @Schema(implementation = Create{{ EntityName }}RequestDto.class),
                examples = {
                    @ExampleObject(
                        name = "Example 1",
                        description = "Create {{ EntityName }}",
                        summary = "Create {{ EntityName }}",
                        value = "{\"name\": \"John Doe\"}"
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "{{ EntityName }} was created successfully",
                content = @Content(
                    schema = @Schema(implementation = {{ EntityName }}.class),
                    examples = {
                        @ExampleObject(
                            name = "Example Response",
                            summary = "Successful response example",
                            value = "{\"id\": \"2b4a096a-9d92-4321-9018-086c33d63eb8\", \"name\": \"John Doe\"}"
                        )
                    }
                )
            )
        }
    )
    @PostMapping
    public ResponseEntity<{{ EntityName }}> create{{ EntityName }}(@RequestBody Create{{ EntityName }}RequestDto request) {
        {{ EntityName }} {{ entityName }} = service.create{{ EntityName }}(request);

        return ResponseEntity.ok({{ entityName }});
    }

    @Operation(summary = "Get all {{ entityName | pluralize }}", description = "Get all {{ entityName | pluralize }}")
    @GetMapping
    public ResponseEntity<List<{{ EntityName }}>> get{{ EntityName | pluralize }}() {
        List<{{ EntityName }}> {{ entityName | pluralize }} = service.{{ entityName | pluralize }}();

        return ResponseEntity.ok({{ entityName | pluralize }});
    }

    @Operation(summary = "Get {{ EntityName }} by Id", description = "Gets {{ entityName }} by given id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "{{ EntityName }} successfully retrieved",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = {{ EntityName }}.class),
                examples = @ExampleObject(value = "{\"id\": \"2b4a096a-9d92-4321-9018-086c33d63eb8\", \"name\": \"{{ EntityName }} Item\"}")
            )
        ),
        @ApiResponse(responseCode = "404", description = "{{ EntityName }} not found by given id",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"error\": \"Not Found\"}")
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<{{ EntityName }}> get{{ EntityName }}(@PathVariable("id")UUID id) {
        {{ EntityName }} {{ entityName }} = service.{{ entityName }}(id.toString());

        if ({{ entityName }} == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok({{ entityName }});
    }

    @Operation(summary = "Update {{ entityName }}", description = "Update {{ entityName }} with specified id")
    @PutMapping("/{id}")
    public ResponseEntity<{{ EntityName }}> update{{ EntityName }}(@PathVariable("id") UUID id, @RequestBody Update{{ EntityName }}RequestDto request) {
        {{ EntityName }} {{ entityName }} = service.update{{ EntityName }}(id, request);

        return ResponseEntity.ok({{ entityName }});
    }

    @Operation(summary = "Delete {{ entityName }}", description = "Delete {{ entityName }} with specified id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted"),
        @ApiResponse(responseCode = "404", description = "{{ EntityName }} not found by given id",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"error\": \"Not Found\"}")
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete{{ EntityName }}(@PathVariable("id") UUID id) {
        service.delete{{ EntityName }}(id);

        return ResponseEntity.noContent()
                             .build();
    }
}

