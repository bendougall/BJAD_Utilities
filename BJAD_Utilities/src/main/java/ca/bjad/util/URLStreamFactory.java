package ca.bjad.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Input Stream factory provider that will
 * attempt to provide input streams based 
 * on the string passed to the factory.
 *
 * @author 
 *  Ben Dougall
 */
public final class URLStreamFactory
{
   private static final String CLASSPATH_PREFIX = "classpath:///";
   
   /**
    * Default scope constructor, used to prevent
    * non-static instances from being created.
    */
   protected URLStreamFactory() {}
   
   /**
    * <p>
    * Primary factory function that will return an 
    * inputstream based on the string passed. 
    * </p> 
    * 
    * <p>
    * HTTP://, file:/, and classpath:/// will directly
    * attempt to gather the resource. 
    * </p>
    * 
    * <p>
    * No prefix will be handled by attempting to find a 
    * file with the name/path specified in the string, 
    * and if the file is not found, the classpath will
    * be searched. If nothing is found in the classpath
    * matching the string, IOException will be thrown.
    * </p> 
    * 
    * @param url
    *    The url/string to get the stream for. 
    * @return
    *    The stream based on the string passed, see 
    *    description for further details.
    * @throws IOException
    *    Any stream opening issues from HTTP, File, or 
    *    classpath resources will be thrown, or if the 
    *    protocol is not stated, IOException will be 
    *    thrown if the string passed is not a valid file
    *    path or a valid classpath resource.
    */
   public static InputStream getStream(String url) throws IOException
   {
      String lcURL = url.toLowerCase();
      InputStream is = null;
      
      if (lcURL.startsWith("http://") || lcURL.startsWith("file:/"))
      {
         is = new URL(url).openStream();
      }
      else if (lcURL.startsWith(CLASSPATH_PREFIX))
      {
         is = ClassLoader.getSystemResourceAsStream(url.substring(CLASSPATH_PREFIX.length()));
         if (is != null)
         {
            return is;
         }
      }
      else 
      {
         // check to see if the string represents a file 
         if (new File(url).exists())
         {
            is = new FileInputStream(url);
         }
         // not a file, lets try classpath.
         else
         {
            is = ClassLoader.getSystemResourceAsStream(url);
         }            
      }
      
      if (is == null)
      {
         // no streamable resource found, throw exception
         throw new IOException("Could not find a source for " + url + " to open a stream from");
      }
      else
      {
         return is;
      }
   }
}
