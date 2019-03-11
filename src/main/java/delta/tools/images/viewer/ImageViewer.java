package delta.tools.images.viewer;

import java.io.File;

import javax.swing.JFileChooser;

import delta.common.utils.environment.User;

/**
 * Image viewer.
 * @author DAM
 */
public class ImageViewer
{
  /**
   * Constructor.
   */
  public ImageViewer()
  {
    File file=selectFile();
    if (file!=null)
    {
      go(file);
    }
  }

  private File selectFile()
  {
    JFileChooser chooser=new JFileChooser();
    File rootDir=User.getLocalUser().getHomeDir();
    chooser.setCurrentDirectory(rootDir);
    File file=null;
    int ret=chooser.showOpenDialog(null);
    if (ret==JFileChooser.APPROVE_OPTION)
    {
      file=chooser.getSelectedFile();
    }
    return file;
  }

  private void go(File path)
  {
    ImageFrame frame=new ImageFrame(path);
    frame.setVisible(true);
  }

  /**
   * Main method for this application.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new ImageViewer();
  }
}
