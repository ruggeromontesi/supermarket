package org.reiz.model;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.reiz.exception.PayNotAcceptedException;

public enum CashUnit {
   TWO(2.0),
   ONE(1.0),
   HALF(0.5),
   TENTH(0.1),
   ZERO(0.0);
   private double value;

   private CashUnit(double value) {
      this.value = value;
   }

   public double getValue() {
      return value;
   }

   public static CashUnit valueOf(double value) {
      return Arrays.stream(CashUnit.values()).filter(cashUnit -> cashUnit.getValue() == value).collect(
            Collectors.collectingAndThen(Collectors.toList(),
                  list -> {
                  if (list.size() == 0) {
                     throw new PayNotAcceptedException();
                  }
                  if (list.size() > 1) {
                     throw new RuntimeException("Wrong CashUnit , two different cash units with the same value!");
                  } else {
                     return list.get(0);
                  }
                  }));
   }

}
