package org.reiz.service.supermarket.impl;

import java.util.Scanner;

import org.reiz.service.cash.CashService;
import org.reiz.exception.NotEnoughChangeException;
import org.reiz.exception.PayNotAcceptedException;
import org.reiz.exception.SoldOutException;
import org.reiz.model.Product;
import org.reiz.service.cash.impl.CashServiceImpl;
import org.reiz.service.supermarket.SupermarketService;
import org.reiz.storage.CashRegister;
import org.reiz.storage.ProductStorage;

public class SupermarketServiceImpl implements SupermarketService {

   private String userTypedProduct = "";

   private Product selectedProduct;

   private double dueAmount;

   private ProductStorage productStorage = new ProductStorage();

   private CashRegister cashRegister = new CashRegister(50);

   private CashService controller = new CashServiceImpl(cashRegister);

   public void runSupermarket() {
      int counter = 10;

      while (counter-- > 0) {
         try {
            stepsShouldBe();

         } catch (NotEnoughChangeException ex) {
            System.out.println("Supermarket doesn't have sufficient change to complete this transaction");
         } catch (SoldOutException ex) {
            System.out.println("The user requested a product which is sold out or does not exist");
         } catch (PayNotAcceptedException ex) {
            System.out.println("The user paid with non-accepted bills or coins");
         }
      }

   }

   private void stepsShouldBe() {
      listAllAvailableProducts();
      userSelectProductName();
      controller.userInsertsMultipleCoinsOrBillsTillReachingTheDueAmount(dueAmount);
      controller.provideChangeIfNecessary();
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
      controller.printCashInventory();
      System.out.println("What would you like to buy? Type in the name of the desired product");
      productStorage.getInventory().forEach(product -> System.out.print(product.getDescription()
            + " (price: " + product.getPrice() + ")  "));
      System.out.println("");
   }

}
