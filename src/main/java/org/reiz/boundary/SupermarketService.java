package org.reiz.boundary;

import org.reiz.storage.ProductStorage;

public interface SupermarketService {
   void printInitialProductInventory(ProductStorage productStorage);
   void printInitialCashInventory();
}
