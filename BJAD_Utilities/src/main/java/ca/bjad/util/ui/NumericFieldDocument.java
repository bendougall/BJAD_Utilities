package ca.bjad.util.ui;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Numeric value document that will allow the integer 
 * and decimal text fields to filter out invalid
 * input from the user.
 *
 * @author 
 *  Ben Dougall
 */
class NumericFieldDocument extends PlainDocument
{
   private static final long serialVersionUID = -3409138035297440726L;

   private BigDecimal minimumValue;
   private BigDecimal maximumValue;
   private RestrictiveTextField owningField;
   
   private boolean allowDecimals;
   
   /**
    * Constructor, lays out the parameters for the document's filters.
    * 
    * @param minimumValue
    *    The minimum value allowed within the field.
    * @param maximumValue
    *    The maximum value allowed within the field.
    * @param owningField
    *    The field that owns this document implementation.
    * @param allowDecimals
    *    Flag to allow or disallow decimal values in the field.
    */
   public NumericFieldDocument(BigDecimal minimumValue, BigDecimal maximumValue, RestrictiveTextField owningField, boolean allowDecimals)
   {
      super();
      this.minimumValue = minimumValue;
      this.maximumValue = maximumValue;
      this.owningField = owningField;
      this.allowDecimals = allowDecimals;
   }

   /**
    * @param minimumValue 
    *   The minimumValue to set within the NumericFieldDocument instance
    */
   public void setMinimumValue(BigDecimal minimumValue)
   {
      this.minimumValue = minimumValue;
   }

   /**
    * @param maximumValue 
    *   The maximumValue to set within the NumericFieldDocument instance
    */
   public void setMaximumValue(BigDecimal maximumValue)
   {
      this.maximumValue = maximumValue;
   }

   /**
    * Validates the content for the field's potential 
    * new value.
    * 
    * @param newTextValue
    *    The value to verify.
    * @return
    *    The value as a number, or null if the 
    *    text value is invalid.
    */
   public BigDecimal verifyContents(String newTextValue)
   {
      BigDecimal val = null;
      try
      {  
         // If the negative sign is the only text in the field, 
         // treat it as minus one.
         if (newTextValue.equals("-"))
         {
            val = new BigDecimal("-1");
         }
         // if a decimal point is the only thing in the 
         // text field, treat the value as .1
         else if (newTextValue.equals("."))
         {
            if (allowDecimals)
            {
               val = new BigDecimal(".1");
            }
            else 
            {
               // No decimals allowed, not continuing.
               return null;
            }
         }
         // otherwise, lets see if we can convert the new text 
         // value to a BigInteger object.
         else 
         {
            if (allowDecimals)
            {
               val = new BigDecimal(newTextValue);
            }
            else 
            {
               val = new BigDecimal(new BigInteger(newTextValue));
            }
         }
      }
      catch (Exception ex)
      {
         // not a valid numeric value.
         return null;
      }
      
      if (minimumValue != null && minimumValue.compareTo(val) == 1)
      {
         return null;
      }
      
      if (maximumValue != null && maximumValue.compareTo(val) == -1)
      {
         return null;
      }
      
      return val;
   }
   
   @Override
   public void insertString(int offs, String str, AttributeSet a) throws BadLocationException 
   {
      StringBuilder sb = new StringBuilder(owningField.getTextContent());
      sb.insert(offs, str);            
      String newTextValue = sb.toString();
      
      // Empty string, pass it up to the super class to place in the 
      // field.
      if (newTextValue.isEmpty())
      {
         super.insertString(offs, str, a);
      }
      
      BigDecimal val = verifyContents(newTextValue);
      if (val != null)
      {
         super.insertString(offs, str, a);
      }
   }
}
