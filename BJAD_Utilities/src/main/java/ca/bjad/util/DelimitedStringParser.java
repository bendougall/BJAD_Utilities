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
    * Returns a section of the string as is, with
    * or without the quotes that may have surrounded
    * the section.
    * 
    * @param index
    *    The index of the section to retrieve.
    * @return
    *    The string found at the index passed.
    */
   public String asIs(int index)
   {
      return sections.get(index);
   }
   
   /**
    * Returns the string at the section, with quotes 
    * removed if they were present.
    * 
    * @param index
    *    The index of the sections to retrieve.
    * @return
    *    The string found, with surrounding quotes 
    *    removed.
    */
   public String unquoted(int index)
   {
      String s = sections.get(index);
      int startPos = 0;
      if (s.startsWith("\""))
      {
         startPos = 1;
      }
      if (s.endsWith("\""))
      {
         return s.substring(startPos, s.length() - 2);
      }
      else
      {
         return s.substring(startPos);
      }
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
