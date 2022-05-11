package org.reiz.service.cash;

public interface CashService {

   void provideChangeIfNecessary();

   void printCashInventory();

   void userInsertsMultipleCoinsOrBillsTillReachingTheDueAmount(double dueAmount);

   void userInsertsSingleCoinOrBill();
}
