package org.reiz.boundary;

import org.junit.Test;
import org.reiz.boundary.impl.SupermarketServiceImpl;
import org.reiz.storage.ProductStorage;

public class SuperMarketServiceImplTest {

   @Test
   public void testPrintInitialProductInventory() {
      ProductStorage productStorage = new ProductStorage();
      SupermarketServiceImpl supermarketService = new SupermarketServiceImpl();
      supermarketService.printInitialProductInventory(productStorage);

   }
}
