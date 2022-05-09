package org.reiz;

import java.util.Map;

import org.reiz.controller.SupermarketController;
import org.reiz.model.CashUnit;
import org.reiz.storage.CashRegister;
import org.reiz.storage.ProductStorage;

public class App {
   public static void main(String[] args) {
      System.out.println("Hello World!");
      //testPayArbitraryAmount();
      //printEmptyCashUnitMap();
      testReturnChange();



   }


   static void testPayArbitraryAmount() {
      SupermarketController controller = new SupermarketController();
      ProductStorage productStorage = new ProductStorage();
      System.out.println("You need to pay 2.3\n");
      controller.payWholeAmount(2.3, new CashRegister());

   }

   static void testUserSelectProduct() {
      SupermarketController controller = new SupermarketController();
      ProductStorage productStorage = new ProductStorage();
      controller.userSelectProduct(productStorage);

   }

   static void printEmptyCashUnitMap() {
      System.out.println(SupermarketController.getEmptyCashUnitTable());

   }


   static void testReturnChange() {
      SupermarketController controller = new SupermarketController();
      CashRegister cashRegister = new CashRegister();
      Map<CashUnit,Integer> map =controller.returnChange(0.7,cashRegister );
      System.out.println(map);
      controller.printCashInventory(cashRegister);



   }
}
