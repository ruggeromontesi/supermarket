package org.reiz.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.reiz.model.Product;

public class ProductStorage {

   public List<Product> inventory = new ArrayList<>();

   public ProductStorage( ){
      inventory.addAll(Arrays.asList(
            new Product("SODA", 2.3, 10),
            new Product("WINE", 2.7,10),
            new Product("BREAD", 1.1, 10)

      ));

   }

   public List<Product> getInventory() {
      return inventory;
   }

}
