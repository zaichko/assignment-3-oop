package kz.aitu.digitalcontent.service;

import kz.aitu.digitalcontent.exception.InvalidInputException;
import kz.aitu.digitalcontent.exception.ResourceNotFoundException;
import kz.aitu.digitalcontent.model.Purchase;
import kz.aitu.digitalcontent.repository.DigitalContentRepository;
import kz.aitu.digitalcontent.repository.PurchaseRepository;
import kz.aitu.digitalcontent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final DigitalContentRepository contentRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository,
                           UserRepository userRepository,
                           DigitalContentRepository contentRepository) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.contentRepository = contentRepository;
    }

    public Purchase createPurchase(Purchase purchase) {
        if (purchase == null) {
            throw new InvalidInputException("Purchase cannot be null");
        }

        purchase.validate();

        // Validate foreign keys
        if (!userRepository.exists(purchase.getUserId())) {
            throw new ResourceNotFoundException("User", purchase.getUserId());
        }

        if (!contentRepository.exists(purchase.getContentId())) {
            throw new ResourceNotFoundException("DigitalContent", purchase.getContentId());
        }

        return purchaseRepository.create(purchase);
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.getAll();
    }

    public Purchase getPurchaseById(int id) {
        return purchaseRepository.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase", id));
    }

    public List<Purchase> getPurchasesByUserId(int userId) {
        if (!userRepository.exists(userId)) {
            throw new ResourceNotFoundException("User", userId);
        }
        return purchaseRepository.findByUserId(userId);
    }

    public boolean deletePurchase(int id) {
        if (!purchaseRepository.exists(id)) {
            throw new ResourceNotFoundException("Purchase", id);
        }

        return purchaseRepository.delete(id);
    }
}