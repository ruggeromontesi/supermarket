package org.reiz.controller;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.reiz.model.CashUnit;
import org.reiz.storage.CashRegister;
import org.reiz.storage.ProductStorage;

public class SuperMarketControllerTest {

   @Test
   public void testReturnChange() {
      SupermarketController controller = new SupermarketController();
      CashRegister cashRegister = new CashRegister();
      Map<CashUnit,Integer> map =controller.returnChange(0.7,cashRegister );
      Map<CashUnit,Integer> expectedMap  = SupermarketController.getEmptyCashUnitTable();
      expectedMap.put(CashUnit.HALF,1);
      expectedMap.put(CashUnit.TENTH,2);
      Assert.assertEquals(expectedMap,map);
      System.out.println(map);
      controller.printCashInventory(cashRegister);
   }

   @Test
   public void testPayWholeAmount() {
      SupermarketController controller = new SupermarketController();
      ProductStorage productStorage = new ProductStorage();
      System.out.println("You need to pay 2.3\n");
      controller.payWholeAmount(2.3, new CashRegister());

   }
}
