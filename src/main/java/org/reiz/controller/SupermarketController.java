package org.reiz.controller;

import java.util.Scanner;

import org.reiz.exception.SoldOutException;
import org.reiz.model.Product;
import org.reiz.storage.CashRegister;
import org.reiz.storage.ProductStorage;

public class SupermarketController {

   public void printProductInventory(ProductStorage productStorage) {
      System.out.print("Product Inventory\n");
      productStorage.getInventory().forEach(product -> System.out.print(product.getDescription()
            + " Quantity: " + product.getQuantity() + "\n"));
   }

   public void printCashInventory(CashRegister cashRegister) {
      System.out.println("Product Inventory");
      cashRegister.getTill().forEach((k,v) -> System.out.println("Value: " + k.getValue() + ", quantity: " + v));
   }

   public void userSelectProduct(ProductStorage productStorage) {
      Scanner in = new Scanner(System.in);
      String userTypedProduct  = "";
      String finalUserTypedProduct = userTypedProduct;
      do {
         System.out.println("What would you like to buy? Type in the name of the desired product");
         productStorage.getInventory().forEach(product -> System.out.print(product.getDescription()
               + " (price: " + product.getPrice() + ")  "));
         System.out.println("");
         userTypedProduct = in.nextLine();

      }  while (productStorage.getInventory().stream().anyMatch(product -> product.getDescription().equals(finalUserTypedProduct)));
      Product selectedProduct = productStorage.getByDescription(userTypedProduct).orElseThrow(RuntimeException::new);

      System.out.println("You are trying to buy " + selectedProduct.getDescription() + ". You need to pay " + selectedProduct.getPrice());
      System.out.println("Provide bill or coin (accepted values: 0.1, 0.5, 1, 2)");

   }




}
