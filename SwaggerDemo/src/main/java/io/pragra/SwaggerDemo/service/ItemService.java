package io.pragra.SwaggerDemo.service;

import io.pragra.SwaggerDemo.model.Item;

import io.pragra.SwaggerDemo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    // Create or Update
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    // Read all items
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // Read by ID
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    // Update item
    @Transactional(propagation = Propagation.REQUIRED)
    public Item updateItem(Long id, Item item) {
        Optional<Item> existingItem = getItemById(id);
        if (existingItem.isPresent()) {
            Item updatedItem = existingItem.get();
            updatedItem.setName(item.getName());
            updatedItem.setDescription(item.getDescription());
            return itemRepository.save(updatedItem);  // Save the updated item to DB
        }
        return null;  // Item not found
    }

    // Delete item
    public boolean deleteItem(Long id) {
        Optional<Item> existingItem = getItemById(id);
        if (existingItem.isPresent()) {
            itemRepository.deleteById(id);
            return true;  // Item was deleted
        }
        return false;  // Item not found
    }
}
