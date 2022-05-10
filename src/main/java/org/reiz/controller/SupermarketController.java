package org.reiz.controller;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.reiz.model.CashUnit;
import org.reiz.model.Product;
import org.reiz.storage.CashRegister;
import org.reiz.storage.ProductStorage;

public class SupermarketController {

   private double paidAmountDuringAtomicIteration = 0;
   private double cumulatedPaidAmount = 0;
   private double yetToBePaidAmount = 0;
   private double dueAmount = 2.3;
   private Map<CashUnit,Integer> currentPaymentBufferMap = getEmptyCashUnitTable();
   private Map<CashUnit,Integer> billCoinsCount = getEmptyCashUnitTable();
   private Map<CashUnit,Integer> changeCashUnitTable = getEmptyCashUnitTable();
   private CashRegister cashRegister = new CashRegister();
   private double dueChange;



   public void printProductInventory(ProductStorage productStorage) {
      System.out.print("Product Inventory\n");
      productStorage.getInventory().forEach(product -> System.out.print(product.getDescription()
            + " Quantity: " + product.getQuantity() + "\n"));
   }

   public void printCashInventory(CashRegister cashRegister) {
      System.out.println("Product Inventory");
      cashRegister.getTill().forEach((k,v) -> System.out.println("Value: " + k.getValue() + ", quantity: " + v));
   }

   public void printCashInventoryReimplemented() {
      System.out.println("Product Inventory");
      cashRegister.getTill().forEach((k,v) -> System.out.println("Value: " + k.getValue() + ", quantity: " + v));
   }

   public void userSelectProduct(ProductStorage productStorage) {
      Scanner in = new Scanner(System.in);
      String userTypedProduct  = "";
      String finalUserTypedProduct = userTypedProduct;
      do {
         System.out.println("What would you like to buy? Type in the name of the desired product");
         productStorage.getInventory().forEach(product -> System.out.print(product.getDescription()
               + " (price: " + product.getPrice() + ")  "));
         System.out.println("");
         userTypedProduct = in.nextLine();

      }  while (productStorage.getInventory().stream().anyMatch(product -> product.getDescription().equals(finalUserTypedProduct)));
      Product selectedProduct = productStorage.getByDescription(userTypedProduct).orElseThrow(RuntimeException::new);

   }

   public void managePurchaseTransaction(Product selectedProduct) {
      System.out.println("You are trying to buy " + selectedProduct.getDescription()
            + ". You need to pay " + selectedProduct.getPrice());
      System.out.println("Provide bill or coin (accepted values: 0.1, 0.5, 1, 2)");
      double change = -1;
   }

   public void payWholeAmount(double dueAmount, CashRegister cashRegister) {
      double paidAmountDuringAtomicIteration = 0;
      double cumulatedPaidAmount = 0;
      double yetToBePaidAmount = dueAmount;

      Map<CashUnit,Integer> currentPaymentBufferMap = getEmptyCashUnitTable();

      do {
         paidAmountDuringAtomicIteration = payArbitraryAmount(currentPaymentBufferMap);

         yetToBePaidAmount = ((double)Math.round(10 * (yetToBePaidAmount - paidAmountDuringAtomicIteration))) / 10;
         cumulatedPaidAmount += paidAmountDuringAtomicIteration;

         askAgainToPayIfDueAmountIsNotZero(yetToBePaidAmount, paidAmountDuringAtomicIteration);

      } while (yetToBePaidAmount > 0);

      if (yetToBePaidAmount != 0) {
         System.out.println("You paid " + cumulatedPaidAmount + " in total. Your change will be " + (-yetToBePaidAmount));
         System.out.println("Here is your change");
         printChange(returnChange(-yetToBePaidAmount,cashRegister));
      }

      putCoinsAndBillsIntoTill(cashRegister,currentPaymentBufferMap);
      printCashInventory(cashRegister);
   }





   public void payWholeAmountReimplemented() {
      yetToBePaidAmount = dueAmount;
      processPaymentTillReachingTheDueAmountReimplemented();
      if (yetToBePaidAmount != 0) {
         System.out.println("You paid " + cumulatedPaidAmount + " in total. Your change will be " + (-yetToBePaidAmount));
         System.out.println("Here is your change");
         returnChangeReimplemented();
         printChangeReimplemented();
      }
      putCoinsAndBillsIntoTillReimplemented();
      printCashInventoryReimplemented();
   }


   private void processPaymentTillReachingTheDueAmountReimplemented() {
      paidAmountDuringAtomicIteration = 0;
      do {
         payAtomicAmountReimplemented();

         yetToBePaidAmount = ((double)Math.round(10 * (yetToBePaidAmount - paidAmountDuringAtomicIteration))) / 10;
         cumulatedPaidAmount += paidAmountDuringAtomicIteration;
         askAgainToPayIfDueAmountIsNotZeroReimplemented();
      } while (yetToBePaidAmount > 0);

   }



   private void handleChange(double yetToBePaidAmount){

   }

   private void askAgainToPayIfDueAmountIsNotZero(double yetToBePaidAmount, double paidAmount) {
      if (yetToBePaidAmount > 0) {
         System.out.println("You paid " + paidAmount + " in total. You still need to pay " + yetToBePaidAmount);
         System.out.println("Provide bill or coin (accepted values: 0.1, 0.5, 1, 2)");
      }
   }

