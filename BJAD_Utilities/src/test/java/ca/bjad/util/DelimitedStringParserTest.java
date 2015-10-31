package ca.bjad.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test cases for the Delimited String Parser 
 * object.
 *
 * @author 
 *  Ben Dougall
 */
public class DelimitedStringParserTest
{
   private static DelimitedStringParser parser = new DelimitedStringParser
         ("\"fir,st\",second,,", ',');

   /**
    * Ensure the number of sections method works.
    */
   @Test
   public void testNumberOfSections()
   {
      assertEquals("Number of sections", 4, parser.getNumberOfSections());
   }
   
   /**
    * Ensure each of the sections come out correctly.
    */
   @Test
   public void testSections()
   {
      assertEquals("Section 1", "fir,st", parser.section(0));
      assertEquals("Section 2", "second", parser.section(1));
      assertEquals("Section 3 empty?", true, parser.section(2).isEmpty());
      assertEquals("Section 4 (tail end) empty?", true, parser.section(3).isEmpty());
   }
}
