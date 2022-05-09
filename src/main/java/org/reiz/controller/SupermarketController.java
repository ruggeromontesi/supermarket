package org.reiz.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.reiz.model.CashUnit;
import org.reiz.model.Product;
import org.reiz.storage.CashRegister;
import org.reiz.storage.ProductStorage;

public class SupermarketController {

   public void printProductInventory(ProductStorage productStorage) {
      System.out.print("Product Inventory\n");
      productStorage.getInventory().forEach(product -> System.out.print(product.getDescription()
            + " Quantity: " + product.getQuantity() + "\n"));
   }

   public void printCashInventory(CashRegister cashRegister) {
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
      double paidAmount = 0;
      double yetToBePaidAmount = dueAmount;
      Map<CashUnit,Integer> billCoinsCount = getEmptyCashUnitTable();

      do {
         paidAmount = payArbitraryAmount(billCoinsCount);

         yetToBePaidAmount -= paidAmount;
         yetToBePaidAmount = ((double)Math.round(10*yetToBePaidAmount))/10;
         if (yetToBePaidAmount > 0) {
            System.out.println("You paid " + paidAmount + " in total. You still need to pay " + yetToBePaidAmount);
            System.out.println("Provide bill or coin (accepted values: 0.1, 0.5, 1, 2)");
         }

      } while (yetToBePaidAmount > 0);

      if (yetToBePaidAmount != 0) {
         System.out.println("You paid " + paidAmount + " in total. Your change will be " + (-yetToBePaidAmount));
         System.out.println("Here is your change");
         returnChange(-yetToBePaidAmount).forEach((k,v) -> System.out.println("Value : " + k.getValue() + " quantity: " + v));
      }

      BiFunction<Integer,Integer,Integer> addBillCoin = Integer::sum;
      Arrays.stream(CashUnit.values()).forEach(cashUnit -> {
         cashRegister.getTill().merge(cashUnit,billCoinsCount.get(cashUnit), addBillCoin);
      });

      printCashInventory(cashRegister);
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

   public Map<CashUnit,Integer> returnChange(double dueChange, CashRegister cashRegister) {
      Map<CashUnit,Integer> changeCashUnitTable = getEmptyCashUnitTable();

      do {
         double finalDueChange = dueChange;
         CashUnit firstCashUnit =
               Arrays.stream(CashUnit.values()).sorted(Comparator.comparingDouble(CashUnit::getValue).reversed())
                     .filter(cashUnit -> cashUnit.getValue() <= finalDueChange).findFirst().get();
         changeCashUnitTable.put(firstCashUnit,changeCashUnitTable.get(firstCashUnit) + 1);
         dueChange = dueChange - firstCashUnit.getValue();
         dueChange = ((double)Math.round(10 * dueChange)) / 10;

      } while (dueChange > 0.01);

      //cashRegister.getTill().forEach((cashUnit, integer) ->
      /*Arrays.stream(CashUnit.values()).forEach(cashUnit -> {
         cashRegister.getTill().put(cashUnit, cashRegister.getTill().get(cashUnit) - changeCashUnitTable.get(cashUnit));
      });*/

      BiFunction<Integer,Integer,Integer> removeBillCoin = (i1,i2) -> i1 - i2;

      Arrays.stream(CashUnit.values()).forEach(cashUnit -> {
         cashRegister.getTill().merge(cashUnit, changeCashUnitTable.get(cashUnit), removeBillCoin);
      });



      return changeCashUnitTable;
   }


   public Map<CashUnit,Integer> returnChange(double dueChange) {
      Map<CashUnit,Integer> changeCashUnitTable = getEmptyCashUnitTable();

      do {
         double finalDueChange = dueChange;
         CashUnit firstCashUnit =
               Arrays.stream(CashUnit.values()).sorted(Comparator.comparingDouble(CashUnit::getValue).reversed())
                     .filter(cashUnit -> cashUnit.getValue() <= finalDueChange).findFirst().get();
         changeCashUnitTable.put(firstCashUnit,changeCashUnitTable.get(firstCashUnit) + 1);
         dueChange = dueChange - firstCashUnit.getValue();
         dueChange = ((double)Math.round(10 * dueChange)) / 10;

      } while (dueChange > 0.01);
      return changeCashUnitTable;
   }

   public static Map<CashUnit,Integer> getEmptyCashUnitTable() {
      Map<CashUnit,Integer> emptyCashUnitTable = new TreeMap<>(Comparator.comparingDouble(CashUnit::getValue).reversed());
      Arrays.stream(CashUnit.values()).forEach(cashUnit -> emptyCashUnitTable.put(cashUnit,0));
      return emptyCashUnitTable;
   }
}
