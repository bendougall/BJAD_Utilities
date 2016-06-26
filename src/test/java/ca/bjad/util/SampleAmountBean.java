package ca.bjad.util.cboamount;

import java.math.BigDecimal;

public class SampleAmountBean implements ComboFinderElement
{
   public BigDecimal amount = new BigDecimal("0.00");

   public SampleAmountBean()
   {
      
   }
   
   public SampleAmountBean(BigDecimal amount)
   {
      this.amount = amount;
   }
   
   @Override
   public BigDecimal getComboAmount()
   {
      return this.amount;
   }
   
}
