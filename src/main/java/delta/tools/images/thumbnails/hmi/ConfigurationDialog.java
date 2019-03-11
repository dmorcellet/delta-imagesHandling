package delta.tools.images.thumbnails.hmi;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import delta.tools.images.thumbnails.ImageSizeComputer;

/**
 * Configuration dialog for the thumbnail generation.
 * @author DAM
 */
public class ConfigurationDialog extends JDialog implements ActionListener
{
  private FormatConfigurationPanel _formatPanel;
  private PathsConfigurationPanel _pathsPanel;
  private OkCancelPanel _okCancelPanel;
  private boolean _exitStatus;

  /**
   * Constructor.
   */
  public ConfigurationDialog()
  {
    super((Frame)null,"Configuration");
    build();
    _exitStatus=false;
  }

  /**
   * Get the exit status for this dialog.
   * @return <code>true</code> if OK, <code>false</code> if cancel.
   */
  public boolean getExitStatus()
  {
    return _exitStatus;
  }

  private void build()
  {
    JPanel panel=new JPanel();
    panel.setLayout(new GridBagLayout());
    _pathsPanel=new PathsConfigurationPanel();
    Border pathsBorder=BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Chemins");
    _pathsPanel.setBorder(pathsBorder);
    _formatPanel=new FormatConfigurationPanel();
    Border formatBorder=BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Format");
    _formatPanel.setBorder(formatBorder);
    _okCancelPanel=new OkCancelPanel();
    Insets insets=new Insets(0,0,0,0);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0);
    panel.add(_pathsPanel,gbc);
    gbc.gridy=1;
    panel.add(_formatPanel,gbc);
    gbc.gridy=2;
    panel.add(_okCancelPanel,gbc);
    _okCancelPanel.getOKButton().addActionListener(this);
    _okCancelPanel.getCancelButton().addActionListener(this);
    setContentPane(panel);
    pack();
  }

  /**
   * Get the chosen thumbnail size computer.
   * @return a thumbnail size computer.
   */
  public ImageSizeComputer getConfiguration()
  {
    return _formatPanel.getConfiguration();
  }

  /**
   * Get the chosen source file.
   * @return a file.
   */
  public File getSourcePath()
  {
    return new File(_pathsPanel.getSourcePath());
  }

  /**
   * Get the chose target file.
   * @return a file.
   */
  public File getTargetPath()
  {
    return new File(_pathsPanel.getTargetPath());
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource()==_okCancelPanel.getOKButton())
    {
      boolean check=_formatPanel.check();
      if (check)
      {
        check=_pathsPanel.check();
        if (check)
        {
          setVisible(false);
          _exitStatus=true;
        }
      }
    }
    else if (e.getSource()==_okCancelPanel.getCancelButton())
    {
      setVisible(false);
      _exitStatus=false;
    }
  }
}
