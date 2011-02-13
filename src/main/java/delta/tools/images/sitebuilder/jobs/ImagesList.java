package delta.tools.images.sitebuilder.jobs;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.files.Path;

/**
 * List of images.
 * @author DAM
 */
public class ImagesList
{
  private Path _path;
  private List<ImageInfo> _images;
  
  /**
   * Constructor.
   * @param path Path of the managed images.
   */
  public ImagesList(Path path)
  {
    _images=new ArrayList<ImageInfo>();
    _path=path;
  }

  /**
   * Get the information about an image using it's index.
   * @param index Index to use.
   * @return An image information object.
   */
  public ImageInfo getImage(int index)
  {
    return _images.get(index);
  }

  /**
   * Get the path of the images in this list.
   * @return A path.
   */
  public Path getPath()
  {
    return _path;
  }

  /**
   * Add an image to this list.
   * @param imageName Name of the image to add.
   */
  public void addImage(String imageName)
  {
    ImageInfo info=new ImageInfo(imageName);
    _images.add(info);
  }

  /**
   * Get the number of images in this list.
   * @return A positive number. 
   */
  public int getNumberOfImages()
  {
    return _images.size();
  }
}
