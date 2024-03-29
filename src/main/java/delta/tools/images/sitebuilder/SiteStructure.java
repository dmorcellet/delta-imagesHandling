package delta.tools.images.sitebuilder;

import java.io.File;

import delta.common.utils.files.Path;

/**
 * Encapsulates site structure information.
 * @author DAM
 */
public class SiteStructure
{
  /**
   * Name of the source pictures directory.
   */
  public static final String SOURCE_PICTURES="originaux";

  private File _targetDir;
  private File _siteDir;
  private File _sourcePicturesDir;

  /**
   * Constructor.
   * @param to Root path for the site.
   */
  public SiteStructure(File to)
  {
    _targetDir=to;
    _siteDir=new File(_targetDir,SiteBuilderPathConstants.SITE);
    _sourcePicturesDir=new File(_targetDir,SOURCE_PICTURES);
  }

  /**
   * Get the target directory.
   * @return the target directory.
   */
  public File getTargetDir()
  {
    return _targetDir;
  }

  /**
   * Get the root directory for the HTML site.
   * @return the root directory for the HTML site.
   */
  public File getSiteDir()
  {
    return _siteDir;
  }

  /**
   * Get the root directory for the original pictures.
   * @return the root directory for the original pictures.
   */
  public File getSourcePicturesDir()
  {
    return _sourcePicturesDir;
  }

  /**
   * Get the relative path from an images directory
   * to the site root directory. 
   * @param imagesDir Images directory to use.
   * @return A relative path.
   */
  public String getRelativePathFromImagesDirToSiteRoot(Path imagesDir)
  {
    StringBuffer tmp=new StringBuffer();
    int level=imagesDir.getLevel();
    for(int i=0;i<level;i++)
    {
      tmp.append("../");
    }
    String ret=tmp.toString();
    return ret;
  }

  /**
   * Get the relative path from an images directory
   * to the site resources directory. 
   * @param imagesDir Images directory to use.
   * @return A relative path.
   */
  public String getRelativePathFromImagesDirToResources(Path imagesDir)
  {
    StringBuilder tmp=new StringBuilder();
    tmp.append(getRelativePathFromImagesDirToSiteRoot(imagesDir));
    tmp.append(SiteBuilderPathConstants.RESOURCES);
    tmp.append('/');
    tmp.append(SiteBuilderPathConstants.IMAGE_RESOURCES);
    tmp.append('/');
    String ret=tmp.toString();
    return ret;
  }

  /**
   * Get the relative path from an images directory
   * to the site resources directory. 
   * @param sourcePath Path of images in the source tree.
   * @param sitePath Path of images in the site tree.
   * @return A relative path.
   */
  public String getRelativePathFromImagesDirToSourceImages(Path sourcePath, Path sitePath)
  {
    StringBuilder tmp=new StringBuilder();
    tmp.append(getRelativePathFromImagesDirToSiteRoot(sitePath));
    tmp.append("../../");
    tmp.append(SOURCE_PICTURES);
    tmp.append('/');
    tmp.append(sourcePath.getPath());
    tmp.append('/');
    String ret=tmp.toString();
    return ret;
  }
}
