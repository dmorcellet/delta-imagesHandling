package delta.tools.images.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import delta.common.utils.files.FilesMiscTools;

/**
 * Represents a set of related images.
 * @author DAM
 */
public class ImagesSet implements ImagesContainer
{
  /**
   * Reference to the database that owns this set.
   */
  private ImagesDatabase _database;
  /**
   * Root dir of this set, relative to the database's root dir.
   */
  private File _rootDir;
  /**
   * Name of this images set.
   */
  private String _name;
  private List<String> _images;

  /**
   * Constructor.
   * @param db Image database that owns this set.
   * @param name Name of this set.
   */
  public ImagesSet(ImagesDatabase db, String name)
  {
    _database=db;
    _name=name;
    _images=new ArrayList<String>();
  }

  /**
   * Get the database that owns this set (if any).
   * @return the database that owns this set (if any).
   */
  public ImagesDatabase getDatabase()
  {
    return _database;
  }

  /**
   * Get the name of this images set.
   * @return the name of this images set.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Get an image from this set.
   * @param index Index of targeted image.
   * @return An image.
   */
  public String getImage(int index)
  {
    return _images.get(index);
  }

  /**
   * Get the absolute root dir of this images set.
   * @return the absolute root dir of this images set.
   */
  public File getAbsoluteRootDir()
  {
    File databaseDir=null;
    if (_database!=null)
    {
      databaseDir=_database.getRootDir();
    }
    File ret=FilesMiscTools.build(databaseDir,_rootDir);
    return ret;
  }

  /**
   * Get the root dir of this images set (relative to the database's dir).
   * @return the root dir of this images set (relative to the database's dir).
   */
  public File getRootDir()
  {
    return _rootDir;
  }
}
