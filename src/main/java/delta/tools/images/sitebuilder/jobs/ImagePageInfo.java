package delta.tools.images.sitebuilder.jobs;

import delta.common.utils.files.Path;
import delta.tools.images.sitebuilder.SiteBuilderConfiguration;

/**
 * @author DAM
 */
public class ImagePageInfo
{
  private SiteBuilderConfiguration _config;
  private ImagesList _images;
  private int _index;

  public ImagePageInfo(SiteBuilderConfiguration config, ImagesList images, int index)
  {
    _config=config;
    _images=images;
    _index=index;
  }

  public SiteBuilderConfiguration getConfig()
  {
    return _config;
  }

  public Path getPath()
  {
    return _images.getPath();
  }

  public int getIndex()
  {
    return _index+1;
  }

  public ImageInfo getImageInfo()
  {
    return _images.getImage(_index);
  }

  public int getTotalNumberOfImages()
  {
    return _images.getNumberOfImages();
  }
}
