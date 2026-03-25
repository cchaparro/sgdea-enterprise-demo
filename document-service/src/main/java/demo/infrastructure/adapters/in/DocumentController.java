
package demo.infrastructure.adapters.in;

import demo.domain.Document;
import demo.infrastructure.adapters.in.response.DocumentResponse;
import demo.infrastructure.adapters.in.response.DocumentCreatedResponse;
import demo.infrastructure.adapters.in.response.DocumentDownloadResponse;
import demo.infrastructure.api.ApiResponse;
import demo.ports.in.CreateDocumentCommand;
import demo.ports.in.CreateDocumentUseCase;
import demo.ports.in.CreateDocumentVersionUseCase;
import demo.ports.in.DownloadDocumentUseCase;
import demo.ports.in.GetDocumentUseCase;
import demo.ports.in.ListDocumentsUseCase;
import java.util.List;
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

  private final CreateDocumentUseCase createDocumentUseCase;
  private final CreateDocumentVersionUseCase createDocumentVersionUseCase;
  private final GetDocumentUseCase getDocumentUseCase;
  private final ListDocumentsUseCase listDocumentsUseCase;
  private final DownloadDocumentUseCase downloadDocumentUseCase;

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

  @PostMapping("/{id}/versions")
  public ResponseEntity<ApiResponse<DocumentCreatedResponse>> uploadVersion(
      @PathVariable String id,
      @RequestParam MultipartFile file,
      @RequestParam String user,
      @RequestParam(required = false) String estado) throws Exception {
    log.info("Uploading new version for document '{}' by user '{}'", id, user);
    CreateDocumentCommand cmd = CreateDocumentCommand.builder()
        .title(file.getOriginalFilename())
        .owner(user)
        .estado(estado)
        .file(file.getInputStream())
        .build();

    Document document = createDocumentVersionUseCase.createVersion(id, cmd);
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

    return ResponseEntity.ok(ApiResponse.success(response, "Document version uploaded", MDC.get("traceId")));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<DocumentResponse>> getById(
      @PathVariable String id,
      @RequestParam(required = false) String user) {
    Document document = getDocumentUseCase.getById(id, user);
    return ResponseEntity.ok(ApiResponse.success(
        DocumentResponse.from(document),
        "Document fetched",
        MDC.get("traceId")));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<DocumentResponse>>> listByOwner(@RequestParam String owner) {
    List<DocumentResponse> response = listDocumentsUseCase.listByOwner(owner)
        .stream()
        .map(DocumentResponse::from)
        .toList();
    return ResponseEntity.ok(ApiResponse.success(
        response,
        "Documents fetched",
        MDC.get("traceId")));
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<ApiResponse<DocumentDownloadResponse>> download(
      @PathVariable String id,
      @RequestParam String user) {
    String url = downloadDocumentUseCase.download(id, user);
    return ResponseEntity.ok(ApiResponse.success(
        new DocumentDownloadResponse(id, url, "15m"),
        "Presigned download URL generated",
        MDC.get("traceId")));
  }

}
