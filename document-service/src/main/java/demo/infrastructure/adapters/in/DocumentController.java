
package demo.infrastructure.adapters.in;

import demo.domain.Document;
import demo.infrastructure.adapters.in.response.DocumentCreatedResponse;
import demo.infrastructure.api.ApiResponse;
import demo.ports.in.CreateDocumentCommand;
import demo.ports.in.CreateDocumentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

  private final  CreateDocumentUseCase createDocumentUseCase;

  @PostMapping("/upload")
  public ResponseEntity<ApiResponse<DocumentCreatedResponse>> upload(
      @RequestParam MultipartFile file,
      @RequestParam String user,
      @RequestParam(required = false) String parentId,
      @RequestParam(required = false) String expedienteId,
      @RequestParam(required = false) String tipoDocumental,
      @RequestParam(defaultValue = "ACTIVO") String estado,
      @RequestParam(defaultValue = "1") Integer version) throws Exception {
    log.info("Uploading document '{}' for user '{}'", file.getOriginalFilename(), user);
    CreateDocumentCommand cmd = CreateDocumentCommand.builder()
        .title(file.getOriginalFilename())
        .owner(user)
        .parentId(parentId)
        .expedienteId(expedienteId)
        .tipoDocumental(tipoDocumental)
        .estado(estado)
        .version(version)
        .file(file.getInputStream())
        .build();

    Document document = createDocumentUseCase.create(cmd);
    DocumentCreatedResponse response = new DocumentCreatedResponse(
        document.getId(),
        document.getTitle(),
        document.getFileUrl(),
        document.getOwner(),
        document.getParentId(),
        document.getExpedienteId(),
        document.getTipoDocumental(),
        document.getEstado(),
        document.getVersion());

    return ResponseEntity.ok(ApiResponse.success(response, "Document uploaded", MDC.get("traceId")));
  }

}
