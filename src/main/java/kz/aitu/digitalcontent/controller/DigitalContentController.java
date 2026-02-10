package kz.aitu.digitalcontent.controller;

import kz.aitu.digitalcontent.dto.DigitalContentDTO;
import kz.aitu.digitalcontent.model.*;
import kz.aitu.digitalcontent.patterns.DigitalContentFactory;
import kz.aitu.digitalcontent.service.interfaces.DigitalContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/content")
@CrossOrigin(origins = "*")
public class DigitalContentController {

    private final DigitalContentService service;

    @Autowired
    public DigitalContentController(DigitalContentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DigitalContentDTO>> getAllContent() {
        List<DigitalContent> contents = service.getAllContent();
        List<DigitalContentDTO> dtos = contents.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DigitalContentDTO> getContentById(@PathVariable int id) {
        DigitalContent content = service.getContentById(id);
        return ResponseEntity.ok(convertToDTO(content));
    }

    @PostMapping
    public ResponseEntity<DigitalContentDTO> createContent(@RequestBody DigitalContentDTO dto) {
        DigitalContent content = convertToEntity(dto);
        DigitalContent created = service.createContent(content);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DigitalContentDTO> updateContent(
            @PathVariable int id,
            @RequestBody DigitalContentDTO dto) {
        DigitalContent content = convertToEntity(dto);
        DigitalContent updated = service.updateContent(id, content);
        return ResponseEntity.ok(convertToDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable int id) {
        service.deleteContent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<DigitalContentDTO>> searchContent(@RequestParam String keyword) {
        List<DigitalContent> results = service.searchByName(keyword);
        List<DigitalContentDTO> dtos = results.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/available")
    public ResponseEntity<List<DigitalContentDTO>> getAvailableContent() {
        List<DigitalContent> contents = service.getAvailableContent();
        List<DigitalContentDTO> dtos = contents.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private DigitalContentDTO convertToDTO(DigitalContent content) {
        DigitalContentDTO dto = new DigitalContentDTO();
        dto.setId(content.getId());
        dto.setName(content.getName());
        dto.setReleaseYear(content.getReleaseYear());
        dto.setAvailable(content.isAvailable());
        dto.setContentType(content.getEntityType());
        dto.setDescription(content.getDescription());

        if (content.getCreator() != null) {
            dto.setCreatorCountry(content.getCreator().getCountry());
            dto.setCreatorBio(content.getCreator().getBio());
        }

        if (content instanceof Movie) {
            Movie movie = (Movie) content;
            dto.setRentable(movie.isRentable());
            dto.setDurationMinutes(movie.getDurationMinutes());
        } else if (content instanceof MusicAlbum) {
            MusicAlbum album = (MusicAlbum) content;
            dto.setTrackCount(album.getCountTracks());
        }

        return dto;
    }

    private DigitalContent convertToEntity(DigitalContentDTO dto) {
        Creator creator = new Creator(dto.getCreatorCountry(), dto.getCreatorBio());

        DigitalContent content;

        switch (dto.getContentType().toUpperCase()) {
            case "MOVIE":
                content = DigitalContentFactory.createMovie(
                        dto.getId(), dto.getName(), dto.getReleaseYear(),
                        dto.isAvailable(), creator, dto.getDescription(),
                        dto.getRentable() != null ? dto.getRentable() : false,
                        dto.getDurationMinutes() != null ? dto.getDurationMinutes() : 0
                );
                break;

            case "MUSIC_ALBUM":
            case "ALBUM":
                content = DigitalContentFactory.createMusicAlbum(
                        dto.getId(), dto.getName(), dto.getReleaseYear(),
                        dto.isAvailable(), creator, dto.getDescription(),
                        dto.getTrackCount() != null ? dto.getTrackCount() : 0
                );
                break;

            default:
                content = DigitalContentFactory.createContent(
                        "GAME", dto.getId(), dto.getName(), dto.getReleaseYear(),
                        dto.isAvailable(), creator, dto.getDescription()
                );
        }

        return content;
    }
}