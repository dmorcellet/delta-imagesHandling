package delta.tools.images.manager;

import java.io.File;

/**
 * Represents an image.
 * @author DAM
 */
public class Image
{
  /**
   * Reference to the database that owns this set.
   */
  private ImagesDatabase _database;
  /**
   *
   */
  private File _imagePath;
  /**
   * Name of this image.
   */
  private String _name;

  /**
   * Constructor.
   * @param db Image database that owns this image.
   * @param imagePath Image path.
   * @param name Name of the image.
   */
  public Image(ImagesDatabase db, File imagePath, String name)
  {
    _database=db;
    _imagePath=imagePath;
    _name=name;
  }

  /**
   * Get the database that owns this image (if any).
   * @return the database that owns this image (if any).
   */
  public ImagesDatabase getDatabase()
  {
    return _database;
  }

  /**
   * Get the image file path.
   * @return the image file path.
   */
  public File getImagePath()
  {
    return _imagePath;
  }

  /**
   * Get the name of this image.
   * @return the name of this image.
   */
  public String getName()
  {
    return _name;
  }
}
