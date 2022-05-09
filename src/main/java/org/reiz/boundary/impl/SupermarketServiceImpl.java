package org.reiz.boundary.impl;

import org.reiz.boundary.SupermarketService;
import org.reiz.model.Product;
import org.reiz.storage.ProductStorage;

public class SupermarketServiceImpl implements SupermarketService {

   ProductStorage productStorage;

   @Override
   public void printInitialProductInventory(ProductStorage productStorage) {
      System.out.print("Initial Product Inventory\n");
      productStorage.getInventory().forEach(product -> System.out.print(product.getDescription() + " Quantity: " +product.getQuantity() + "\n"));
   }

   @Override
   public void printInitialCashInventory() {
      System.out.println();

   }
}
