package delta.tools.images.thumbnails.hmi;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import delta.imaging.utils.ImageAxis;
import delta.tools.images.thumbnails.FixedSizeForAxisISComputer;
import delta.tools.images.thumbnails.ImageSizeComputer;
import delta.tools.images.thumbnails.MaxBoundsISComputer;
import delta.tools.images.thumbnails.ReductionFactorISComputer;

/**
 * Panel for the configuration of the thumbnail size.
 * @author DAM
 */
public class FormatConfigurationPanel extends JPanel implements ActionListener
{
  // Max size
  private JRadioButton _setMaxSize;
  private JTextField _maxHeightPixels;
  private JTextField _maxWidthPixels;
  // Set height
  private JRadioButton _setHeight;
  private JTextField _heightPixels;
  // Set width
  private JRadioButton _setWidth;
  private JTextField _widthPixels;
  // Reduction
  private JRadioButton _reductionMode;
  private JTextField _factor;

  private JRadioButton[] _buttons;
  private JTextField[][] _editors;
  private static int[] _minValues={1,1,1,1};
  private static int[] _maxValues={Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE};
  private static String[] _fieldNames={"Taille maximum","Hauteur","Largeur","Facteur de réduction"};
  private int _selectedMode=0;

  /**
   * Constructor.
   */
  public FormatConfigurationPanel()
  {
    build();
  }

  private void build()
  {
    setLayout(new GridBagLayout());
    ButtonGroup modes=new ButtonGroup();

    // Max size
    _setMaxSize=new JRadioButton("Taille maximum");
    _setMaxSize.addActionListener(this);
    modes.add(_setMaxSize);
    _maxHeightPixels=new JTextField(5);
    _maxWidthPixels=new JTextField(5);
    // Max height
    _setHeight=new JRadioButton("Hauteur");
    _setHeight.addActionListener(this);
    modes.add(_setHeight);
    _heightPixels=new JTextField(5);
    // Max width
    _setWidth=new JRadioButton("Largeur");
    _setWidth.addActionListener(this);
    modes.add(_setWidth);
    _widthPixels=new JTextField(5);
    // Reduction
    _reductionMode=new JRadioButton("Réduction");
    _reductionMode.addActionListener(this);
    modes.add(_reductionMode);
    _factor=new JTextField(5);

    Insets insets=new Insets(0,0,0,0);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    gbc.gridx=0;
    gbc.gridy=0;
    // max size
    add(_setMaxSize,gbc);gbc.gridx++;
    add(_maxHeightPixels,gbc);gbc.gridx++;
    add(_maxWidthPixels,gbc);gbc.gridx++;
    add(new JLabel("pixels"),gbc);gbc.gridx++;
    gbc.gridx=0;gbc.gridy++;
    // set height
    add(_setHeight,gbc);gbc.gridx++;
    add(_heightPixels,gbc);gbc.gridx++;
    add(new JLabel("pixels"),gbc);gbc.gridx++;
    gbc.gridx=0;gbc.gridy++;
    // set width
    add(_setWidth,gbc);gbc.gridx++;
    add(_widthPixels,gbc);gbc.gridx++;
    add(new JLabel("pixels"),gbc);gbc.gridx++;
    gbc.gridx=0;gbc.gridy++;
    // reduction mode
    add(_reductionMode,gbc);gbc.gridx++;
    add(_factor,gbc);gbc.gridx++;
    add(new JLabel("fois"),gbc);gbc.gridx++;

    _buttons=new JRadioButton[] { _setMaxSize,_setHeight,_setWidth,_reductionMode };
    _editors=new JTextField[][] { {_maxHeightPixels,_maxWidthPixels},{_heightPixels},{_widthPixels},{_factor} };

    select(_setMaxSize);
  }

  public void actionPerformed(ActionEvent e)
  {
    Object o=e.getSource();
    select((JRadioButton)o);
  }

  private void select(JRadioButton button)
  {
    button.setSelected(true);
    for(int i=0;i<_buttons.length;i++)
    {
      if (_buttons[i]==button)
      {
        for(int j=0;j<_editors[i].length;j++)
        {
          _editors[i][j].setEnabled(true);
        }
        _selectedMode=i;
      }
      else
      {
        for(int j=0;j<_editors[i].length;j++)
        {
          _editors[i][j].setEnabled(false);
        }
      }
    }
  }

  /**
   * Check the validity of the input.
   * @return <code>true</code> if it is valid, <code>false</code> otherwise.
   */
  public boolean check()
  {
    int minValue=_minValues[_selectedMode];
    int maxValue=_maxValues[_selectedMode];
    String fieldName=_fieldNames[_selectedMode];
    JTextField[] textField=_editors[_selectedMode];
    boolean ok=true;
    for(int i=0;i<textField.length;i++)
    {
      ok=checkTextfield(minValue,maxValue,fieldName,textField[i]);
      if (!ok)
      {
        break;
      }
    }
    return ok;
  }

  private boolean checkTextfield(int min, int max, String fieldName, JTextField textField)
  {
    String value=textField.getText();
    if ((value==null) || (value.length()==0))
    {
      JOptionPane.showMessageDialog(this,"Champ '"+fieldName+"' non renseigné.","Erreur",JOptionPane.ERROR_MESSAGE);
      textField.requestFocusInWindow();
      return false;
    }
    int intValue=-1;
    try
    {
      intValue=Integer.parseInt(value);
    }
    catch(NumberFormatException nfe)
    {
      JOptionPane.showMessageDialog(this,"Champ '"+fieldName+"' mal renseigné ('"+value+"' n'est pas un nombre).","Erreur",JOptionPane.ERROR_MESSAGE);
      textField.requestFocusInWindow();
      return false;
    }
    if (intValue<min)
    {
      JOptionPane.showMessageDialog(this,"Champ '"+fieldName+"' mal renseigné ("+value+"<"+min+").","Erreur",JOptionPane.ERROR_MESSAGE);
      textField.requestFocusInWindow();
      return false;
    }
    if (intValue>max)
    {
      JOptionPane.showMessageDialog(this,"Champ '"+fieldName+"' mal renseigné ("+value+">"+max+").");
      textField.requestFocusInWindow();
      return false;
    }
    return true;
  }

  /**
   * Get the image size computer from the current input.
   * @return An image size computer.
   */
  public ImageSizeComputer getConfiguration()
  {
    ImageSizeComputer computer;
    String text=_editors[_selectedMode][0].getText().trim();
    int value=Integer.parseInt(text);
    if (_selectedMode==0)
    {
      String text2=_editors[_selectedMode][1].getText().trim();
      int value2=Integer.parseInt(text2);
      computer=new MaxBoundsISComputer(new Dimension(value,value2));
    }
    else if (_selectedMode==1)
    {
      computer=new FixedSizeForAxisISComputer(ImageAxis.VERTICAL,value);
    }
    else if (_selectedMode==2)
    {
      computer=new FixedSizeForAxisISComputer(ImageAxis.HORIZONTAL,value);
    }
    else
    {
      computer=new ReductionFactorISComputer(value);
    }
    return computer;
  }
}
