package kz.aitu.digitalcontent.controller;

import kz.aitu.digitalcontent.model.Purchase;
import kz.aitu.digitalcontent.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin(origins = "*")
public class PurchaseController {

    private final PurchaseService service;

    @Autowired
    public PurchaseController(PurchaseService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        return ResponseEntity.ok(service.getAllPurchases());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable int id) {
        return ResponseEntity.ok(service.getPurchaseById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Purchase>> getPurchasesByUserId(@PathVariable int userId) {
        return ResponseEntity.ok(service.getPurchasesByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@RequestBody Purchase purchase) {
        Purchase created = service.createPurchase(purchase);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable int id) {
        service.deletePurchase(id);
        return ResponseEntity.noContent().build();
    }
}