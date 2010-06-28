package delta.tools.images.thumbnails;

import java.awt.Dimension;

/**
 * An image size computer that uses some fixed bounds to compute new image sizes.
 * @author DAM
 */
public class MaxBoundsISComputer implements ImageSizeComputer
{
  private Dimension _bounds;

  /**
   * Constructor.
   * @param bounds Maximum dimension for the new image.
   */
  public MaxBoundsISComputer(Dimension bounds)
  {
    _bounds=new Dimension(bounds);
  }

  /**
   * Compute the size of a new image based on the size of an original image.
   * @param originalSize Size of original image.
   * @return Dimension of new image.
   */
  public Dimension computeNewSize(Dimension originalSize)
  {
    int oldHeight=originalSize.height;
    int oldWidth=originalSize.width;

    int height;
    int width;

    int v1=_bounds.width*oldHeight;
    int v2=_bounds.height*oldWidth;
    if (v1<v2)
    {
      width=_bounds.width;
      height=(width*oldHeight)/oldWidth;
    }
    else if (v1>v2)
    {
      height=_bounds.height;
      width=(height*oldWidth)/oldHeight;
    }
    else
    {
      height=_bounds.height;
      width=_bounds.width;
    }
    Dimension ret=new Dimension(width,height);
    return ret;
  }

  /**
   * Indicates if this computer preserves image ratios or not.
   * @return <code>true</code>.
   */
  public boolean preservesRatio()
  {
    return true;
  }
}
