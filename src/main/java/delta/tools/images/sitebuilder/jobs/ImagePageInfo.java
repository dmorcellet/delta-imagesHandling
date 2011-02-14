package delta.tools.images.sitebuilder.jobs;

import delta.common.utils.files.Path;

/**
 * Information about an image page.
 * @author DAM
 */
public class ImagePageInfo
{
  private ImagesList _images;
  private int _index;

  /**
   * Constructor.
   * @param images Set of images to use.
   * @param index Index of image to use in this set. 
   */
  public ImagePageInfo(ImagesList images, int index)
  {
    _images=images;
    _index=index;
  }

  /**
   * Get the source path of the managed image.
   * @return a path.
   */
  public Path getSourcePath()
  {
    return _images.getSourcePath();
  }

  /**
   * Get the site path of the managed image.
   * @return a path.
   */
  public Path getSitePath()
  {
    return _images.getSitePath();
  }

  /**
   * Get the index of image.
   * @return an image index (starting at 1).
   */
  public int getIndex()
  {
    return _index+1;
  }

  /**
   * Get the information about the image.
   * @return image information.
   */
  public ImageInfo getImageInfo()
  {
    return _images.getImage(_index);
  }

  /**
   * Get the number of images in the parent set of images.
   * @return a positive number.
   */
  public int getTotalNumberOfImages()
  {
    return _images.getNumberOfImages();
  }
}
