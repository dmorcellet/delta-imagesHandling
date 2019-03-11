package delta.tools.images.thumbnails.hmi;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Panel for the configuration of the input and output paths.
 * @author DAM
 */
public class PathsConfigurationPanel extends JPanel implements ActionListener
{
  /**
   * Number of columns for the path text fields.
   */
  private static final int NB_COLUMNS=30;

  private JTextField _sourcePath;
  private JButton _chooseSourcePath;
  private JFileChooser _sourceChooser;
  private JTextField _targetPath;
  private JButton _chooseTargetPath;
  private JFileChooser _targetChooser;

  /**
   * Constructor.
   */
  public PathsConfigurationPanel()
  {
    build();
  }

  private void build()
  {
    setLayout(new GridBagLayout());
    // Source path
    _sourcePath=new JTextField(NB_COLUMNS);
    _chooseSourcePath=new JButton("Choisir...");
    _chooseSourcePath.addActionListener(this);
    _sourcePath.setEditable(false);
    // Target path
    _targetPath=new JTextField(NB_COLUMNS);
    _chooseTargetPath=new JButton("Choisir...");
    _chooseTargetPath.addActionListener(this);
    _targetPath.setEditable(false);
    {
      Insets insets=new Insets(5,5,5,5);
      GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,
          GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
      add(new JLabel("Source :"),gbc);
      gbc.gridy=1;
      add(new JLabel("Destination :"),gbc);
      gbc.gridy=0; gbc.gridx=1;
      gbc.weightx=1.0; gbc.fill=GridBagConstraints.HORIZONTAL;
      add(_sourcePath,gbc);
      gbc.gridy=1;
      add(_targetPath,gbc);
      gbc.gridy=0; gbc.gridx=2;
      gbc.weightx=0.0;
      add(_chooseSourcePath,gbc);
      gbc.gridy=1;
      add(_chooseTargetPath,gbc);
    }

    _sourceChooser=new JFileChooser();
    _sourceChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    _targetChooser=new JFileChooser();
    _targetChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
  }

  private File showChooser(JFileChooser chooser)
  {
    int ret=chooser.showOpenDialog(this);
    if (ret==JFileChooser.APPROVE_OPTION)
      return chooser.getSelectedFile();
    return null;
  }

  /**
   * Check the validity of the input.
   * @return <code>true</code> if it is valid, <code>false</code> otherwise.
   */
  public boolean check()
  {
    String sourcePath=_sourcePath.getText();
    if ((sourcePath==null) || (sourcePath.length()==0))
    {
      JOptionPane.showMessageDialog(this,"Champ 'Source' non renseigné.","Erreur",JOptionPane.ERROR_MESSAGE);
      _chooseSourcePath.requestFocusInWindow();
      return false;
    }
    String targetPath=_targetPath.getText();
    if ((targetPath==null) || (targetPath.length()==0))
    {
      JOptionPane.showMessageDialog(this,"Champ 'Destination' non renseigné.","Erreur",JOptionPane.ERROR_MESSAGE);
      _chooseTargetPath.requestFocusInWindow();
      return false;
    }
    File fSourcePath=new File(sourcePath);
    if (!fSourcePath.canRead())
    {
      JOptionPane.showMessageDialog(this,"Champ 'Source' : chemin non lisible.","Erreur",JOptionPane.ERROR_MESSAGE);
      _chooseSourcePath.requestFocusInWindow();
      return false;
    }
    File fTargetPath=new File(targetPath);
    if (!fTargetPath.exists())
    {
      boolean mkdirs=fTargetPath.mkdirs();
      if (!mkdirs)
      {
        JOptionPane.showMessageDialog(this,"Impossible de créer le répertoire '"+targetPath+"'","Erreur",JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    if (!fTargetPath.canWrite())
    {
      JOptionPane.showMessageDialog(this,"Champ 'Destination' : chemin non inscriptible.","Erreur",JOptionPane.ERROR_MESSAGE);
      _chooseTargetPath.requestFocusInWindow();
      return false;
    }
    return true;
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource()==_chooseSourcePath)
    {
      File f=showChooser(_sourceChooser);
      if (f!=null)
        _sourcePath.setText(f.getAbsolutePath());
    }
    else if (e.getSource()==_chooseTargetPath)
    {
      File f=showChooser(_targetChooser);
      if (f!=null)
        _targetPath.setText(f.getAbsolutePath());
    }
  }

  /**
   * Get the source path.
   * @return the source path.
   */
  public String getSourcePath()
  {
    return _sourcePath.getText();
  }

  /**
   * Get the target path.
   * @return the target path.
   */
  public String getTargetPath()
  {
    return _targetPath.getText();
  }
}
