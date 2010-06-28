package delta.tools.images.sitebuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.utils.files.Path;
import delta.common.utils.time.chronometers.Chronometer;
import delta.common.utils.time.chronometers.ChronometerManager;
import delta.imaging.utils.ImagesHandlingLoggers;

/**
 * HTML page builder for a "choice" page.
 * @author DAM
 */
public class ChoicePage implements SiteBuilderTask
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
  public ChoicePage(SiteBuilderConfiguration config, Path path, List<String> dirNames)
  {
    _config=config;
    _path=path;
    _dirNames=dirNames;
    _targetPath=new File(config.getSiteDir(),path.getPath());
    StringBuffer tmp=new StringBuffer();
    int level=_path.getLevel();
    for(int i=0;i<level;i++) tmp.append("../");
    tmp.append(SiteBuilderPathConstants.RESOURCES);
    tmp.append(File.separatorChar);
    tmp.append(SiteBuilderPathConstants.IMAGE_RESOURCES);
    tmp.append(File.separatorChar);
    _pathToResources=tmp.toString();
  }

  /**
   * Do the job.
   */
  public void doIt()
  {
    ChronometerManager chronoMgr=ChronometerManager.getInstance();
    Chronometer c=chronoMgr.start("Choice page : "+_path);
    buildPage();
    chronoMgr.stopRemoveAndDump(c);
  }

  private void buildPage()
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
