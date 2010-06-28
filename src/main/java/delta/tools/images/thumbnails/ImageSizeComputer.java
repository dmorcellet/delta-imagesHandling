package delta.tools.images.thumbnails;

import java.awt.Dimension;

/**
 * Interface of a image size computer.
 * @author DAM
 */
public interface ImageSizeComputer
{
  /**
   * Compute the size of a new image based on the size of an original image.
   * @param originalSize Size of original image.
   * @return Dimension of new image.
   */
  public Dimension computeNewSize(Dimension originalSize);

  /**
   * Indicates if this computer preserves image ratios or not.
   * @return <code>true</code> if it does, <code>false</code> otherwise.
   */
  public boolean preservesRatio();
}
