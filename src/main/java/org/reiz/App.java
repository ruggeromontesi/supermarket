package org.reiz;

import org.reiz.service.supermarket.impl.SupermarketServiceImpl;

public class App {
   public static void main(String[] args) {

      SupermarketServiceImpl.getInstance().runSupermarket();
   }
}
