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
  private Path _sourcePath;
  private Path _sitePath;
  private List<ImageInfo> _images;
  
  /**
   * Constructor.
   * @param sourcePath Path of the managed images in the source tree.
   * @param sitePath Path of the managed images in the site tree.
   */
  public ImagesList(Path sourcePath, Path sitePath)
  {
    _images=new ArrayList<ImageInfo>();
    _sourcePath=sourcePath;
    _sitePath=sitePath;
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
   * Get the path in the source tree of the managed images.
   * @return A path.
   */
  public Path getSourcePath()
  {
    return _sourcePath;
  }

  /**
   * Get the path in the site tree of of the managed images.
   * @return A path.
   */
  public Path getSitePath()
  {
    return _sitePath;
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
