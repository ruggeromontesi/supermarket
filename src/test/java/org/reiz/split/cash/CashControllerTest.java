package org.reiz.split.cash;

import org.junit.Before;
import org.reiz.service.cash.impl.CashServiceImpl;
import org.reiz.storage.CashRegister;

public class CashControllerTest {

   private CashServiceImpl cashController;
   @Before
   public void atStart() {
      cashController = new CashServiceImpl(new CashRegister(5));
   }

   //
}
