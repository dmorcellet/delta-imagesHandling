package delta.tools.images.sitebuilder;

import java.io.File;
import java.util.List;

import delta.common.framework.jobs.JobPool;
import delta.common.framework.jobs.MultiThreadedJobExecutor;
import delta.common.framework.jobs.gui.swing.MultiThreadedProgressDialog;
import delta.common.utils.configuration.Configuration;
import delta.common.utils.configuration.Configurations;
import delta.common.utils.text.TextUtils;
import delta.tools.images.sitebuilder.selection.AutomaticFilesSelection;
import delta.tools.images.sitebuilder.selection.FilesSelection;
import delta.tools.images.sitebuilder.selection.ManualFilesSelection;

/**
 * Main class for the site builder.
 * @author DAM
 */
public class MainSiteBuilder
{
  private static final String SECTION="SITE_BUILDER";

  /**
   * Main method of the site builder.
   * @param args First argument is the source directory, second argument is the destination directory.
   */
  public static void main(String[] args)
  {
    Configuration cfg=Configurations.getConfiguration();
    File from=cfg.getFileValue(SECTION,"FROM_DIR",null);
    if (args.length>=1) from=new File(args[0]);
    if (from==null) return;

    File to=cfg.getFileValue(SECTION,"TO_DIR",null);
    if (args.length>=2) to=new File(args[1]);
    if (to==null) return;

    File selection=cfg.getFileValue(SECTION,"SELECTION",null);
    if (args.length>=3) selection=new File(args[2]);

    SiteBuilderConfiguration config=new SiteBuilderConfiguration(from,to);
    JobPool pool=new JobPool();
    FilesSelection filesFetcher;
    if (selection!=null)
    {
      List<String> files=TextUtils.readAsLines(selection);
      filesFetcher=new ManualFilesSelection(files);
    }
    else
    {
      filesFetcher=new AutomaticFilesSelection(config.getSourceDir());
    }
    InitialJob job=new InitialJob(config,pool,filesFetcher);
    pool.addJob(job);
    MultiThreadedJobExecutor executor=new MultiThreadedJobExecutor(pool,2);
    MultiThreadedProgressDialog progressDialog=new MultiThreadedProgressDialog(executor);
    progressDialog.start();
    executor.start();
    executor.waitForCompletion();
    progressDialog.stop();
  }
}
