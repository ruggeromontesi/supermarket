package org.reiz.model;

public enum CashUnit {
   TWO(2.0),
   ONE(1.0),
   HALF(0.5),
   TENTH(0.1);
   private double value;

   private CashUnit(double value) {
      this.value = value;
   }

   public double getValue() {
      return value;
   }



}

/*
ZERO_POINT_FIVE{
      double value = 0.5;
      public double getValue() {
         return value;
      }
   },
   ZERO_POINT_TWO{
      double value = 0.2;
      public double getValue() {
         return value;
      }
   },
   ZERO_POINT_ONE{
      double value = 0.1;
      public double getValue() {
         return value;
      }
   }
 */
