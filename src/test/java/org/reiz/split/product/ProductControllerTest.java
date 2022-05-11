package org.reiz.split.product;

import org.junit.Test;
import org.reiz.service.supermarket.impl.SupermarketServiceImpl;

public class ProductControllerTest {

   @Test
   public void testManageSupermarketOperation() {
      SupermarketServiceImpl controller = new SupermarketServiceImpl();
      controller.runSupermarket();

   }
}
