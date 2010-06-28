package delta.tools.images.sitebuilder;

import java.io.File;

/**
 * Configuration information for the site builder.
 * @author DAM
 */
public class SiteBuilderConfiguration
{
  private File _sourceDir;
  private File _targetDir;
  private File _siteDir;
  private File _sourcePicturesDir;
  private boolean _useFullTitle;
  private SiteLabelsManager _labelManager;
  private ImageCommentsManager _imageCommentsManager;

  /**
   * Constructor.
   * @param from Source directory (contains the images).
   * @param to Directory to write to.
   */
  public SiteBuilderConfiguration(File from, File to)
  {
    _sourceDir=from;
    _targetDir=to;
    _siteDir=new File(_targetDir,SiteBuilderPathConstants.SITE);
    _sourcePicturesDir=new File(_targetDir,SiteBuilderPathConstants.SOURCE_PICTURES);
    File labelsFile=new File(_sourceDir,SiteBuilderPathConstants.LABELS_FILE);
    _labelManager=new SiteLabelsManager(labelsFile);
    File commentsFile=new File(_sourceDir,SiteBuilderPathConstants.COMMENTS_FILE);
    _imageCommentsManager=new ImageCommentsManager(commentsFile);
    _useFullTitle=false;
  }

  /**
   * Get the labels manager.
   * @return the labels manager.
   */
  public SiteLabelsManager getLabelManager()
  {
    return _labelManager;
  }

  /**
   * Get the comments manager.
   * @return the comments manager.
   */
  public ImageCommentsManager getCommentsManager()
  {
    return _imageCommentsManager;
  }

  /**
   * Indicates if pages should generate a long title or not.
   * @return <code>true</code> for a full/long title, <code>false</code> otherwise.
   */
  public boolean useFullTitle()
  {
    return _useFullTitle;
  }

  /**
   * Get the source directory.
   * @return the source directory.
   */
  public File getSourceDir()
  {
    return _sourceDir;
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
   * Indicates if pages should generate a link to the original pages or not.
   * @return <code>true</code> generates a link, <code>false</code> does nothing.
   */
  public boolean doLinksToOriginalImages()
  {
    return true;
  }
}
