package ca.bjad.util.cboamount;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class for finding which combination within a
 * list of elements have an amount equal to the desired
 * amount to search for. 
 * 
 * @author 
 *    Ben Dougall
 * @version
 *    1.0
 */
public final class ComboFinder
{
   private Collection<ComboFinderElement> elements;
   private BigDecimal amountToFind; 
   
   /**
    * Constructor, setting the list of elements to search through and 
    * the amount to find combinations for. 
    * 
    * @param elements
    *    The elements to search through.
    * @param amountToFind
    *    The amount to search for from all the combinations we can find. 
    */
   public ComboFinder(Collection<ComboFinderElement> elements, BigDecimal amountToFind)
   {
      this.elements = elements;
      this.amountToFind = amountToFind;
   }
   
   /**
    * Filters the list of elements to search for into a list with elements
    * whoses amount is less than or equal to the amount being searched for.
    * 
    * @return
    *    The list of elements whose amounts are less than or equal to 
    *    the amount being looked for. 
    */
   private ArrayList<ComboFinderElement> filterListOfElements()
   {
      ArrayList<ComboFinderElement> filteredResults = new ArrayList<>();
      for (ComboFinderElement element : elements)
      {
         if (element.getComboAmount().compareTo(amountToFind) < 1 && element.getComboAmount().compareTo(BigDecimal.ZERO) != 0)
         {
            filteredResults.add(element);
         }
      }
      
      // Sort the list by largest amount first to take advantage of the logic
      // in the verify logic to stop adding once the amount is greater than 
      // the amount we are searching for.
      Collections.sort(filteredResults, new Comparator<ComboFinderElement>()
         {
            @Override
            public int compare(ComboFinderElement o1, ComboFinderElement o2)
            {
               return o1.getComboAmount().compareTo(o2.getComboAmount()) * -1;
            }
         });;
         
      return filteredResults;
   }
   
   /**
    * Converts the big integer number to be a binary string whose length
    * padded to the number of elements to search for. 
    *  
    * @param val
    *    The value to convert into the binary string.
    * @param paddingCount
    *    The number of characters to pad the string to.
    * @return
    *    The padded binary string.
    */
   private String getBinaryString(BigInteger val, int paddingCount)
   {
      String returnVal = val.toString(2);
      while (returnVal.length() < paddingCount)
      {
         returnVal = "0" + returnVal;
      }      
      return returnVal;
   }
   
   /**
    * Checks to see if the amount summed up by the indexes of "1"s in the 
    * binary string equals the amount being searched for. 
    * 
    * @param results
    *    The filtered set of results.
    * @param possibilityBString
    *    The binary string to determine which indexes needs to be added 
    *    together. 
    * @return
    *    Null if the sum of the 1s in the binary string does not 
    *    equal the amount being searched for. If the amount matches, the
    *    FoundComboResultList is returned with the list of elements
    *    that added up to the total.
    */
   private FoundComboResultList verifyPossiblility(List<ComboFinderElement> results, String possibilityBString)
   {
      FoundComboResultList result = new FoundComboResultList();
      BigDecimal total = BigDecimal.ZERO;
      // Go through all the characters in the binary string.
      for (int charIndex = 0; charIndex != possibilityBString.length(); ++charIndex)
      {
         // If the current character equals 1, count the amount towards the total.
         if (possibilityBString.charAt(charIndex) == '1')
         {
            ComboFinderElement ele = results.get(charIndex);
            
            result.comboElements.add(ele);
            total = total.add(ele.getComboAmount());
            
            // The total is greater than the amount to find, no need to keep looking 
            // at the number. 
            if (total.compareTo(amountToFind) == 1)
            {
               result = null;
               break;
            }
         }
      }
      
      // Hey, a match, return the list generated when finding the total. 
      if (total.compareTo(amountToFind) == 0)
      {
         return result;
      }
      // No match, return null.
      else
      {
         return null;
      }
   }
   
   /**
    * Determines all the possible combinations of elements will result in the 
    * amount being looked for. 
    * 
    * @return
    *    The list of found combination results from the list of elements
    *    that add up to the amount we are looking for.  Empty list is returned
    *    if no combinations are found.
    */
   public List<FoundComboResultList> findCombinationsForAmount()
   {
      List<FoundComboResultList> results = new ArrayList<>();
      
      // Filter out any elements whose amount is higher than the amount we are looking for
      List<ComboFinderElement> filteredResults = filterListOfElements();
      
      // If there is nothing to access, return the empty result set.
      if (filteredResults.isEmpty())
      {
         return results;
      }
      
      // Determine the number of combination possibilities that need to be looked at (2 to 
      // the power of the filtered element count.
      BigInteger possibilityCount = new BigInteger("2").pow(filteredResults.size());
      
      // Start at 1, as 0 will never match.
      BigInteger possibilityIndex = new BigInteger("1");
      
      while (possibilityIndex.compareTo(possibilityCount) < 0)
      {
         String bString = getBinaryString(possibilityIndex, filteredResults.size());
         FoundComboResultList result = verifyPossiblility(filteredResults, bString);
         
         if (result != null)
         {
            results.add(result);
         }
         
         // Increment the index by one. 
         possibilityIndex = possibilityIndex.add(BigInteger.ONE);
      }
      
      return results;
   }
}
