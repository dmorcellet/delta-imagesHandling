package delta.tools.images.sitebuilder.selection;

import java.util.List;

/**
 * Chooses the files and directories to put in the site.
 * @author DAM
 */
public interface FilesSelection
{
  /**
   * Get an ordered list of the images found in the specified path.
   * @param path Path to use.
   * @return A list of image names.
   */
  List<String> getImages(String path);

  /**
   * Get an ordered list of the directories found in the specified path.
   * @param path Path to use.
   * @return A list of directories names.
   */
  List<String> getDirectories(String path);
}
