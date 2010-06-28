package delta.tools.images.viewer;

import java.awt.Dimension;
import java.io.File;

import javax.media.jai.PlanarImage;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import com.sun.media.jai.widget.DisplayJAI;

import delta.imaging.utils.ImagesHandlingLoggers;
import delta.tools.images.ImageTools;

public class ImageFrame extends JFrame
{
  private static final Logger _logger=ImagesHandlingLoggers.getImagesHandlingLogger();

  public ImageFrame(String name, PlanarImage image)
  {
    super(name);
    init(image);
  }

  public ImageFrame(File path)
  {
    super(path.getName());
    PlanarImage image=ImageTools.readImage(path);
    init(image);
  }

  private void init(PlanarImage image)
  {
    Dimension screenSize=getToolkit().getScreenSize();
    DisplayJAI imagePanel=new DisplayJAI(image);
    add(imagePanel);
    int width=Math.min(image.getWidth(),screenSize.width);
    int height=Math.min(image.getHeight(),screenSize.height);
    Dimension prefs=new Dimension(width,height);
    imagePanel.setPreferredSize(prefs);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    pack();
  }
}
