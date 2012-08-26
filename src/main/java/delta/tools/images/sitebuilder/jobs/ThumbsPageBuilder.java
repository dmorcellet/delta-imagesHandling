package delta.tools.images.sitebuilder.jobs;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

import delta.common.framework.jobs.JobImpl;
import delta.common.framework.jobs.JobSupport;
import delta.common.utils.files.Path;
import delta.common.utils.html.HtmlConversions;
import delta.imaging.utils.ImagesHandlingLoggers;
import delta.tools.images.sitebuilder.ImageCommentsManager;
import delta.tools.images.sitebuilder.SiteBuilderConfiguration;
import delta.tools.images.sitebuilder.SiteBuilderPathConstants;
import delta.tools.images.sitebuilder.SiteLabelsManager;
import delta.tools.images.sitebuilder.SitePreferences;
import delta.tools.images.sitebuilder.SiteStructure;

/**
 * Builder for the thumbs page.
 * @author DAM
 */
public class ThumbsPageBuilder implements JobImpl
{
  private static final Logger _logger=ImagesHandlingLoggers.getImagesHandlingLogger();
  private static final int THUMBS_PER_LINE=4;

  private SiteBuilderConfiguration _config;
  private ImagesList _images;

  /**
   * Constructor.
   * @param config Configuration.
   * @param images Images to use.
   */
  public ThumbsPageBuilder(SiteBuilderConfiguration config, ImagesList images)
  {
    _config=config;
    _images=images;
  }

  public String getLabel()
  {
    Path path=_images.getSitePath();
    return "Building thumbs page for directory "+path;
  }

  /**
   * Interrupt job.
   * @return Always <code>false</code>.
   */
  public boolean interrupt()
  {
    return false;
  }

  public void doIt(JobSupport support)
  {
    SiteLabelsManager labelsManager=_config.getLabelManager();
    ImageCommentsManager commentsManager=_config.getCommentsManager();
    SiteStructure siteStructure=_config.getSiteStructure();

    SitePreferences sitePreferences=new SitePreferences();
    Path path=_images.getSitePath();
    File targetPath=new File(siteStructure.getSiteDir(),path.getPath());
    File indexFile=new File(targetPath,"index.html");
    try
    {
      PrintWriter fw=new PrintWriter(indexFile);
      fw.println("<HTML>");
      fw.println("<meta http-equiv=\"Content-type\" content=\"text/html;charset=UTF-8\" />");
      fw.print("<HEAD><TITLE>");
      String title="";
      int level=path.getLevel();
      if (_config.useFullTitle())
      {
        StringBuffer tmp=new StringBuffer();
        String pathStr;
        for(int i=0;i<level;i++)
        {
          if (i>0) tmp.append(" - ");
          pathStr=labelsManager.getLabel(path.getPath(i));
          tmp.append(pathStr);
        }
        title=tmp.toString();
      }
      else
      {
        if (level==0)
        {
          title=labelsManager.getHomeLabel();
        }
        else
        {
          title=labelsManager.getLabel(path.getPath(level-1));
        }
      }
      fw.print(title);
      fw.println("</TITLE></HEAD>");
      String pathToResources=siteStructure.getRelativePathFromImagesDirToResources(_images.getSitePath());
      fw.println("<BODY background=\""+pathToResources+"/fond.jpg\">");
      fw.println("<CENTER>");
      if (level>0)
      {
        fw.print("<H1><FONT COLOR=\"#cc66cc\">");
        fw.print("<B>");
        fw.print(title);
        fw.print("</B>");
        fw.println("</FONT></H1>");
      }
      fw.println("<TABLE cellpadding=\"2\" cellspacing=\"2\" border=\"0\" width=\"100%\">");
      fw.println("<TBODY>");

      int imagesInCurrentRow=0;
      Dimension imgSize;
      String comment;
      int nbImages=_images.getNumberOfImages();
      for(int i=0;i<nbImages;i++)
      {
        ImageInfo imageInfo=_images.getImage(i);
        String imageName=imageInfo.getName();
        Dimension realSize=new Dimension(imageInfo.getWidth(),imageInfo.getHeight());
        imgSize=sitePreferences.computeThumbnailDimension(realSize);
        if (imagesInCurrentRow==0) fw.println("<TR>");
        fw.print("<TD valign=\"Middle\" align=\"Center\"><A HREF=\"");
        fw.print(SiteBuilderPathConstants.PAGES+"/"+(i+1)+".html");
        fw.print("\"><IMG SRC=\"");
        fw.print(SiteBuilderPathConstants.THUMBNAILS+"/"+imageName);
        fw.print("\" ");
        comment=commentsManager.getComment(imageName);
        if (comment!=null)
        {
          String htmlComment=HtmlConversions.stringToHtml(comment,false);
          fw.print("TITLE=\""+htmlComment+"\" ");
        }
        fw.print("WIDTH=\"");
        fw.print(imgSize.width);
        fw.print("\" height=\"");
        fw.print(imgSize.height);
        fw.println("\" border=\"0\"></A></TD>");
        imagesInCurrentRow++;
        if (imagesInCurrentRow==THUMBS_PER_LINE)
        {
          fw.println("</TR>");
          imagesInCurrentRow=0;
        }
      }
      fw.println("</TBODY>");
      fw.println("</TABLE>");
      fw.println("</CENTER>");
      fw.println("<HR WIDTH=\"100%\" size=\"1\" noshade=\"\">");

      fw.print("<B>");
      for(int i=0;i<=level;i++)
      {
        if (i>0) fw.print(" :: ");
        if (i!=level)
        {
          fw.print("<A HREF=\"");
          for(int j=0;j<level-i;j++)
            fw.print("../");
          fw.print("index.html\">");
        }
        if (i==0)
          fw.print(labelsManager.getHomeLabel());
        else
          fw.print(labelsManager.getLabel(path.getPath(i-1)));
        if (i!=level)
        {
          fw.print("</A>");
        }
      }
      fw.println("</B><BR>");
      fw.println("</BODY>");
      fw.println("</HTML>");
      fw.close();
    }
    catch (IOException e)
    {
      _logger.error("",e);
    }
  }
}
