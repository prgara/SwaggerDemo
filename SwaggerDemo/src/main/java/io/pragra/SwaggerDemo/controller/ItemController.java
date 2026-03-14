package io.pragra.SwaggerDemo.controller;

import io.pragra.SwaggerDemo.model.Item;
import io.pragra.SwaggerDemo.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@NoArgsConstructor
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    // Cacheable for create operation isn't necessary as it would only apply for read operations, but you can have it for the result.

    // Create Item
    @Operation(summary = "Create Item", description = "API to create Item", tags = "Create")
    @ApiResponse(responseCode = "201", description = "Item has been created")
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item createdItem = itemService.saveItem(item);
        return ResponseEntity.ok(createdItem);
    }

    // Cache Get All Items (Optional depending on data size)
    @GetMapping
    @Cacheable(value = "itemsCache")  // Cache all items, assuming it doesn't change frequently.
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    // Get Item by ID (Cache the result of individual item fetch)
    @GetMapping("/{id}")
    @Cacheable(value = "itemsCache", key = "#id")  // Cache individual items by ID
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Optional<Item> item = itemService.getItemById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update Item
    @PutMapping("/{id}")
    @CacheEvict(value = "itemsCache", key = "#id")  // Evict the cached item by ID before updating
    @CachePut(value = "itemsCache", key = "#id")  // Re-cache the updated item
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item) {
        Item updatedItem = itemService.updateItem(id, item);
        return updatedItem != null ? ResponseEntity.ok(updatedItem) : ResponseEntity.notFound().build();
    }

    // Delete Item
    @DeleteMapping("/{id}")
    @CacheEvict(value = "itemsCache", key = "#id")  // Evict the cache entry for the deleted item
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        boolean isDeleted = itemService.deleteItem(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
