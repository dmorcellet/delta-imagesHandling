package delta.tools.images.viewer;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

public class ImageCanvas extends Canvas
{
  private transient Image _image;
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
