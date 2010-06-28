package delta.tools.images.manager;

import java.io.File;

/**
 * Represents an images database.
 * @author DAM
 */
public class ImagesDatabase implements ImagesContainer
{
  private String _name;
  private File _rootDir;

  /**
   * Constructor.
   * @param name Name of the database.
   * @param rootDir Root directory of the database.
   */
  public ImagesDatabase(String name, File rootDir)
  {
    _name=name;
    _rootDir=rootDir;
  }

  /**
   * Get the name of this database.
   * @return the name of this database.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Get the root dir of this database.
   * @return the root dir of this database.
   */
  public File getRootDir()
  {
    return _rootDir;
  }
}
