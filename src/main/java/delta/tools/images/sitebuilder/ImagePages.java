package delta.tools.images.sitebuilder;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.media.jai.RenderedOp;

import org.apache.log4j.Logger;

import delta.common.utils.files.FileCopy;
import delta.common.utils.files.Path;
import delta.common.utils.time.chronometers.Chronometer;
import delta.common.utils.time.chronometers.ChronometerManager;
import delta.imaging.utils.ImagesHandlingLoggers;
import delta.tools.images.ImageTools;

/**
 * HTML page builder for a set of "image" pages (thumbnails page and single-image pages).
 * @author DAM
 */
public class ImagePages implements SiteBuilderTask
{
  private static final Logger _logger=ImagesHandlingLoggers.getImagesHandlingLogger();

  private SiteBuilderConfiguration _config;
  private Path _path;
  private File _sourcePath;
  private File _targetPath;
  private List<String> _imageNames;
  private Map<String,Dimension> _imageSizeMap;
  private Map<String,Dimension> _thumbnailSizeMap;
  private Map<String,Dimension> _smallImageSizeMap;
  private String _pathToSiteRoot;
  private String _pathToResources;

  private static final int MAX_WIDTH_THUMB=150;
  private static final int MAX_HEIGHT_THUMB=150;
  private static final int MAX_WIDTH_IMAGE=900;
  private static final int MAX_HEIGHT_IMAGE=600;
  private static final int THUMBS_PER_LINE=4;

  /**
   * Constructor.
   * @param config Site builder's configuration.
   * @param path Path of directory to use.
   * @param imageNames List of images to reference in the page.
   */
  public ImagePages(SiteBuilderConfiguration config, Path path,
      List<String> imageNames)
  {
    _config=config;
    _path=path;
    _imageNames=imageNames;
    _sourcePath=new File(config.getSourcePicturesDir(),path.getPath());
    _targetPath=new File(config.getSiteDir(),path.getPath());
    _imageSizeMap=new HashMap<String,Dimension>();
    _thumbnailSizeMap=new HashMap<String,Dimension>();
    _smallImageSizeMap=new HashMap<String,Dimension>();

    StringBuffer tmp=new StringBuffer();
    int level=_path.getLevel();
    for(int i=0;i<level;i++)
    {
      tmp.append("../");
    }
    _pathToSiteRoot=tmp.toString();
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
    if (_logger.isInfoEnabled())
    {
      _logger.info("Handle images for "+_path);
    }
    _targetPath.mkdirs();
    buildImages();
    buildThumbsPage();
    buildImagePages();
  }

  private Dimension computeThumbnailDimension(Dimension d)
  {
    int maxWidth=MAX_WIDTH_THUMB;
    int maxHeight=MAX_HEIGHT_THUMB;
    int width=d.width;
    int height=d.height;
    if (width>height)
    {
      float factor=(float)width/maxWidth;
      height=(int)(height/factor);
      width=maxWidth;
    }
    else
    {
      float factor=(float)height/maxHeight;
      width=(int)(width/factor);
      height=maxHeight;
    }
    return new Dimension(width,height);
  }

  private Dimension computeSmallImageDimension(Dimension d)
  {
    int maxWidth=MAX_WIDTH_IMAGE;
    int maxHeight=MAX_HEIGHT_IMAGE;
    int width=d.width;
    int height=d.height;

    float factor1=(float)width/maxWidth;
    float factor2=(float)height/maxHeight;
    if (factor1>factor2)
    {
      height=(int)(height/factor1);
      width=maxWidth;
    }
    else
    {
      float factor=(float)height/maxHeight;
      width=(int)(width/factor);
      height=maxHeight;
    }
    return new Dimension(width,height);
  }

  private void buildImages()
  {
    ChronometerManager chronoMgr=ChronometerManager.getInstance();
    String imageName;
    RenderedOp sourceImage;
    File sourceFile;
    Dimension realImageDim, thumbnailDim, smallImageDim;

    File thumbTarget=new File(_targetPath,SiteBuilderPathConstants.THUMBNAILS);
    thumbTarget.mkdirs();
    File smallImgTarget=new File(_targetPath,SiteBuilderPathConstants.IMAGES);
    smallImgTarget.mkdirs();

    for(Iterator<String> it=_imageNames.iterator();it.hasNext();)
    {
      imageName=it.next();
      sourceFile=new File(_sourcePath,imageName);
      Chronometer imgChrono=chronoMgr.start("Handle "+imageName);

      // Read image
      Chronometer readChronometer=chronoMgr.start("read");
      sourceImage=ImageTools.readImage(sourceFile);
      chronoMgr.stop(readChronometer);
      if (sourceImage==null) continue;

      // Compute dimensions
      realImageDim=new Dimension(sourceImage.getWidth(),sourceImage.getHeight());
      _imageSizeMap.put(imageName,realImageDim);

      // Handle thumbnail
      {
        thumbnailDim=computeThumbnailDimension(realImageDim);
        _thumbnailSizeMap.put(imageName,thumbnailDim);
        Chronometer c=chronoMgr.start("thumbnail-"+imageName);
        if (thumbnailDim.equals(realImageDim))
        {
          // Simple copy
          Chronometer copyChronometer=chronoMgr.start("copy");
          FileCopy.copyToDir(sourceFile,thumbTarget);
          chronoMgr.stop(copyChronometer);
        }
        else
        {
          // Scale image
          Chronometer scaleChronometer=chronoMgr.start("scale");
          RenderedOp scaledImage=ImageTools.scaleImage(sourceImage,thumbnailDim);
          chronoMgr.stop(scaleChronometer);
          // Write image
          Chronometer writeChronometer=chronoMgr.start("write");
          File to=new File(thumbTarget,imageName);
          ImageTools.writeJPEGImage(scaledImage,to);
          chronoMgr.stop(writeChronometer);
        }
        chronoMgr.stop(c);
      }

      // Handle smallImage
      {
        smallImageDim=computeSmallImageDimension(realImageDim);
        _smallImageSizeMap.put(imageName,smallImageDim);
        Chronometer c=chronoMgr.start("smallImage-"+imageName);
        if (smallImageDim.equals(realImageDim))
        {
          // Simple copy
          Chronometer copyChronometer=chronoMgr.start("copy");
          FileCopy.copyToDir(sourceFile,smallImgTarget);
          chronoMgr.stop(copyChronometer);
        }
        else
        {
          // Scale image
          Chronometer scaleChronometer=chronoMgr.start("scale");
          RenderedOp scaledImage=ImageTools.scaleImage(sourceImage,smallImageDim);
          chronoMgr.stop(scaleChronometer);
          // Write image
          Chronometer writeChronometer=chronoMgr.start("write");
          File to=new File(smallImgTarget,imageName);
          ImageTools.writeJPEGImage(scaledImage,to);
          chronoMgr.stop(writeChronometer);
        }
        chronoMgr.stop(c);
      }
      chronoMgr.stopRemoveAndDump(imgChrono);
    }
  }

