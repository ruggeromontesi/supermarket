package org.reiz.storage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.reiz.model.CashUnit;

public class CashRegister {

   private static final CashRegister instance = new CashRegister();

   private final Map<CashUnit,Integer> till = new TreeMap<>(Comparator.comparingDouble(CashUnit::getValue).reversed());

   private CashRegister() {
   }

   public static  CashRegister getInstance() {
      return instance;
   }

   public Map<CashUnit, Integer> getTill() {
      return till;
   }

   public void fillWithFiniteAmountOfBillsAndCoins(int amount) {
      Arrays.stream(CashUnit.values()).sorted().forEach(cashUnit -> till.put(cashUnit, amount));
   }
}
