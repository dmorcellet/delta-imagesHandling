package delta.tools.images.sitebuilder;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.framework.jobs.JobImpl;
import delta.common.framework.jobs.JobPool;
import delta.common.utils.files.FileCopy;
import delta.common.utils.files.FilesFinder;
import delta.common.utils.files.Path;
import delta.common.utils.files.filter.CompoundPredicate;
import delta.common.utils.files.filter.ExtensionPredicate;
import delta.common.utils.files.filter.FileTypePredicate;
import delta.common.utils.url.URLTools;
import delta.imaging.utils.ImagesHandlingLoggers;
import delta.tools.images.sitebuilder.jobs.ImagesList;

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
  private FileFilter _imgFilter;
  private FileFilter _dirFilter;
  private JobPool _pool;

  /**
   * Constructor.
   * @param config Site builder's configuration.
   * @param pool Job pool.
   */
  public InitialJob(SiteBuilderConfiguration config, JobPool pool)
  {
    _config=config;
    _pool=pool;
    FileFilter jpgFilter=new ExtensionPredicate("JPG",false);
    FileFilter jpegFilter=new ExtensionPredicate("JPEG",false);
    _imgFilter=new CompoundPredicate(jpgFilter,jpegFilter,CompoundPredicate.MODE_OR);
    _dirFilter=new FileTypePredicate(FileTypePredicate.DIRECTORY);
  }

  /**
   * Build the initial directory structure of the site.
   */
  private void buildTarget()
  {
    File targetDir=_config.getTargetDir();
    targetDir.mkdirs();
    File siteDir=_config.getSiteDir();
    siteDir.mkdirs();
    File sourcePicturesDir=_config.getSourcePicturesDir();
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
    FilesFinder finder=new FilesFinder();
    File root=new File(_config.getSourceDir(),path.getPath());
    List<File> imgs=finder.find(FilesFinder.RELATIVE_MODE,root,_imgFilter,false);
    List<File> dirs=finder.find(FilesFinder.RELATIVE_MODE,root,_dirFilter,false);

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
  private void doImgDir(Path path, List<File> imgs)
  {
    ImagesList images=new ImagesList(path);
    File sourceDir=new File(_config.getSourceDir(),path.getPath());
    File targetDir=new File(_config.getSourcePicturesDir(),path.getPath());
    targetDir.mkdirs();
    File img;
    File from,to;
    for(Iterator<File> it=imgs.iterator();it.hasNext();)
    {
      img=it.next();
      from=new File(sourceDir,img.getName());
      to=new File(targetDir,img.getName());
      FileCopy.copy(from,to);
      images.addImage(img.getName());
    }
    _pool.addJob(new ImagePages(_config,images,_pool));
  }

  /**
   * Handle a directory that contains subdirectories but no images.
   * @param path Path of directory to handle.
   * @param dirs List of directories in this directory.
   */
  private void doDirDir(Path path, List<File> dirs)
  {
    File targetDir=new File(_config.getSourcePicturesDir(),path.getPath());
    targetDir.mkdirs();
    File dir;
    Path newPath;
    List<String> dirNames=new ArrayList<String>();
    for(Iterator<File> it=dirs.iterator();it.hasNext();)
    {
      dir=it.next();
      newPath=path.buildChildPath(dir.getName());
      boolean ok=doIt(newPath);
      if (ok)
      {
        dirNames.add(dir.getName());
      }
    }
    Collections.sort(dirNames);
    _pool.addJob(new ChoicePageBuilder(_config,path,dirNames));
  }

  /**
   * Handle a directory that contains images and subdirectories.
   * @param path Path of directory to handle.
   * @param imgs List of images in this directory.
   * @param dirs List of directories in this directory.
   */
  private void doMixedDir(Path path, List<File> imgs, List<File> dirs)
  {
    File sourceDir=new File(_config.getSourceDir(),path.getPath());
    File targetDir=new File(_config.getSourcePicturesDir(),path.getPath());
    // Copy images to sourceDirs/path/OTHER
    {
      Path otherPath=path.buildChildPath(SiteBuilderPathConstants.OTHER);
      ImagesList images=new ImagesList(otherPath);
      File imgTargetDir=new File(targetDir,SiteBuilderPathConstants.OTHER);
      imgTargetDir.mkdirs();
      File img;
      File from,to;
      for(Iterator<File> it=imgs.iterator();it.hasNext();)
      {
        img=it.next();
        from=new File(sourceDir,img.getName());
        to=new File(imgTargetDir,img.getName());
        FileCopy.copy(from,to);
        images.addImage(img.getName());
      }
      _pool.addJob(new ImagePages(_config,images,_pool));
    }
    // Handle dirs
    List<String> dirNames=new ArrayList<String>();
    File dir;
    Path newPath;
    for(Iterator<File> it=dirs.iterator();it.hasNext();)
    {
      dir=it.next();
      newPath=path.buildChildPath(dir.getName());
      dirNames.add(dir.getName());
      doIt(newPath);
    }
    Collections.sort(dirNames);
    dirNames.add("autres");
    _pool.addJob(new ChoicePageBuilder(_config,path,dirNames));
  }
}
