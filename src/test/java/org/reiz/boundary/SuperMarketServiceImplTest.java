package org.reiz.boundary;

import org.junit.Before;
import org.junit.Test;
import org.reiz.controller.SupermarketController;

public class SuperMarketServiceImplTest {

   private SupermarketController controller;

   @Before
   public void initialize() {
      controller = new SupermarketController();
   }

   @Test
   public void testPrintInitialProductInventory() {
      //controller.printProductInventoryReimplemented();
   }

   @Test
   public void testInitialCashInventory() {
      //controller.printCashInventory();
   }

   @Test
   public void testuserSelectProduct() {
      //controller.printCashInventory();
   }

   @Test
   public void testUserSelectProduct() {
      controller.userExtendedProductSelection();
   }


}
