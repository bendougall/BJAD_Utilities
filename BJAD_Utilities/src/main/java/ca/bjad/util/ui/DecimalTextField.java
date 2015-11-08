package ca.bjad.util.ui;

import java.math.BigDecimal;

/**
 * Text field for swing applications that will
 * allow for users to enter only Integer values
 * into the field. 
 *
 * @author 
 *  Ben Dougall
 */
public class DecimalTextField extends RestrictiveTextField
{
   private static final long serialVersionUID = 548220705280538015L;
   
   /**
    * Default constructor, setting no minimum or maximum 
    * parameters for the field.
    */
   public DecimalTextField()
   {
      this(null, null);
   }
   
   /**
    * Custom constructor that defines the minimum and maximum 
    * parameters for the field.
    *
    * @param minimumValue
    *    The minimum value allowed within the text field.
    * @param maximumValue
    *    The maximum value allowed within the text field.
    */
   public DecimalTextField(BigDecimal minimumValue, BigDecimal maximumValue)
   {      
      setDocument(new NumericFieldDocument(
            minimumValue,
            maximumValue, 
            this, 
            true));
   }
   
   /**
    * Sets the value for the field to display
    * 
    * @param val
    *    The value to display in the field.
    */
   public void setValue(BigDecimal val)
   {
      super.setText(val.toString());
   }
   
   /**
    * Gathers the value from the text field, or null
    * if the text is an invalid integer.
    * 
    * @return
    *    the value from the text field, or null
    *    if the text is an invalid integer.
    */
   public BigDecimal getValue()
   {
      try
      {
         return new BigDecimal(super.getText());
      }
      catch (Exception ex)
      {
         return null;
      }
   }
}