   private void askAgainToPayIfDueAmountIsNotZeroReimplemented() {
      if (yetToBePaidAmount > 0) {
         System.out.println("You paid " + cumulatedPaidAmount + " in total. You still need to pay " + yetToBePaidAmount);
         System.out.println("Provide bill or coin (accepted values: 0.1, 0.5, 1, 2)");
      }
   }




   private void printChange(Map<CashUnit,Integer> changeCashUnitTable) {
      changeCashUnitTable.entrySet().stream()
            .sorted((e1,e2) ->  (int) (10 * (e1.getKey().getValue() - e2.getKey().getValue())))
            .filter(e -> e.getValue() > 0).forEach(e -> System.out.println(
                  "Value"
                        + " : " + e.getKey().getValue() + " quantity: " + e.getValue()));
   }

   private void printChangeReimplemented() {
      changeCashUnitTable.entrySet().stream()
            .sorted((e1,e2) ->  (int) (10 * (e1.getKey().getValue() - e2.getKey().getValue())))
            .filter(e -> e.getValue() > 0).forEach(e -> System.out.println(
                  "Value"
                        + " : " + e.getKey().getValue() + " quantity: " + e.getValue()));
   }




   public double payArbitraryAmount(Map<CashUnit,Integer> billCoinsCount) {
      Scanner in = new Scanner(System.in);
      String userPaidAmount;
      double amountFromKeyboard = 0;
      boolean askAgain = true;
      do {
         userPaidAmount  = in.nextLine();
         try {
            amountFromKeyboard = Double.parseDouble(userPaidAmount);
            CashUnit thisCashUnit = CashUnit.valueOf(amountFromKeyboard);
            billCoinsCount.put(thisCashUnit, billCoinsCount.get(thisCashUnit) + 1);
            askAgain = false;

         } catch (NullPointerException | NumberFormatException ignored) {
         }
      } while (askAgain);

      return amountFromKeyboard;

   }



   public double payAtomicAmountReimplemented() {
      Scanner in = new Scanner(System.in);
      String userPaidAmount;
      double amountFromKeyboard = 0;
      boolean askAgain = true;
      do {
         userPaidAmount  = in.nextLine();
         try {
            amountFromKeyboard = Double.parseDouble(userPaidAmount);
            CashUnit thisCashUnit = CashUnit.valueOf(amountFromKeyboard);
            billCoinsCount.put(thisCashUnit, billCoinsCount.get(thisCashUnit) + 1);
            askAgain = false;

         } catch (NullPointerException | NumberFormatException ignored) {
         }
      } while (askAgain);

      paidAmountDuringAtomicIteration = amountFromKeyboard;

      return amountFromKeyboard;

   }
   public Map<CashUnit,Integer> returnChange(double dueChange, CashRegister cashRegister) {
      Map<CashUnit,Integer> changeCashUnitTable = getEmptyCashUnitTable();

      do {
         double finalDueChange = dueChange;
         CashUnit firstCashUnit =
               Arrays.stream(CashUnit.values()).sorted(Comparator.comparingDouble(CashUnit::getValue).reversed())
                     .filter(cashUnit -> cashUnit.getValue() <= finalDueChange).findFirst().get();
         changeCashUnitTable.merge(firstCashUnit, 1 ,Integer::sum);
         dueChange = ((double)Math.round(10 * (dueChange - firstCashUnit.getValue()))) / 10;

      } while (dueChange > 0.01);

      Arrays.stream(CashUnit.values()).forEach(cashUnit -> {
         cashRegister.getTill().merge(cashUnit, changeCashUnitTable.get(cashUnit), (i1,i2) -> i1 - i2);
      });
      return changeCashUnitTable;
   }


   public void returnChangeReimplemented() {
      dueChange = -yetToBePaidAmount;

      do {
         double finalDueChange = dueChange;
         CashUnit firstCashUnit =
               Arrays.stream(CashUnit.values()).sorted(Comparator.comparingDouble(CashUnit::getValue).reversed())
                     .filter(cashUnit -> cashUnit.getValue() <= finalDueChange).findFirst().get();
         changeCashUnitTable.merge(firstCashUnit, 1 ,Integer::sum);
         dueChange = ((double)Math.round(10 * (dueChange - firstCashUnit.getValue()))) / 10;

      } while (dueChange > 0.01);

      Arrays.stream(CashUnit.values()).forEach(cashUnit -> {
         cashRegister.getTill().merge(cashUnit, changeCashUnitTable.get(cashUnit), (i1,i2) -> i1 - i2);
      });

   }



   public static Map<CashUnit,Integer> getEmptyCashUnitTable() {
      Map<CashUnit,Integer> emptyCashUnitTable = new TreeMap<>(Comparator.comparingDouble(CashUnit::getValue).reversed());
      Arrays.stream(CashUnit.values()).forEach(cashUnit -> emptyCashUnitTable.put(cashUnit,0));
      return emptyCashUnitTable;
   }

   private void putCoinsAndBillsIntoTill(CashRegister cashRegister, Map<CashUnit,Integer> currentPaymentBufferMap) {
      Arrays.stream(CashUnit.values()).forEach(cashUnit -> {
         cashRegister.getTill().merge(cashUnit,currentPaymentBufferMap.get(cashUnit), Integer::sum);
      });

   }

   private void putCoinsAndBillsIntoTillReimplemented() {
      Arrays.stream(CashUnit.values()).forEach(cashUnit -> {
         cashRegister.getTill().merge(cashUnit,currentPaymentBufferMap.get(cashUnit), Integer::sum);
      });

   }







}
