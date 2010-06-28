package delta.tools.images.thumbnails;

import java.awt.Dimension;

import delta.imaging.utils.ImageAxis;

/**
 * An image size computer that uses a fixed size for one axis.
 * @author DAM
 */
public class FixedSizeForAxisISComputer implements ImageSizeComputer
{
  private int _size;
  private ImageAxis _axis;

  /**
   * Constructor.
   * @param axis Targeted image axis.
   * @param size Size for the targeted image axis.
   */
  public FixedSizeForAxisISComputer(ImageAxis axis, int size)
  {
    _size=size;
    _axis=axis;
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
    if (_axis==ImageAxis.VERTICAL)
    {
      height=_size;
      width=(_size*oldWidth)/oldHeight;
    }
    else if (_axis==ImageAxis.HORIZONTAL)
    {
      width=_size;
      height=(_size*oldHeight)/oldWidth;
    }
    else
    {
      throw new IllegalStateException("Invalid axis !!");
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
