package org.reiz.split.cash;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.reiz.exception.NotEnoughChangeException;
import org.reiz.model.CashUnit;
import org.reiz.storage.CashRegister;
import org.reiz.storage.ProductStorage;

public class CashController {

   private ProductStorage productStorage;

   private CashRegister cashRegister;

   private double paidAmountDuringAtomicIteration = 0;

   private double cumulatedPaidAmount = 0;

   private double yetToBePaidAmount = 0;

   private double dueChange;

   private Map<CashUnit,Integer> currentPaymentBufferMap = getEmptyCashUnitTable();

   private Map<CashUnit,Integer> changeCashUnitTable = getEmptyCashUnitTable();

   public CashController(ProductStorage productStorage, CashRegister cashRegister) {
      this.productStorage = productStorage;
      this.cashRegister = cashRegister;
   }

   private void printProductInventoryReimplemented() {
      System.out.print("Product Inventory\n");
      productStorage.getInventory().forEach(product -> System.out.print(product.getDescription()
            + " Quantity: " + product.getQuantity() + "\n"));
   }


   public void payWholeAmount(double dueAmount) {
      yetToBePaidAmount = dueAmount;
      cumulatedPaidAmount = 0;
      processPaymentTillReachingTheDueAmount();
      if (yetToBePaidAmount != 0) {
         System.out.println("You paid " + cumulatedPaidAmount + " in total. Your change will be " + (-yetToBePaidAmount));
         System.out.println("Here is your change");
         returnChange();
         printChange();
      }
      putCoinsAndBillsIntoTill();
      resetPaymentTransaction();
      currentPaymentBufferMap = getEmptyCashUnitTable();
   }

   private void processPaymentTillReachingTheDueAmount() {
      paidAmountDuringAtomicIteration = 0;
      do {
         payAtomicAmount();

         yetToBePaidAmount = ((double)Math.round(10 * (yetToBePaidAmount - paidAmountDuringAtomicIteration))) / 10;
         cumulatedPaidAmount += paidAmountDuringAtomicIteration;
         askAgainToPayIfDueAmountIsNotZero();
      } while (yetToBePaidAmount > 0);

   }

   public double payAtomicAmount() {
      Scanner in = new Scanner(System.in);
      String userPaidAmount;
      double amountFromKeyboard = 0;
      boolean askAgain = true;
      do {
         userPaidAmount  = in.nextLine();
         try {
            amountFromKeyboard = Double.parseDouble(userPaidAmount);
            CashUnit thisCashUnit = CashUnit.valueOf(amountFromKeyboard);
            currentPaymentBufferMap.put(thisCashUnit, currentPaymentBufferMap.get(thisCashUnit) + 1);
            askAgain = false;

         } catch (NullPointerException | NumberFormatException ignored) {
         }
      } while (askAgain);

      paidAmountDuringAtomicIteration = amountFromKeyboard;

      return amountFromKeyboard;

   }

   public static Map<CashUnit,Integer> getEmptyCashUnitTable() {
      Map<CashUnit,Integer> emptyCashUnitTable = new TreeMap<>(Comparator.comparingDouble(CashUnit::getValue).reversed());
      Arrays.stream(CashUnit.values()).forEach(cashUnit -> emptyCashUnitTable.put(cashUnit,0));
      return emptyCashUnitTable;
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
      //dueAmount = 0;
   }

   private void putCoinsAndBillsIntoTill() {
      Arrays.stream(CashUnit.values()).forEach(cashUnit -> {
         cashRegister.getTill().merge(cashUnit,currentPaymentBufferMap.get(cashUnit), Integer::sum);
      });
   }

   public void returnChange() {
      dueChange = -yetToBePaidAmount;

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

   private void printChange() {
      changeCashUnitTable.entrySet().stream()
            .sorted((e1,e2) ->  (int) (10 * (e1.getKey().getValue() - e2.getKey().getValue())))
            .filter(e -> e.getValue() > 0).forEach(e -> System.out.println(
                  "Value"
                        + " : " + e.getKey().getValue() + " quantity: " + e.getValue()));
   }

   private void verifyThatEnoughChangeIsPresent() {
      if (!Arrays.stream(CashUnit.values())
            .allMatch(cashUnit -> (cashRegister.getTill().get(cashUnit) > changeCashUnitTable.get(cashUnit) - 1))) {
         throw new NotEnoughChangeException();
      }
   }



}
