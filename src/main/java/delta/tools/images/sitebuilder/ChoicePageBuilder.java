package delta.tools.images.sitebuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.framework.jobs.JobImpl;
import delta.common.utils.files.Path;
import delta.imaging.utils.ImagesHandlingLoggers;

/**
 * HTML page builder for a "choice" page.
 * @author DAM
 */
public class ChoicePageBuilder implements JobImpl
{
  private static final Logger _logger=ImagesHandlingLoggers.getImagesHandlingLogger();

  private SiteBuilderConfiguration _config;
  private Path _path;
  private File _targetPath;
  private List<String> _dirNames;
  private String _pathToResources;

  /**
   * Constructor.
   * @param config Site builder's configuration.
   * @param path Path of directory to use.
   * @param dirNames List of directories to reference in the page.
   */
  public ChoicePageBuilder(SiteBuilderConfiguration config, Path path, List<String> dirNames)
  {
    _config=config;
    _path=path;
    _dirNames=dirNames;
    SiteStructure siteStructure=config.getSiteStructure();
    _targetPath=new File(siteStructure.getSiteDir(),path.getPath());
    StringBuffer tmp=new StringBuffer();
    int level=_path.getLevel();
    for(int i=0;i<level;i++) tmp.append("../");
    tmp.append(SiteBuilderPathConstants.RESOURCES);
    tmp.append(File.separatorChar);
    tmp.append(SiteBuilderPathConstants.IMAGE_RESOURCES);
    tmp.append(File.separatorChar);
    _pathToResources=tmp.toString();
  }

  public String getLabel()
  {
    return "Choice page for "+_path;
  }

  /**
   * Do the job.
   */
  public void doIt()
  {
    SiteLabelsManager labelsManager=_config.getLabelManager();
    _targetPath.mkdirs();
    File indexFile=new File(_targetPath,"index.html");
    try
    {
      PrintWriter fw=new PrintWriter(indexFile);
      fw.println("<HTML>");
      fw.println("<meta http-equiv=\"Content-type\" content=\"text/html;charset=UTF-8\" />");
      fw.print("<HEAD><TITLE>");

      String title="";
      int level=_path.getLevel();
      if (_config.useFullTitle())
      {
        StringBuffer tmp=new StringBuffer();
        String path;
        String pathPart;
        for(int i=0;i<level;i++)
        {
          pathPart=_path.getPath(i);
          if (i>0) tmp.append(" - ");
          path=labelsManager.getLabel(pathPart);
          tmp.append(path);
        }
        title=tmp.toString();
      }
      else
      {
        if (level==0)
          title=labelsManager.getHomeLabel();
        else
          title=labelsManager.getLabel(_path.getPath(level-1));
      }
      fw.print(title);
      fw.println("</TITLE></HEAD>");
      fw.println("<BODY background=\""+_pathToResources+"/fond.jpg\">");
      fw.println("<CENTER>");
      {
        fw.print("<H1><FONT COLOR=\"#cc66cc\">");
        fw.print("<B>");
        fw.print(title);
        fw.print("</B>");
        fw.println("</FONT></H1>");
      }
      String dirName;
      fw.println("<UL>");
      for(Iterator<String> it=_dirNames.iterator();it.hasNext();)
      {
        dirName=it.next();
        fw.print("<LI>");
        fw.print("<A HREF=\"");
        fw.print(dirName+"/index.html\">");
        fw.print(labelsManager.getLabel(dirName));
        fw.println("</A>");
      }
      fw.println("</UL>");
      fw.println("</CENTER>");
      fw.println("<HR WIDTH=\"100%\" size=\"1\" noshade=\"\">");

      fw.print("<B>");
      for(int i=0;i<=level;i++)
      {
        if (i>0) fw.print(" :: ");
        if (i!=level)
        {
          fw.print("<A HREF=\"");
          for(int j=0;j<level-i;j++) fw.print("../");
          fw.print("index.html\">");
        }
        if (i==0)
          fw.print(labelsManager.getHomeLabel());
        else
          fw.print(labelsManager.getLabel(_path.getPath(i-1)));
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
    catch(IOException e)
    {
      _logger.error("",e);
    }
  }
}
