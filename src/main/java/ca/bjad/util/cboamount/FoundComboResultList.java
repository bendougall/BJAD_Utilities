package ca.bjad.util.cboamount;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom object to contain the list of elements that match 
 * up to the combination finder's target amount. 
 * 
 * @author 
 *    Ben Dougall
 */
public class FoundComboResultList
{
   /**
    * The list of elements that were found to match the amount 
    * being looked for. 
    */
   protected List<ComboFinderElement> comboElements = new ArrayList<>();
   
   /**
    * Gets the list of elements, initializing the list if needed. 
    * @return
    *    The list of results, will never be null.
    */
   public List<ComboFinderElement> getComboElements()
   {
      if (comboElements == null)
      {
         comboElements = new ArrayList<>();
      }
      return comboElements;
   }
}
