package delta.tools.images.sitebuilder;

import java.io.File;

import delta.common.framework.jobs.JobPool;
import delta.common.framework.jobs.MultiThreadedJobExecutor;
import delta.common.framework.jobs.gui.swing.MultiThreadedProgressDialog;
import delta.common.utils.configuration.Configuration;
import delta.common.utils.configuration.Configurations;

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

    SiteBuilderConfiguration config=new SiteBuilderConfiguration(from,to);
    JobPool pool=new JobPool();
    InitialJob job=new InitialJob(config,pool);
    pool.addJob(job);
    MultiThreadedJobExecutor executor=new MultiThreadedJobExecutor(pool,2);
    MultiThreadedProgressDialog progressDialog=new MultiThreadedProgressDialog(executor);
    progressDialog.start();
    executor.start();
    executor.waitForCompletion();
    progressDialog.stop();
  }
}
