package ca.bjad.util.cboamount;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ComboAmountFinderTest
{
   static List<ComboFinderElement> smallSampleData = new ArrayList<>();
   
   static 
   {
      smallSampleData.add(new SampleAmountBean(new BigDecimal("1.00")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("10.00")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("5.00")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("9.00")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("15.00")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("1.01")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("2.02")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("3.3")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("1.96")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("5.56")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("100.01")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("26.02")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("39.3")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("12.46")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("54.56")));
   }
   
   @Test
   public void testFoundComboResultNoNullLogic()
   {
      TestingFoundComboResult resultList = new TestingFoundComboResult();
      assertNotNull("Initial List should not be null", resultList.getComboElements());
      
      // Now explictly set to null.
      resultList.setComboElements(null);
      assertNotNull("Even after explict setting of null, List should not be null", resultList.getComboElements());      
   }
   
   @Test
   public void testNoMatches()
   {
      List<FoundComboResultList> results = new ComboFinder(smallSampleData, new BigDecimal("1000.00")).findCombinationsForAmount();
      assertEquals("Searching for 1000 should have no results", 0, results.size());
   }
   
   @Test
   public void testOneMatch()
   {
      List<FoundComboResultList> results = new ComboFinder(smallSampleData, new BigDecimal("12.46")).findCombinationsForAmount();
      assertEquals("Searching for 12.46 should have 1 result.", 1, results.size());
   }
   
   @Test
   public void testTwoMatches()
   {
      List<FoundComboResultList> results = new ComboFinder(smallSampleData, new BigDecimal("10.00")).findCombinationsForAmount();
      assertEquals("Searching for 10.00 should have 2 result.", 2, results.size());
   }
}

class TestingFoundComboResult extends FoundComboResultList
{
   void setComboElements(List<ComboFinderElement> comboElements)
   {
      this.comboElements = comboElements;
   }
}