  private void buildImagePages()
  {
    String imageName;
    int nb=_imageNames.size();
    int i=1;
    for(Iterator<String> it=_imageNames.iterator();it.hasNext();)
    {
      imageName=it.next();
      buildImagePage(imageName,i,nb);
      i++;
    }
  }

  private void buildImagePage(String imageName, int index, int nbImages)
  {
    SiteLabelsManager labelsManager=_config.getLabelManager();
    String comment=_config.getCommentsManager().getComment(imageName);
    File targetDir=new File(_targetPath,SiteBuilderPathConstants.PAGES);
    targetDir.mkdirs();
    File pageFile=new File(targetDir,index+".html");

    try
    {
      PrintWriter fw=new PrintWriter(pageFile);
      fw.println("<HTML>");
      fw.println("<meta http-equiv=\"Content-type\" content=\"text/html;charset=UTF-8\" />");
      fw.print("<HEAD><TITLE>");

      String title;
      int level=_path.getLevel();
      if (_config.useFullTitle())
      {
        StringBuffer tmp=new StringBuffer();
        String path;
        for(int i=0;i<level;i++)
        {
          path=labelsManager.getLabel(_path.getPath(i));
          tmp.append(path).append(" - ");
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

      Dimension d=_smallImageSizeMap.get(imageName);
      if (d==null) d=new Dimension(100,100);
      String pathToResources="../"+_pathToResources;
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
        String imagePath=_pathToSiteRoot+"../../"
            +SiteBuilderPathConstants.SOURCE_PICTURES+File.separator
            +_path.getPath()+File.separator+imageName;
        fw.print(imagePath.replace('\\','/'));
        fw.print("\">");
      }
      fw.print("<IMG SRC=\"");
      fw.print("../"+SiteBuilderPathConstants.IMAGES+'/'+imageName);
      fw.print("\" ALT=\"\" WIDTH=\"");
      fw.print(d.width);
      fw.print("\" height=\"");
      fw.print(d.height);
      fw.println("\" border=\"0\">");
      if (doLink)
      {
        fw.print("</A>");
      }

      if (comment!=null)
      {
        fw.print("<BR><B>");
        fw.print(comment);
        fw.print("</B>");
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
          fw.print(labelsManager.getLabel(_path.getPath(i-1)));
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
      _logger.error("",e);
    }
  }

  private void buildThumbsPage()
  {
    SiteLabelsManager labelsManager=_config.getLabelManager();
    ImageCommentsManager commentsManager=_config.getCommentsManager();

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
        for(int i=0;i<level;i++)
        {
          if (i>0) tmp.append(" - ");
          path=labelsManager.getLabel(_path.getPath(i));
          tmp.append(path);
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
          title=labelsManager.getLabel(_path.getPath(level-1));
        }
      }
      fw.print(title);
      fw.println("</TITLE></HEAD>");
      fw.println("<BODY background=\""+_pathToResources+"/fond.jpg\">");
      fw.println("<CENTER>");
      if (level>0)
      {
        fw.print("<H1><FONT COLOR=\"#cc66cc\">");
        fw.print("<B>");
        fw.print(title);
        fw.print("</B>");
        fw.println("</FONT></H1>");
      }
      fw
          .println("<TABLE cellpadding=\"2\" cellspacing=\"2\" border=\"0\" width=\"100%\">");
      fw.println("<TBODY>");

      int imagesInCurrentRow=0;
      String imageName;
      Dimension imgSize;
      int index=1;
      String comment;
      for(Iterator<String> it=_imageNames.iterator();it.hasNext();)
      {
        imageName=it.next();
        if (imagesInCurrentRow==0) fw.println("<TR>");
        imgSize=_thumbnailSizeMap.get(imageName);
        if (imgSize==null) imgSize=new Dimension(150,100);
        fw.print("<TD valign=\"Middle\" align=\"Center\"><A HREF=\"");
        fw.print(SiteBuilderPathConstants.PAGES+"/"+index+".html");
        fw.print("\"><IMG SRC=\"");
        fw.print(SiteBuilderPathConstants.THUMBNAILS+"/"+imageName);
        fw.print("\" ");
        comment=commentsManager.getComment(imageName);
        if (comment!=null) fw.print("TITLE=\""+comment+"\" ");
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
        index++;
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
    catch (IOException e)
    {
      _logger.error("",e);
    }
  }
}
