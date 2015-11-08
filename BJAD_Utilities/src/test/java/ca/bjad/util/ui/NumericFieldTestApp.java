package ca.bjad.util.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import ca.bjad.util.ui.listener.InvalidEntryListener;

/**
 * Quick driver app to verify the numerictextfield
 * behaviours.
 *
 * @author 
 *  Ben Dougall
 */
@SuppressWarnings("javadoc")
public class NumericFieldTestApp extends JFrame implements Runnable, InvalidEntryListener, FocusListener
{
   private static final long serialVersionUID = -1021652999054749965L;
   private JLabel statusLabel = new JLabel("");
   private Color standardBGColor = null;
   private Color standardFGColor = null; 

   private JPanel createIntegerTextFieldPanel()
   {
      JPanel pane = new JPanel(new GridLayout(5, 2, 5, 5));
      pane.add(new JLabel("No Limits:"));
      pane.add(NumericTextField.newIntegerFieldNoLimits());
      
      pane.add(new JLabel("No Negatives:"));
      pane.add(NumericTextField.newIntegerFieldNoNegatives());
      
      pane.add(new JLabel("Max: 100"));
      pane.add(NumericTextField.newIntegerFieldWithLimit(100));
      
      pane.add(new JLabel("No Neg, Max: 100"));
      pane.add(NumericTextField.newIntegerField(false, 100));
      
      pane.add(new JLabel(""));
      pane.add(new JLabel(""));
      
      applyListenersForFieldsInPanel(pane);
      return pane;
   }
   
   private JPanel createDecimalTextFieldPanel()
   {
      JPanel pane = new JPanel(new GridLayout(5, 2, 10, 5));
      pane.add(new JLabel("No Limits:"));
      pane.add(NumericTextField.newDecimalFieldNoLimits());
      
      pane.add(new JLabel("No Negatives:"));
      pane.add(NumericTextField.newDecimalFieldNoNegatives());
      
      pane.add(new JLabel("Max: 100.5"));
      pane.add(NumericTextField.newDecimalFieldWithLimit(100.5));
      
      pane.add(new JLabel("No Neg, Max: 100.5"));
      pane.add(NumericTextField.newDecimalField(false, 100.5));
      
      pane.add(new JLabel(""));
      pane.add(new JLabel(""));
      
      applyListenersForFieldsInPanel(pane);
      return pane;
   }
   
   private JPanel createStatusBarPanel()
   {
      JPanel statusPanel = new JPanel();
      statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
      statusPanel.setPreferredSize(new Dimension(this.getWidth(), 16));
      statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
      
      statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
      statusPanel.add(statusLabel);
      
      return statusPanel;
   }
   
   private void applyListenersForFieldsInPanel(JPanel panel)
   {
      for (int i = 0; i != panel.getComponentCount(); ++i)
      {
         if (panel.getComponent(i) instanceof AbstractRestrictiveTextField)
         {
            AbstractRestrictiveTextField field = (AbstractRestrictiveTextField)panel.getComponent(i);
            field.addInvalidEntryListener(this);
            field.addFocusListener(this);
            
            if (standardBGColor == null)
            {
               standardBGColor = field.getBackground();
               standardFGColor = field.getForeground();
            }
         }
      }
   }
   
   public NumericFieldTestApp()
   {
      super("Numeric Text Field test application");      
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
   
      JPanel contentPane = new JPanel(new BorderLayout(5, 2), true);
      contentPane.add(createIntegerTextFieldPanel(), BorderLayout.WEST);
      contentPane.add(createDecimalTextFieldPanel(), BorderLayout.EAST);
      contentPane.add(createStatusBarPanel(), BorderLayout.SOUTH);
      contentPane.setBorder(new EmptyBorder(2, 5, 1, 5));
      
      this.setContentPane(contentPane);
      this.pack();      
   }
   
   public static void main(String[] args)
   {
      NumericFieldTestApp app = new NumericFieldTestApp();      
      SwingUtilities.invokeLater(app);
   }

   @Override
   public void run()
   {
      this.setSize(600, this.getHeight());
      this.setVisible(true);
   }
   
   @Override
   public void invalidEntryDetected(AbstractRestrictiveTextField field, InvalidatedReason reason, String input)
   {      
      field.setBackground(Color.YELLOW);
      field.setForeground(Color.BLACK);
      
      String labelText = String.format("Invalid entry attempted within the highlighted field. Reason: %s, Attempted Entry: %s",
            reason.name(),
            input);
      
      statusLabel.setText(labelText);
   }

   @Override
   public void focusLost(FocusEvent e)
   {
      if (e.getSource() instanceof AbstractRestrictiveTextField)
      {
         AbstractRestrictiveTextField field = (AbstractRestrictiveTextField)e.getSource();
         if (field.getBackground().equals(Color.YELLOW))
         {
            field.setBackground(standardBGColor);
            field.setForeground(standardFGColor);
         }
         
         if (!statusLabel.getText().isEmpty())
         {
            statusLabel.setText("");
         }
      }
   }

   @Override public void focusGained(FocusEvent e){}
}
