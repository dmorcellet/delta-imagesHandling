package delta.tools.images.sitebuilder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.framework.jobs.JobImpl;
import delta.common.framework.jobs.JobPool;
import delta.common.utils.files.FileCopy;
import delta.common.utils.files.Path;
import delta.common.utils.url.URLTools;
import delta.imaging.utils.ImagesHandlingLoggers;
import delta.tools.images.sitebuilder.jobs.ImagesList;
import delta.tools.images.sitebuilder.selection.FilesSelection;

/**
 * Initial job of the site builder.
 * This job is made of the following steps :
 * <ul>
 * <li>build the initial structure of the site (including shared ressources),
 * <li>analyze the images directory to build the list of jobs that need to be executed.
 * </ul>
 * @author DAM
 */
public class InitialJob implements JobImpl
{
  private static final Logger _logger=ImagesHandlingLoggers.getImagesHandlingLogger();

  private SiteBuilderConfiguration _config;
  private FilesSelection _filesFetcher;
  private JobPool _pool;

  /**
   * Constructor.
   * @param config Site builder's configuration.
   * @param pool Job pool.
   * @param filesFetcher USed to fetch the files to handle.
   */
  public InitialJob(SiteBuilderConfiguration config, JobPool pool, FilesSelection filesFetcher)
  {
    _config=config;
    _pool=pool;
    _filesFetcher=filesFetcher;
  }

  /**
   * Build the initial directory structure of the site.
   */
  private void buildTarget()
  {
    SiteStructure siteStructure=_config.getSiteStructure();
    File targetDir=siteStructure.getTargetDir();
    targetDir.mkdirs();
    File siteDir=siteStructure.getSiteDir();
    siteDir.mkdirs();
    File sourcePicturesDir=siteStructure.getSourcePicturesDir();
    sourcePicturesDir.mkdirs();

    File resources=new File(siteDir,SiteBuilderPathConstants.RESOURCES);
    File imageResources=new File(resources,SiteBuilderPathConstants.IMAGE_RESOURCES);
    imageResources.mkdirs();

    String imageResourcesDir=SiteBuilderPathConstants.RESOURCES+"/"+SiteBuilderPathConstants.IMAGE_RESOURCES;
    String[] filesToFind={"fond.jpg","l_hand.gif","r_hand.gif"};
    for(int i=0;i<filesToFind.length;i++)
    {
      String resource=imageResourcesDir+"/"+filesToFind[i];
      URL url=URLTools.getFromClassPath(resource,this);
      if (url!=null)
      {
        boolean copyOK=FileCopy.copyFromURL(url,new File(imageResources,filesToFind[i]));
        if (!copyOK)
        {
          _logger.warn("Cannot copy resource "+filesToFind[i]);
        }
      }
      else
      {
        _logger.warn("Cannot find URL for resource "+filesToFind[i]);
      }
    }
  }

  public String getLabel()
  {
    return "Initial job";
  }

  /**
   * Do the job.
   */
  public void doIt()
  {
    buildTarget();
    doIt(new Path());
  }

  /**
   * Handle a directory.
   * @param path Path of directory to handle.
   * @return <code>true</code> if this directory contains images or subdirectories, <code>false</code> otherwise.
   */
  private boolean doIt(Path path)
  {
    List<String> imgs=_filesFetcher.getImages(path.getPath());
    List<String> dirs=_filesFetcher.getDirectories(path.getPath());

    boolean ret=true;
    if ((dirs.size()==0) && (imgs.size()>0))
    {
      doImgDir(path,imgs);
    }
    else if ((dirs.size()>0) && (imgs.size()==0))
    {
      doDirDir(path,dirs);
    }
    else if ((imgs.size()>0) && (dirs.size()>0))
    {
      doMixedDir(path,imgs,dirs);
    }
    else
    {
      ret=false;
    }
    return ret;
  }

  /**
   * Handle a directory that contains images but no subdirectories.
   * @param path Path of directory to handle.
   * @param imgs List of images in this directory.
   */
  private void doImgDir(Path path, List<String> imgs)
  {
    boolean doCopy=_config.doCopySourceImages();
    ImagesList images=new ImagesList(path,path);
    if (doCopy)
    {
      SiteStructure siteStructure=_config.getSiteStructure();
      File sourceDir=new File(_config.getSourceDir(),path.getPath());
      File targetDir=new File(siteStructure.getSourcePicturesDir(),path.getPath());
      targetDir.mkdirs();
      for(Iterator<String> it=imgs.iterator();it.hasNext();)
      {
        String img=it.next();
        File from=new File(sourceDir,img);
        File to=new File(targetDir,img);
        FileCopy.copy(from,to);
      }
    }
    for(Iterator<String> it=imgs.iterator();it.hasNext();)
    {
      String img=it.next();
      images.addImage(img);
    }
    _pool.addJob(new ImagePages(_config,images,_pool));
  }

  /**
   * Handle a directory that contains subdirectories but no images.
   * @param path Path of directory to handle.
   * @param dirs List of directories in this directory.
   */
  private void doDirDir(Path path, List<String> dirs)
  {
    List<String> dirNames=new ArrayList<String>();
    for(Iterator<String> it=dirs.iterator();it.hasNext();)
    {
      String dir=it.next();
      Path newPath=path.buildChildPath(dir);
      boolean ok=doIt(newPath);
      if (ok)
      {
        dirNames.add(dir);
      }
    }
    _pool.addJob(new ChoicePageBuilder(_config,path,dirNames));
  }

  /**
   * Handle a directory that contains images and subdirectories.
   * @param path Path of directory to handle.
   * @param imgs List of images in this directory.
   * @param dirs List of directories in this directory.
   */
  private void doMixedDir(Path path, List<String> imgs, List<String> dirs)
  {
    boolean doCopy=_config.doCopySourceImages();
    // Copy images to sourceDirs/path/OTHER
    {
      Path otherPath=path.buildChildPath(SiteBuilderPathConstants.OTHER);
      ImagesList images=new ImagesList(path,otherPath);
      if (doCopy)
      {
        File sourceDir=new File(_config.getSourceDir(),path.getPath());
        SiteStructure siteStructure=_config.getSiteStructure();
        File targetDir=new File(siteStructure.getSourcePicturesDir(),path.getPath());
        File imgTargetDir=new File(targetDir,SiteBuilderPathConstants.OTHER);
        imgTargetDir.mkdirs();
        for(Iterator<String> it=imgs.iterator();it.hasNext();)
        {
          String img=it.next();
          File from=new File(sourceDir,img);
          File to=new File(imgTargetDir,img);
          FileCopy.copy(from,to);
        }
      }
      for(Iterator<String> it=imgs.iterator();it.hasNext();)
      {
        String img=it.next();
        images.addImage(img);
      }
      _pool.addJob(new ImagePages(_config,images,_pool));
    }
    // Handle dirs
    List<String> dirNames=new ArrayList<String>();
    String dir;
    Path newPath;
    for(Iterator<String> it=dirs.iterator();it.hasNext();)
    {
      dir=it.next();
      newPath=path.buildChildPath(dir);
      dirNames.add(dir);
      doIt(newPath);
    }
    dirNames.add(SiteBuilderPathConstants.OTHER);
    _pool.addJob(new ChoicePageBuilder(_config,path,dirNames));
  }
}
