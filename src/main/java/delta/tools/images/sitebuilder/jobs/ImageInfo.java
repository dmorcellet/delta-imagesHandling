package delta.tools.images.sitebuilder.jobs;

/**
 * @author DAM
 */
public class ImageInfo
{
  private String _name;
  private int _width;
  private int _height;

  public ImageInfo(String name)
  {
    _name=name;
  }
  
  public String getName()
  {
    return _name;
  }

  public void setSize(int width, int height)
  {
    _width=width;
    _height=height;
  }

  public int getWidth()
  {
    return _width;
  }
  
  public int getHeight()
  {
    return _height;
  }
}
