package delta.tools.images.sitebuilder;

import java.io.File;

import delta.common.utils.files.Path;

/**
 * @author DAM
 */
public class SiteStructure
{
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
