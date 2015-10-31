package ca.bjad.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser utility to parse a string by a delimiter, with or 
 * without quotes surrounding the sections.
 *
 * @author Ben Dougall
 */
public class DelimitedStringParser
{
   private List<String> sections = new ArrayList<>();
   
   /**
    * Creates a delimited string parser object that will
    * separate the string passed by the delimiter provided
    * regardless of the string sections being surrounded 
    * by quotes or not.
    *
    * @param line
    *    The string to parse. 
    *    
    * @param delimiter
    *    The delimiting character that splits the string 
    *    into various sections.
    */
   public DelimitedStringParser(String line, char delimiter)
   {
      boolean quoteFound = false;
      StringBuilder fieldBuilder = new StringBuilder();
      
      for (int i = 0; i < line.length(); i++)
      {
         char c = line.charAt(i);
         fieldBuilder.append(c);
         if (c == '"')
         {
            quoteFound = !quoteFound;
         }
         if ((!quoteFound && c == delimiter) || i + 1 == line.length())
         {
            sections.add(
                  fieldBuilder.toString().replaceAll(delimiter + "$", "")
                     .replaceAll("^\"|\"$", "").
                        replace("\"\"", "\"").trim());
            fieldBuilder.setLength(0);
         }
         if (c == ',' && i + 1 == line.length())
         {
            sections.add("");
         }
      }
   }
   
   /**
    * Returns a section of the string as is.
    * 
    * @param index
    *    The index of the section to retrieve.
    * @return
    *    The string found at the index passed.
    */
   public String section(int index)
   {
      return sections.get(index);
   }
   
   /**
    * Gets the section count. 
    * 
    * @return
    *    The number of sections following the 
    *    delimiter option.
    */
   public int getNumberOfSections()
   {
      return sections.size();
   }
}
