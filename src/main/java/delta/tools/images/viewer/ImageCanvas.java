package delta.tools.images.viewer;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

/**
 * Canvas component to display an image.
 * @author DAM
 */
public class ImageCanvas extends Canvas
{
  private transient Image _image;

  /**
   * Constructor.
   * @param i Image to show.
   */
  public ImageCanvas(Image i)
  {
    _image=i;
  }

  @Override
  public void paint(Graphics g)
  {
    g.drawImage(_image,0,0,null);
  }
}
