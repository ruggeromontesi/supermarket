package org.reiz.split.cash;

import org.junit.Before;
import org.reiz.service.cash.impl.CashServiceImpl;

public class CashControllerTest {

   private CashServiceImpl cashController;
   @Before
   public void atStart() {
      cashController = CashServiceImpl.getInstance();
   }

   //
}
