package org.reiz.boundary;

import org.junit.Before;
import org.junit.Test;
import org.reiz.controller.SupermarketController;
import org.reiz.storage.CashRegister;
import org.reiz.storage.ProductStorage;

public class SuperMarketServiceImplTest {

   private SupermarketController controller;

   @Before
   public void initialize() {
      controller = new SupermarketController();
   }

   @Test
   public void testPrintInitialProductInventory() {
      ProductStorage productStorage = new ProductStorage();
      controller.printProductInventory(productStorage);
   }

   @Test
   public void testInitialCashInventory() {
      CashRegister cashRegister = new CashRegister();
      controller.printCashInventory(cashRegister);
   }

   @Test
   public void testuserSelectProduct() {
      ProductStorage productStorage = new ProductStorage();
      controller.userSelectProduct(productStorage);

   }

   public void testUserSelectProduct() {
      ProductStorage productStorage = new ProductStorage();
      controller.userSelectProduct(productStorage);

   }
}
