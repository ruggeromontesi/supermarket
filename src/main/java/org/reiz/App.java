package org.reiz;

import org.reiz.controller.SupermarketController;
import org.reiz.storage.ProductStorage;

public class App {
   public static void main(String[] args) {
      System.out.println("Hello World!");
   }

   static void testUserSelectProduct() {
      SupermarketController controller = new SupermarketController();
      ProductStorage productStorage = new ProductStorage();
      //controller.userSelectProduct(productStorage);

   }

   static void printEmptyCashUnitMap() {
      System.out.println(SupermarketController.getEmptyCashUnitTable());

   }


}
