package org.reiz.storage;

import org.junit.Assert;
import org.junit.Test;

public class CashRegisterTest {

   @Test
   public void testGetTotalAmountOfMoney( ){
      CashRegister cashRegister = new CashRegister();
      Assert.assertEquals(180.0,cashRegister.getTotalAmountOfMoney(), 0.00001 );
   }
}
