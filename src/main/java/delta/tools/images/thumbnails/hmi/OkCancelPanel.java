package delta.tools.images.thumbnails.hmi;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Panel with OK and cancel buttons.
 * @author DAM
 */
public class OkCancelPanel extends JPanel
{
  private JButton _okButton;
  private JButton _cancelButton;

  /**
   * Constructor.
   */
  public OkCancelPanel()
  {
    build();
  }

  private void build()
  {
    setLayout(new FlowLayout(FlowLayout.TRAILING));
    _okButton=new JButton("OK");
    add(_okButton);
    _cancelButton=new JButton("Annuler");
    add(_cancelButton);
  }

  /**
   * Get the OK button.
   * @return the OK button.
   */
  public JButton getOKButton()
  {
    return _okButton;
  }

  /**
   * Get the cancel button.
   * @return the cancel button.
   */
  public JButton getCancelButton()
  {
    return _cancelButton;
  }
}
