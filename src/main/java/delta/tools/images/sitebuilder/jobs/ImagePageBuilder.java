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
import delta.tools.images.sitebuilder.SiteBuilderConfiguration;
import delta.tools.images.sitebuilder.SiteBuilderPathConstants;
import delta.tools.images.sitebuilder.SiteLabelsManager;
import delta.tools.images.sitebuilder.SitePreferences;
import delta.tools.images.sitebuilder.SiteStructure;

/**
 * HTML page builder for a single image.
 * @author DAM
 */
public class ImagePageBuilder implements JobImpl
{
  private static final Logger LOGGER=Logger.getLogger(ImagePageBuilder.class);

  private SiteBuilderConfiguration _config;
  private ImagePageInfo _info;
  
  /**
   * Constructor.
   * @param config Configuration.
   * @param info Image page information.
   */
  public ImagePageBuilder(SiteBuilderConfiguration config, ImagePageInfo info)
  {
    _config=config;
    _info=info;
  }

  public String getLabel()
  {
    ImageInfo info=_info.getImageInfo();
    String imageName=info.getName();
    return "Building HTML page for image "+imageName;
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
    SiteStructure siteStructure=_config.getSiteStructure();
    ImageInfo info=_info.getImageInfo();
    String imageName=info.getName();
    String comment=_config.getCommentsManager().getComment(imageName);
    Path sourcePath=_info.getSourcePath();
    Path sitePath=_info.getSitePath();
    File targetPath=new File(siteStructure.getSiteDir(),sitePath.getPath());
    File targetDir=new File(targetPath,SiteBuilderPathConstants.PAGES);
    targetDir.mkdirs();
    int index=_info.getIndex();
    File pageFile=new File(targetDir,index+".html");

    SitePreferences sitePreferences=new SitePreferences();
    try
    {
      int nbImages=_info.getTotalNumberOfImages();
      PrintWriter fw=new PrintWriter(pageFile);
      fw.println("<HTML>");
      fw.println("<meta http-equiv=\"Content-type\" content=\"text/html;charset=UTF-8\" />");
      fw.print("<HEAD><TITLE>");

      String title;
      int level=sitePath.getLevel();
      if (_config.useFullTitle())
      {
        StringBuilder tmp=new StringBuilder();
        String pathStr;
        for(int i=0;i<level;i++)
        {
          pathStr=labelsManager.getLabel(sitePath.getPath(i));
          tmp.append(pathStr).append(" - ");
        }
        tmp.append(index);
        tmp.append('/');
        tmp.append(nbImages);
        title=tmp.toString();
      }
      else
      {
        StringBuffer tmp=new StringBuffer();
        tmp.append(index);
        tmp.append('/');
        tmp.append(nbImages);
        title=tmp.toString();
      }
      fw.print(title);

      Dimension fullSize=new Dimension(info.getWidth(),info.getHeight());
      Dimension smallSize=sitePreferences.computeSmallImageDimension(fullSize);
      String pathToResources="../"+siteStructure.getRelativePathFromImagesDirToResources(sitePath);
      fw.println("</TITLE></HEAD>");
      fw.println("<BODY background=\""+pathToResources+"fond.jpg\">");
      fw.println("<CENTER>");
      fw.print("<H3>");
      if (index>1)
        fw.print("<A HREF=\""+(index-1)+".html\"><IMG SRC=\""+pathToResources
            +"l_hand.gif\" border=\"0\"></A> ");
      fw.print(index);
      fw.print(" / ");
      fw.print(nbImages);
      if (index<nbImages)
        fw.print(" <A HREF=\""+(index+1)+".html\"><IMG SRC=\""+pathToResources
            +"r_hand.gif\" border=\"0\"></A>");
      fw.println("</H3>");
      boolean doLink=_config.doLinksToOriginalImages();
      if (doLink)
      {
        fw.print("<A HREF=\"");
        String imagePath=siteStructure.getRelativePathFromImagesDirToSourceImages(sourcePath,sitePath)+imageName;
        fw.print(imagePath);
        fw.print("\">");
      }
      fw.print("<IMG SRC=\"");
      fw.print("../"+SiteBuilderPathConstants.IMAGES+'/'+imageName);
      fw.print("\" ALT=\"\" WIDTH=\"");
      fw.print(smallSize.width);
      fw.print("\" height=\"");
      fw.print(smallSize.height);
      fw.println("\" border=\"0\">");
      if (doLink)
      {
        fw.print("</A>");
      }

      if (comment!=null)
      {
        fw.print("<BR><B>");
        String htmlComment=HtmlConversions.stringToHtml(comment,true);
        fw.print(htmlComment);
        fw.print("</B>");
      }
      else
      {
        System.err.println("No comment for ["+imageName+"]");
      }

      fw.println("</CENTER>");

      fw.println("<HR WIDTH=\"100%\" size=\"1\" noshade=\"\">");

      fw.print("<B>");
      for(int i=0;i<=level;i++)
      {
        fw.print("<A HREF=\"");
        for(int j=0;j<level-i+1;j++)
        {
          fw.print("../");
        }
        fw.print("index.html\">");
        if (i==0)
        {
          fw.print(labelsManager.getHomeLabel());
        }
        else
        {
          fw.print(labelsManager.getLabel(sitePath.getPath(i-1)));
        }
        fw.print("</A>");
        fw.print(" :: ");
      }
      fw.print(index);
      fw.println("</B><BR>");

      fw.println("</BODY>");
      fw.println("</HTML>");
      fw.close();
    }
    catch (IOException e)
    {
      LOGGER.error("",e);
    }
  }
}
