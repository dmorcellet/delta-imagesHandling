package delta.tools.images.thumbnails;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

import delta.common.framework.jobs.JobPool;
import delta.common.framework.jobs.MultiThreadedJobExecutor;
import delta.common.framework.jobs.gui.swing.MultiThreadedProgressDialog;
import delta.common.utils.files.FilesDeleter;
import delta.common.utils.files.FilesFinder;
import delta.common.utils.files.filter.CompoundPredicate;
import delta.common.utils.files.filter.ExtensionPredicate;
import delta.tools.images.thumbnails.hmi.ConfigurationDialog;

/**
 * Main class for the thumbnails builder.
 * @author DAM
 */
public class ThumbnailsMain
{
  /**
   * Constructor.
   */
  public ThumbnailsMain()
  {
    // Nothing to do here
  }

  /**
   * Does the job, that is :
   * <ul>
   * <li>invoke the GUI,
   * <li>compute reduced images.
   * </ul>
   *
   */
  void go()
  {
    ConfigurationDialog d=new ConfigurationDialog();
    d.setModal(true);
    d.setVisible(true);
    if (d.getExitStatus())
    {
      ImageSizeComputer config=d.getConfiguration();
      File sourceDir=d.getSourcePath();
      File targetDir=d.getTargetPath();
      doTheJob(config,sourceDir,targetDir);
    }
    else
    {
    	System.exit(0);
    }
  }

  private void doTheJob(ImageSizeComputer config, File sourceDir, File targetDir)
  {
    // Cleaning
    FilesDeleter filesDeleter=new FilesDeleter(targetDir,null,true);
    filesDeleter.doIt();
    // Rebuild
    targetDir.mkdirs();

    List<File> filesToHandle=computeFiles(sourceDir);
    JobPool pool=new JobPool();
    ThumbnailJob job;
    File current;
    for(Iterator<File> it=filesToHandle.iterator();it.hasNext();)
    {
      current=it.next();
      job=new ThumbnailJob(config,sourceDir,targetDir,current);
      pool.addJob(job);
    }
    int nbThreads=getNbThreads();
    int nbJobs=filesToHandle.size();
    if (nbThreads>nbJobs) nbThreads=nbJobs;
    MultiThreadedJobExecutor exec=new MultiThreadedJobExecutor(pool,nbThreads);
    MultiThreadedProgressDialog dialog=new MultiThreadedProgressDialog(exec);
    dialog.start();
    exec.start();
  }

  private List<File> computeFiles(File source)
  {
    FileFilter predicate=new CompoundPredicate(new ExtensionPredicate("jpg",false),new ExtensionPredicate("jpeg",false),CompoundPredicate.MODE_OR);
    FilesFinder finder=new FilesFinder();
    return finder.find(FilesFinder.RELATIVE_MODE,source,predicate,true);
  }

  private static int getNbThreads()
  {
    //int nbProcessors=Runtime.getRuntime().availableProcessors();
    //return nbProcessors;
    return 1;
  }

  /**
   * Main method for this tool.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        new ThumbnailsMain().go();
      }
    });
  }
}
