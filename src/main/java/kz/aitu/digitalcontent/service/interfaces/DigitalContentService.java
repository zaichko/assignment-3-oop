package kz.aitu.digitalcontent.service.interfaces;

import kz.aitu.digitalcontent.model.DigitalContent;
import java.util.List;

public interface DigitalContentService {
    DigitalContent createContent(DigitalContent content);
    List<DigitalContent> getAllContent();
    DigitalContent getContentById(int id);
    DigitalContent updateContent(int id, DigitalContent content);
    boolean deleteContent(int id);
    List<DigitalContent> searchByName(String keyword);
    List<DigitalContent> getAvailableContent();
}