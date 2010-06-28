package delta.tools.images.thumbnails;

import java.awt.Dimension;

/**
 * Image size computer that uses a reduction factor.
 * @author DAM
 */
public class ReductionFactorISComputer implements ImageSizeComputer
{
  private float _reductionFactor;

  /**
   * Constructor.
   * @param reductionFactor Reduction factor.
   */
  public ReductionFactorISComputer(float reductionFactor)
  {
    _reductionFactor=reductionFactor;
  }

  /**
   * Get the value of the reduction factor.
   * @return the value of the reduction factor.
   */
  public float getReductionFactor()
  {
    return _reductionFactor;
  }

  /**
   * Compute the size of a new image based on the size of an original image.
   * @param originalSize Size of original image.
   * @return Dimension of new image.
   */
  public Dimension computeNewSize(Dimension originalSize)
  {
    int newWidth=(int)(originalSize.width/_reductionFactor);
    int newHeight=(int)(originalSize.height/_reductionFactor);
    Dimension ret=new Dimension(newWidth,newHeight);
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
