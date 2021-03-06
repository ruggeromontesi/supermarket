package org.reiz.service.supermarket.impl;

import java.util.Scanner;

import org.reiz.exception.NotEnoughChangeException;
import org.reiz.exception.PayNotAcceptedException;
import org.reiz.exception.SoldOutException;
import org.reiz.model.Product;
import org.reiz.service.cash.CashService;

import org.reiz.service.cash.impl.CashServiceImpl;
import org.reiz.service.supermarket.SupermarketService;
import org.reiz.storage.CashRegister;
import org.reiz.storage.ProductStorage;

public class SupermarketServiceImpl implements SupermarketService {

   private static final SupermarketServiceImpl instance = new SupermarketServiceImpl();

   private String userTypedProduct = "";

   private Product selectedProduct;

   private double dueAmount;

   private boolean isTransactionToBeCanceled = false;

   private ProductStorage productStorage = new ProductStorage();

   private CashRegister cashRegister = CashRegister.getInstance();

   private CashService cashService = CashServiceImpl.getInstance();

   private SupermarketServiceImpl() {
   }

   public static SupermarketServiceImpl getInstance() {
      return  instance;
   }

   public void runSupermarket() {
      productStorage.fillWithProducts(10);
      cashRegister.fillWithFiniteAmountOfBillsAndCoins(50);

      while (true) {
         try {
            executeSteps();

         } catch (NotEnoughChangeException ex) {
            System.out.println("Supermarket doesn't have sufficient change to complete this transaction");
         } catch (SoldOutException ex) {
            System.out.println("The user requested a product which is sold out or does not exist");
         } catch (PayNotAcceptedException ex) {
            System.out.println("The user paid with non-accepted bills or coins");
         }
      }

   }

   private void executeSteps() {
      isTransactionToBeCanceled = false;
      listAllAvailableProducts();
      userSelectProductName();
      if (isTransactionToBeCanceled) {
         return;
      }
      if (!cashService.userInsertsMultipleCoinsOrBillsTillReachingTheDueAmount(dueAmount)) {
         return;
      }
      cashService.provideChangeIfNecessary();
      provideProduct();
   }

   private void provideProduct() {
      System.out.println("Here is your product: " + selectedProduct.getDescription());
      System.out.println("\n\n-----------------");
      selectedProduct.setQuantity(selectedProduct.getQuantity() - 1);
      selectedProduct = null;
   }

   private void userSelectProductName() {
      Scanner in = new Scanner(System.in);
      userTypedProduct = "";
      userTypedProduct = in.nextLine();
      if (verifyIfTransactionHasToBeCanceled(userTypedProduct)) {
         System.out.println("Transaction interrupted on customer's request.");
         return;
      }
      selectedProduct = productStorage.getByDescription(userTypedProduct).orElseThrow(SoldOutException::new);
      if (selectedProduct.getQuantity() == 0) {
         throw new SoldOutException();
      }
      dueAmount = selectedProduct.getPrice();
      System.out.println("You are trying to buy " + selectedProduct.getDescription()
            + ". You need to pay " + dueAmount);
      System.out.println("Provide bill or coin (accepted values: 0.1, 0.5, 1, 2)");

   }

   private void printProductInventory() {
      System.out.print("Product Inventory\n");
      productStorage.getInventory().forEach(product -> System.out.print(product.getDescription()
            + " Quantity: " + product.getQuantity() + "\n"));
   }

   private void listAllAvailableProducts() {
      printProductInventory();
      cashService.printCashInventory();
      System.out.println("Type CANCEL to terminate transaction at any time");
      System.out.println("What would you like to buy? Type in the name of the desired product");
      productStorage.getInventory().forEach(product -> System.out.print(product.getDescription()
            + " (price: " + product.getPrice() + ")  "));
      System.out.println("");

   }

   private boolean verifyIfTransactionHasToBeCanceled(String interruptCommand) {
      if (interruptCommand.equals("CANCEL")) {
         isTransactionToBeCanceled =  true;
      }
      return isTransactionToBeCanceled;
   }

}
