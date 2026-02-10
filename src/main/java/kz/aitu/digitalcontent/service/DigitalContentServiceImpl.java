package kz.aitu.digitalcontent.service;

import kz.aitu.digitalcontent.exception.InvalidInputException;
import kz.aitu.digitalcontent.exception.ResourceNotFoundException;
import kz.aitu.digitalcontent.model.DigitalContent;
import kz.aitu.digitalcontent.repository.DigitalContentRepository;
import kz.aitu.digitalcontent.service.interfaces.DigitalContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DigitalContentServiceImpl implements DigitalContentService {

    private final DigitalContentRepository repository;

    @Autowired
    public DigitalContentServiceImpl(DigitalContentRepository repository) {
        this.repository = repository;
    }

    @Override
    public DigitalContent createContent(DigitalContent content) {
        // Validation
        if (content == null) {
            throw new InvalidInputException("Content cannot be null");
        }

        content.validate();

        return repository.create(content);
    }

    @Override
    public List<DigitalContent> getAllContent() {
        return repository.getAll();
    }

    @Override
    public DigitalContent getContentById(int id) {
        return repository.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DigitalContent", id));
    }

    @Override
    public DigitalContent updateContent(int id, DigitalContent content) {
        if (!repository.exists(id)) {
            throw new ResourceNotFoundException("DigitalContent", id);
        }

        content.validate();
        return repository.update(id, content);
    }

    @Override
    public boolean deleteContent(int id) {
        if (!repository.exists(id)) {
            throw new ResourceNotFoundException("DigitalContent", id);
        }

        return repository.delete(id);
    }

    @Override
    public List<DigitalContent> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new InvalidInputException("Search keyword cannot be empty");
        }

        return repository.getAll().stream()
                .filter(content -> content.getName().toLowerCase()
                        .contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DigitalContent> getAvailableContent() {
        return repository.getAll().stream()
                .filter(DigitalContent::isAvailable)
                .collect(Collectors.toList());
    }
}