package org.reiz.controller;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.reiz.exception.SoldOutException;
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

   private Map<CashUnit,Integer> changeCashUnitTable = getEmptyCashUnitTable();

   private CashRegister cashRegister = new CashRegister();

   private ProductStorage productStorage = new ProductStorage();

   private double dueChange;

   private String userTypedProduct = "";

   private Product selectedProduct;

   public void manageSupermarketOperation() {
      int counter = 10;

      while (counter-- > 0) {
         printProductInventoryReimplemented();
         printCashInventory();
         userExtendedProductSelection();
      }

   }

   private void printProductInventoryReimplemented() {
      System.out.print("Product Inventory\n");
      productStorage.getInventory().forEach(product -> System.out.print(product.getDescription()
            + " Quantity: " + product.getQuantity() + "\n"));
   }

   private void printCashInventory() {
      System.out.println("Cash Inventory");
      cashRegister.getTill().forEach((k,v) -> System.out.println("Value: " + k.getValue() + ", quantity: " + v));
      System.out.println("\n\n-----------------");
   }

   public void userExtendedProductSelection() {
      userTypedProduct = "";
      userAtomicProductSelection();
      dueAmount = selectedProduct.getPrice();

      System.out.println("You are trying to buy " + selectedProduct.getDescription()
            + ". You need to pay " + dueAmount);
      System.out.println("Provide bill or coin (accepted values: 0.1, 0.5, 1, 2)");

      payWholeAmount();
      System.out.println("Here is your product: " + selectedProduct.getDescription());
      System.out.println("\n\n-----------------");
      selectedProduct.setQuantity(selectedProduct.getQuantity() - 1);
      selectedProduct = null;
   }

   public void userAtomicProductSelection() {
      Scanner in = new Scanner(System.in);
      userTypedProduct = "";

      do {
         System.out.println("What would you like to buy? Type in the name of the desired product");
         productStorage.getInventory().forEach(product -> System.out.print(product.getDescription()
               + " (price: " + product.getPrice() + ")  "));
         System.out.println("");
         userTypedProduct = in.nextLine();

      }  while (!productStorage.getInventory().stream().anyMatch(product -> product.getDescription().equals(userTypedProduct)));
      selectedProduct = productStorage.getByDescription(userTypedProduct).orElseThrow(SoldOutException::new);
      if(selectedProduct.getQuantity() == 0) {
         throw new SoldOutException();
      }
   }

   public void managePurchaseTransaction(Product selectedProduct) {
      System.out.println("You are trying to buy " + selectedProduct.getDescription()
            + ". You need to pay " + selectedProduct.getPrice());
      System.out.println("Provide bill or coin (accepted values: 0.1, 0.5, 1, 2)");
      double change = -1;
   }

   public void payWholeAmount() {
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

   private void handleChange(double yetToBePaidAmount){
   }

   private void askAgainToPayIfDueAmountIsNotZero() {
      if (yetToBePaidAmount > 0) {
         System.out.println("You paid " + cumulatedPaidAmount + " in total. You still need to pay " + yetToBePaidAmount);
         System.out.println("Provide bill or coin (accepted values: 0.1, 0.5, 1, 2)");
      }
   }

   private void printChange() {
      changeCashUnitTable.entrySet().stream()
            .sorted((e1,e2) ->  (int) (10 * (e1.getKey().getValue() - e2.getKey().getValue())))
            .filter(e -> e.getValue() > 0).forEach(e -> System.out.println(
                  "Value"
                        + " : " + e.getKey().getValue() + " quantity: " + e.getValue()));
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

      Arrays.stream(CashUnit.values()).forEach(cashUnit -> {
         cashRegister.getTill().merge(cashUnit, changeCashUnitTable.get(cashUnit), (i1,i2) -> i1 - i2);
      });
   }

   public static Map<CashUnit,Integer> getEmptyCashUnitTable() {
      Map<CashUnit,Integer> emptyCashUnitTable = new TreeMap<>(Comparator.comparingDouble(CashUnit::getValue).reversed());
      Arrays.stream(CashUnit.values()).forEach(cashUnit -> emptyCashUnitTable.put(cashUnit,0));
      return emptyCashUnitTable;
   }

   private void putCoinsAndBillsIntoTill() {
      Arrays.stream(CashUnit.values()).forEach(cashUnit -> {
         cashRegister.getTill().merge(cashUnit,currentPaymentBufferMap.get(cashUnit), Integer::sum);
      });
   }

   private void resetPaymentTransaction() {
      currentPaymentBufferMap = getEmptyCashUnitTable();
      changeCashUnitTable = getEmptyCashUnitTable();
      cumulatedPaidAmount = 0;
      yetToBePaidAmount = 0;
      dueAmount = 0;
   }
}
