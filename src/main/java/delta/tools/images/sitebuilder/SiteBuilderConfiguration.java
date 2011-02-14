package delta.tools.images.sitebuilder;

import java.io.File;

/**
 * Configuration information for the site builder.
 * @author DAM
 */
public class SiteBuilderConfiguration
{
  private File _sourceDir;
  private boolean _useFullTitle;
  private SiteStructure _siteStructure;
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
    _siteStructure=new SiteStructure(to);
    File labelsFile=new File(_sourceDir,SiteBuilderPathConstants.LABELS_FILE);
    _labelManager=new SiteLabelsManager(labelsFile);
    File commentsFile=new File(_sourceDir,SiteBuilderPathConstants.COMMENTS_FILE);
    _imageCommentsManager=new ImageCommentsManager(_sourceDir,commentsFile);
    _useFullTitle=false;
  }

  /**
   * Get the site structure.
   * @return the site structure.
   */
  public SiteStructure getSiteStructure()
  {
    return _siteStructure;
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
   * Indicates if pages should generate a link to the original pages or not.
   * @return <code>true</code> generates a link, <code>false</code> does nothing.
   */
  public boolean doLinksToOriginalImages()
  {
    return true;
  }

  /**
   * Indicates if source images shall be copied to the target tree.
   * @return <code>true</code> to perform copy, <code>false</code> otherwise.
   */
  public boolean doCopySourceImages()
  {
    return false;
  }
}
