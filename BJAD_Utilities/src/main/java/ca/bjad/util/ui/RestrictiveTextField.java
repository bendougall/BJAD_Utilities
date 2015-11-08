package ca.bjad.util.ui;

import javax.swing.JTextField;

/**
 * Base class for restrictive text fields, such as
 * the integer and decimal text fields contained
 * within this package.
 *
 * @author 
 *  Ben Dougall
 */
class RestrictiveTextField extends JTextField
{
   private static final long serialVersionUID = 1558940477904874871L;

   /**
    * Retrieves the actual text content for the 
    * field, without subclasses affecting the 
    * results.
    * 
    * @return
    *    The results of JTextField.getText();
    */
   String getTextContent()
   {
      return super.getText();
   }
}
