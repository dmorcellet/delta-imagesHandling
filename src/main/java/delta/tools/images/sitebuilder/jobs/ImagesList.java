package delta.tools.images.sitebuilder.jobs;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.files.Path;

/**
 * @author DAM
 */
public class ImagesList
{
  private Path _path;
  private List<ImageInfo> _images;
  
  public ImagesList(Path path)
  {
    _images=new ArrayList<ImageInfo>();
    _path=path;
  }

  public ImageInfo getImage(int index)
  {
    return _images.get(index);
  }

  public Path getPath()
  {
    return _path;
  }

  public void addImage(String imageName)
  {
    ImageInfo info=new ImageInfo(imageName);
    _images.add(info);
  }

  public int getNumberOfImages()
  {
    return _images.size();
  }
}
