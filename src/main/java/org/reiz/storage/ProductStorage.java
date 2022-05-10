package org.reiz.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.reiz.model.Product;

public class ProductStorage {

   public List<Product> inventory = new ArrayList<>();

   public ProductStorage() {
      inventory.addAll(Arrays.asList(
            new Product("SODA", 2.3, 10),
            new Product("BREAD", 1.1, 10),
            new Product("WINE", 2.7,10)
      ));

   }

   public List<Product> getInventory() {
      return inventory;
   }

   public Optional<Product> getByDescription(String description) {
      return inventory.stream().filter(product -> product.getDescription().equals(description)).collect(Collectors.collectingAndThen(
            Collectors.toList(),
            list -> {
               if (list.size() > 1) {
                  throw new RuntimeException("More than a product with same description!");
               }
               return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));

            }
      ));

   }

}
