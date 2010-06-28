package delta.tools.images.thumbnails.hmi;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class OkCancelPanel extends JPanel
{
  private JButton _okButton;
  private JButton _cancelButton;

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

  public JButton getOKButton() { return _okButton; }
  public JButton getCancelButton() { return _cancelButton; }
}
