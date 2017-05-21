package ca.bjad.util.cboamount;

import java.math.BigDecimal;

/**
 * Interface for the ComboFinder class to use in order to
 * know how to get the amount from the object from the data
 * object.
 * 
 * @author 
 *    Ben Dougall
 */
public interface ComboFinderElement
{
   public BigDecimal getComboAmount();
}
