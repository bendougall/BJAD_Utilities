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
@SuppressWarnings("javadoc")
public class DelimitedStringParserTest
{
   private static DelimitedStringParser parser = new DelimitedStringParser
         ("a,\"fir,st\",second,,3", ',');

   @Test
   public void testNumberOfSections()
   {
      assertEquals("Number of sections", 5, parser.getNumberOfSections());
   }

   @Test
   public void testSections()
   {
      assertEquals("Section 2", "fir,st", parser.section(1));
      assertEquals("Section 3", "second", parser.section(2));
      assertEquals("Section 4 empty?", true, parser.section(3).isEmpty());
   }
   
   @Test
   public void testSectionsAgain()
   {
      DelimitedStringParser parser = new DelimitedStringParser("a,\"fir,st\",second,,", ',');
      assertEquals("Number of sections", 5, parser.getNumberOfSections());
      
   }
}
