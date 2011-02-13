package delta.tools.images.sitebuilder.jobs;

/**
 * Information about a single image.
 * @author DAM
 */
public class ImageInfo
{
  private String _name;
  private int _width;
  private int _height;

  /**
   * Constructor.
   * @param name Name of image.
   */
  public ImageInfo(String name)
  {
    _name=name;
  }
  
  /**
   * Get the name of this image.
   * @return the name of this image.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Set the size of this image.
   * @param width Width to set.
   * @param height Height to set.
   */
  public void setSize(int width, int height)
  {
    _width=width;
    _height=height;
  }

  /**
   * Get the width of this image.
   * @return the width of this image.
   */
  public int getWidth()
  {
    return _width;
  }
  
  /**
   * Get the height of this image.
   * @return the height of this image.
   */
  public int getHeight()
  {
    return _height;
  }
}
