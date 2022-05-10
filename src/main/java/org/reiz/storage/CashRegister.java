package org.reiz.storage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.reiz.model.CashUnit;

public class CashRegister {

   private Map<CashUnit,Integer> till = new TreeMap<>(Comparator.comparingDouble(CashUnit::getValue).reversed());

   public CashRegister() {
      Arrays.stream(CashUnit.values()).sorted().forEach(cashUnit -> till.put(cashUnit, 3));
   }

   public Map<CashUnit, Integer> getTill() {
      return till;
   }

   public double getTotalAmountOfMoney() {
      return till.entrySet().stream().collect(Collectors.summingDouble(e -> e.getKey().getValue() * e.getValue()));
   }
}
