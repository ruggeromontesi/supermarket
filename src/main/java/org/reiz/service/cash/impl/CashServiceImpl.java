package org.reiz.service.cash.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.reiz.exception.NotEnoughChangeException;
import org.reiz.exception.PayNotAcceptedException;
import org.reiz.model.CashUnit;
import org.reiz.service.cash.CashService;
import org.reiz.storage.CashRegister;

public class CashServiceImpl implements CashService {

   private static final CashRegister cashRegister = CashRegister.getInstance();

   private static final CashServiceImpl instance = new CashServiceImpl();

   private double amountPaidDuringSingleBillCoinInsertion = 0;

   private double cumulatedPaidAmount = 0;

   private double yetToBePaidAmount = 0;

   private Map<CashUnit,Integer> currentPaymentBufferMap = getEmptyCashUnitTable();

   private Map<CashUnit,Integer> changeCashUnitTable = getEmptyCashUnitTable();

   private CashServiceImpl() {

   }

   public static CashServiceImpl getInstance() {
      return instance;
   }

   public static Map<CashUnit,Integer> getEmptyCashUnitTable() {
      Map<CashUnit,Integer> emptyCashUnitTable = new TreeMap<>(Comparator.comparingDouble(CashUnit::getValue).reversed());
      Arrays.stream(CashUnit.values()).forEach(cashUnit -> emptyCashUnitTable.put(cashUnit,0));
      return emptyCashUnitTable;
   }

   public void provideChangeIfNecessary() {
      if (yetToBePaidAmount != 0) {
         System.out.println("You paid " + cumulatedPaidAmount + " in total. Your change will be " + (-yetToBePaidAmount));
         System.out.println("Here is your change");
         provideChange();
         printChange();
      }
      putCoinsAndBillsIntoTill();
      resetPaymentTransaction();
      currentPaymentBufferMap = getEmptyCashUnitTable();
   }

   public void printCashInventory() {
      System.out.println("Cash Inventory");
      cashRegister.getTill().forEach((k,v) -> System.out.println("Value: " + k.getValue() + ", quantity: " + v));
      System.out.println("\n\n-----------------");
   }

   public void userInsertsMultipleCoinsOrBillsTillReachingTheDueAmount(double dueAmount) {
      yetToBePaidAmount = dueAmount;
      cumulatedPaidAmount = 0;
      amountPaidDuringSingleBillCoinInsertion = 0;
      do {
         userInsertsSingleCoinOrBill();
         yetToBePaidAmount = ((double)Math.round(10 * (yetToBePaidAmount - amountPaidDuringSingleBillCoinInsertion))) / 10;
         cumulatedPaidAmount += amountPaidDuringSingleBillCoinInsertion;
         askAgainToPayIfDueAmountIsNotZero();
      } while (yetToBePaidAmount > 0);
   }

   public void userInsertsSingleCoinOrBill() {
      Scanner in = new Scanner(System.in);
      String userPaidAmount;
      CashUnit thisCashUnit = null;
      userPaidAmount  = in.nextLine();
      try {
         thisCashUnit = CashUnit.valueOf(Double.parseDouble(userPaidAmount));
         currentPaymentBufferMap.put(thisCashUnit, currentPaymentBufferMap.get(thisCashUnit) + 1);

      } catch (NullPointerException | NumberFormatException ex) {
         throw  new PayNotAcceptedException();
      }
      amountPaidDuringSingleBillCoinInsertion =  thisCashUnit != null ? thisCashUnit.getValue() : 0;
   }

   private void askAgainToPayIfDueAmountIsNotZero() {
      if (yetToBePaidAmount > 0) {
         System.out.println("You paid " + cumulatedPaidAmount + " in total. You still need to pay " + yetToBePaidAmount);
         System.out.println("Provide bill or coin (accepted values: 0.1, 0.5, 1, 2)");
      }
   }

   private void resetPaymentTransaction() {
      currentPaymentBufferMap = getEmptyCashUnitTable();
      changeCashUnitTable = getEmptyCashUnitTable();
      cumulatedPaidAmount = 0;
      yetToBePaidAmount = 0;
   }

   private void putCoinsAndBillsIntoTill() {
      Arrays.stream(CashUnit.values()).forEach(cashUnit -> {
         cashRegister.getTill().merge(cashUnit,currentPaymentBufferMap.get(cashUnit), Integer::sum);
      });
   }

   private void printChange() {
      changeCashUnitTable.entrySet().stream()
            .sorted((e1,e2) ->  (int) (10 * (e1.getKey().getValue() - e2.getKey().getValue())))
            .filter(e -> e.getValue() > 0).forEach(e -> System.out.println(
                  "Value"
                        + " : " + e.getKey().getValue() + " quantity: " + e.getValue()));
   }

   private void provideChange() {
      double dueChange = -yetToBePaidAmount;

      do {
         double finalDueChange = dueChange;
         CashUnit firstCashUnit =
               Arrays.stream(CashUnit.values()).sorted(Comparator.comparingDouble(CashUnit::getValue).reversed())
                     .filter(cashUnit -> cashUnit.getValue() <= finalDueChange).findFirst().get();
         changeCashUnitTable.merge(firstCashUnit, 1,Integer::sum);
         dueChange = ((double)Math.round(10 * (dueChange - firstCashUnit.getValue()))) / 10;

      } while (dueChange > 0.01);

      verifyThatEnoughChangeIsPresent();

      Arrays.stream(CashUnit.values()).forEach(cashUnit -> {
         cashRegister.getTill()
               .merge(cashUnit, changeCashUnitTable.get(cashUnit),(i1,i2) -> i1 - i2);
      });
   }

   private void verifyThatEnoughChangeIsPresent() {
      if (!Arrays.stream(CashUnit.values())
            .allMatch(cashUnit -> (cashRegister.getTill().get(cashUnit) > changeCashUnitTable.get(cashUnit) - 1))) {
         throw new NotEnoughChangeException();
      }
   }

}