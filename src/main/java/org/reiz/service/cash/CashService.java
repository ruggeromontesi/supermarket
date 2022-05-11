package org.reiz.service.cash;

public interface CashService {

   void provideChangeIfNecessary();

   void printCashInventory();

   boolean userInsertsMultipleCoinsOrBillsTillReachingTheDueAmount(double dueAmount);

   void userInsertsSingleCoinOrBill();
}
