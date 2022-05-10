package org.reiz.split.product;

import java.util.Scanner;

import org.reiz.exception.NotEnoughChangeException;
import org.reiz.exception.PayNotAcceptedException;
import org.reiz.exception.SoldOutException;
import org.reiz.model.Product;
import org.reiz.split.cash.CashController;
import org.reiz.storage.CashRegister;
import org.reiz.storage.ProductStorage;

public class ProductController {

   private String userTypedProduct = "";

   private Product selectedProduct;

   private double dueAmount;

   private ProductStorage productStorage = new ProductStorage();

   private CashRegister cashRegister = new CashRegister();

   private CashController controller = new CashController(productStorage,cashRegister);

   private void printCashInventory() {
      System.out.println("Cash Inventory");
      cashRegister.getTill().forEach((k,v) -> System.out.println("Value: " + k.getValue() + ", quantity: " + v));
      System.out.println("\n\n-----------------");
   }

   public void manageSupermarketOperation() {
      int counter = 10;

      while (counter-- > 0) {
         try {
            printProductInventoryReimplemented();
            printCashInventory();
            userExtendedProductSelection();

         } catch (NotEnoughChangeException ex) {
            System.out.println("Supermarket doesn't have sufficient change to complete this transaction");
         } catch (SoldOutException ex) {
            System.out.println("The user requested a product which is sold out or does not exist");
         } catch (PayNotAcceptedException ex) {
            System.out.println("The user paid with non-accepted bills or coins");
         }

      }

   }



   public void userExtendedProductSelection() {
      userTypedProduct = "";
      userAtomicProductSelection();
      dueAmount = selectedProduct.getPrice();

      System.out.println("You are trying to buy " + selectedProduct.getDescription()
            + ". You need to pay " + dueAmount);
      System.out.println("Provide bill or coin (accepted values: 0.1, 0.5, 1, 2)");

      //payWholeAmount();
      controller.payWholeAmount(dueAmount);
      System.out.println("Here is your product: " + selectedProduct.getDescription());
      System.out.println("\n\n-----------------");
      selectedProduct.setQuantity(selectedProduct.getQuantity() - 1);
      selectedProduct = null;
   }

   public void userAtomicProductSelection() {
      Scanner in = new Scanner(System.in);
      userTypedProduct = "";
      System.out.println("What would you like to buy? Type in the name of the desired product");
      productStorage.getInventory().forEach(product -> System.out.print(product.getDescription()
            + " (price: " + product.getPrice() + ")  "));
      System.out.println("");
      userTypedProduct = in.nextLine();
      selectedProduct = productStorage.getByDescription(userTypedProduct).orElseThrow(SoldOutException::new);
      if (selectedProduct.getQuantity() == 0) {
         throw new SoldOutException();
      }
   }

   private void printProductInventoryReimplemented() {
      System.out.print("Product Inventory\n");
      productStorage.getInventory().forEach(product -> System.out.print(product.getDescription()
            + " Quantity: " + product.getQuantity() + "\n"));
   }


}
