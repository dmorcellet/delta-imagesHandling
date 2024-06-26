package delta.tools.images.sitebuilder;

import java.awt.Dimension;
import java.io.File;

import javax.media.jai.RenderedOp;

import org.apache.log4j.Logger;

import delta.common.framework.jobs.JobImpl;
import delta.common.framework.jobs.JobPool;
import delta.common.framework.jobs.JobSupport;
import delta.common.utils.files.FileCopy;
import delta.common.utils.files.Path;
import delta.tools.images.ImageTools;
import delta.tools.images.sitebuilder.jobs.ImageInfo;
import delta.tools.images.sitebuilder.jobs.ImagePageBuilder;
import delta.tools.images.sitebuilder.jobs.ImagePageInfo;
import delta.tools.images.sitebuilder.jobs.ImagesList;
import delta.tools.images.sitebuilder.jobs.ThumbsPageBuilder;

/**
 * HTML page builder for a set of "image" pages (thumbnails page and single-image pages).
 * @author DAM
 */
public class ImagePages implements JobImpl
{
  private static final Logger LOGGER=Logger.getLogger(ImagePages.class);

  private SiteBuilderConfiguration _config;
  private ImagesList _images;
  private File _sourcePath;
  private File _targetPath;
  private JobPool _pool;

  /**
   * Constructor.
   * @param config Site builder's configuration.
   * @param images Images to handle.
   * @param pool Job pool.
   */
  public ImagePages(SiteBuilderConfiguration config, ImagesList images, JobPool pool)
  {
    _config=config;
    _images=images;
    _pool=pool;
    Path sourcePath=_images.getSourcePath();
    Path sitePath=_images.getSitePath();
    SiteStructure siteStructure=config.getSiteStructure();
    _sourcePath=new File(_config.getSourceDir(),sourcePath.getPath());
    _targetPath=new File(siteStructure.getSiteDir(),sitePath.getPath());
  }

  public String getLabel()
  {
    return "Image pages for "+_images.getSitePath();
  }

  /**
   * Interrupt job.
   * @return Always <code>false</code>.
   */
  public boolean interrupt()
  {
    return false;
  }

  /**
   * Do the job.
   */
  public void doIt(JobSupport support)
  {
    Path path=_images.getSourcePath();
    if (LOGGER.isInfoEnabled())
    {
      LOGGER.info("Handle images for "+path);
    }
    _targetPath.mkdirs();
    buildImages();
    ThumbsPageBuilder thumbsPageJob=new ThumbsPageBuilder(_config,_images);
    _pool.addJob(thumbsPageJob);
    scheduleImagePagesComputation();
  }

  private void buildImages()
  {
    //ChronometerManager chronoMgr=ChronometerManager.getInstance();
    RenderedOp sourceImage;
    File sourceFile;

    File thumbTarget=new File(_targetPath,SiteBuilderPathConstants.THUMBNAILS);
    thumbTarget.mkdirs();
    File smallImgTarget=new File(_targetPath,SiteBuilderPathConstants.IMAGES);
    smallImgTarget.mkdirs();

    SitePreferences sitePreferences=new SitePreferences();
    int nbImages=_images.getNumberOfImages();
    for(int i=0;i<nbImages;i++)
    {
      ImageInfo info=_images.getImage(i);
      String imageName=info.getName();
      sourceFile=new File(_sourcePath,imageName);
      //Chronometer imgChrono=chronoMgr.start("Handle "+imageName);

      // Read image
      //Chronometer readChronometer=chronoMgr.start("read");
      sourceImage=ImageTools.readImage(sourceFile);
      //chronoMgr.stop(readChronometer);
      if (sourceImage==null) continue;

      // Compute dimensions
      info.setSize(sourceImage.getWidth(),sourceImage.getHeight());
      Dimension realImageDim=new Dimension(sourceImage.getWidth(),sourceImage.getHeight());

      // Handle thumbnail
      File targetThumbFile=new File(thumbTarget,imageName);
      if (!targetThumbFile.exists())
      {
        try
        {
          Dimension thumbnailDim=sitePreferences.computeThumbnailDimension(realImageDim);
          //Chronometer c=chronoMgr.start("thumbnail-"+imageName);
          if (thumbnailDim.equals(realImageDim))
          {
            // Simple copy
            //Chronometer copyChronometer=chronoMgr.start("copy");
            FileCopy.copyToDir(sourceFile,thumbTarget);
            //chronoMgr.stop(copyChronometer);
          }
          else
          {
            // Scale image
            //Chronometer scaleChronometer=chronoMgr.start("scale");
            RenderedOp scaledImage=ImageTools.scaleImage(sourceImage,thumbnailDim);
            //chronoMgr.stop(scaleChronometer);
            // Write image
            //Chronometer writeChronometer=chronoMgr.start("write");
            ImageTools.writeJPEGImage(scaledImage,targetThumbFile);
            //chronoMgr.stop(writeChronometer);
          }
          //chronoMgr.stop(c);
        }
        catch(Throwable t)
        {
          LOGGER.error("Error when building ["+targetThumbFile+"]",t);
        }
      }

      // Handle smallImage
      File targetSmallImageFile=new File(smallImgTarget,imageName);
      if (!targetSmallImageFile.exists())
      {
        try
        {
          Dimension smallImageDim=sitePreferences.computeSmallImageDimension(realImageDim);
          //Chronometer c=chronoMgr.start("smallImage-"+imageName);
          if (smallImageDim.equals(realImageDim))
          {
            // Simple copy
            //Chronometer copyChronometer=chronoMgr.start("copy");
            FileCopy.copyToDir(sourceFile,smallImgTarget);
            //chronoMgr.stop(copyChronometer);
          }
          else
          {
            // Scale image
            //Chronometer scaleChronometer=chronoMgr.start("scale");
            RenderedOp scaledImage=ImageTools.scaleImage(sourceImage,smallImageDim);
            //chronoMgr.stop(scaleChronometer);
            // Write image
            //Chronometer writeChronometer=chronoMgr.start("write");
            ImageTools.writeJPEGImage(scaledImage,targetSmallImageFile);
            //chronoMgr.stop(writeChronometer);
          }
          //chronoMgr.stop(c);
        }
        catch(Throwable t)
        {
          LOGGER.error("Error when building ["+targetSmallImageFile+"]",t);
        }
      }
      //chronoMgr.stopRemoveAndDump(imgChrono);
    }
  }

  private void scheduleImagePagesComputation()
  {
    int nb=_images.getNumberOfImages();
    for(int i=0;i<nb;i++)
    {
      ImagePageInfo pageJobInfo=new ImagePageInfo(_images,i);
      ImagePageBuilder job=new ImagePageBuilder(_config,pageJobInfo);
      _pool.addJob(job);
    }
  }
}
