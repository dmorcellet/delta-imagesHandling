package delta.tools.images.sitebuilder;

import java.io.File;

import delta.common.utils.files.Path;

/**
 * Encapsulates site structure information.
 * @author DAM
 */
public class SiteStructure
{
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
    _sourcePicturesDir=new File(_targetDir,SiteBuilderPathConstants.SOURCE_PICTURES);
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
    StringBuffer tmp=new StringBuffer();
    tmp.append(getRelativePathFromImagesDirToSiteRoot(imagesDir));
    tmp.append(SiteBuilderPathConstants.RESOURCES);
    tmp.append(File.separatorChar);
    tmp.append(SiteBuilderPathConstants.IMAGE_RESOURCES);
    tmp.append(File.separatorChar);
    String ret=tmp.toString();
    return ret;
  }
}
