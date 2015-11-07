package ca.bjad.util;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * Utility which will read in text from a stream
 * and then replace "templates" within the text 
 * from the stream with values passed to the
 * utility.
 *
 * @author 
 *  Ben Dougall
 */
public class TemplateTextUtil
{
   private String templatePrefix;
   private String templateSuffix;
   private String templateText; 
   
   /**
    * Creates the object, reading the data from the url provided, and 
    * setting the default prefix and suffix for the templates 
    * as {{ and }}.
    *
    * @param streamURL
    *    Where to download the base text from.
    * @throws IOException
    *    Any IO Exception encoutered during the loading of the 
    *    base text will be thrown.
    */
   public TemplateTextUtil(String streamURL) throws IOException
   {
      this(streamURL, "{{", "}}");
   }
   
   /**
    * Creates the object, reading the data from the url provided, and 
    * applying a custom prefix and suffix to look for when replace
    * data in the template text loaded from the url.
    *
    * @param streamURL
    *    Where to download the base text from.
    * @param prefix
    *    The prefix to find in the template text.
    * @param suffix
    *    The suffix to find in the template text.
    * @throws IOException
    *    Any IO Exception encoutered during the loading of the 
    *    base text will be thrown.
    */
   public TemplateTextUtil(String streamURL, String prefix, String suffix) throws IOException
   {
      this.templatePrefix = prefix;
      this.templateSuffix = suffix;
      
      StringBuilder sb = new StringBuilder();
      try (Scanner s = new Scanner(URLStreamFactory.getStream(streamURL)))
      {        
         while (s.hasNextLine())
         {
            sb.append(s.nextLine()).append(System.lineSeparator());
         }
      }
      
      templateText = sb.toString().substring(0, sb.toString().lastIndexOf(System.lineSeparator()));
   }
   
   /**
    * Returns the template text, with the "templates" replaced
    * with the values contained in the mapping passed.
    * 
    * @param replacementMap
    *    The mapping of template text and the values to replace
    *    the template text with. 
    *    
    * @return
    *    The adjusted template text, or if the mapping is null/empty
    *    the original text from the stream used during the creation
    *    of the object.
    */
   public String getTemplateText(Map<String, String> replacementMap)
   {
      if (replacementMap == null)
      {
         return templateText;
      }
      
      String retValue = templateText;
      for (Entry<String, String> entry : replacementMap.entrySet())
      {
         retValue = retValue.replace(
               this.templatePrefix + entry.getKey() + this.templateSuffix, 
               entry.getValue());
      }
      
      return retValue;
   }
}
