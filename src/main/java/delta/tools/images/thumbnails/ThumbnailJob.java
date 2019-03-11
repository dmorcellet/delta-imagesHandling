package delta.tools.images.thumbnails;

import java.io.File;
import java.text.DecimalFormat;

import delta.common.framework.jobs.JobImpl;
import delta.common.framework.jobs.JobSupport;

/**
 * Job to build a thumbnail.
 * @author DAM
 */
public class ThumbnailJob implements JobImpl
{
  private static int index=1;

  private ImageSizeComputer _config;
  private File _source;
  private File _target;
  private File _file;
  private DecimalFormat nbFormat=new DecimalFormat("000");

  /**
   * Constructor.
   * @param config Job configuration.
   * @param sourceDir Source file.
   * @param targetDir Target directory.
   * @param relativePath
   */
  public ThumbnailJob(ImageSizeComputer config, File sourceDir, File targetDir, File relativePath)
  {
    _config=config;
    _source=sourceDir;
    _target=targetDir;
    _file=relativePath;
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
    ThumbnailBuilder builder=new ThumbnailBuilder();
    File sourceFile=new File(_source,_file.getPath());
    String name=nbFormat.format(index)+" - "+_file.getName();
    File targetFile=new File(_target,name);
    targetFile.getParentFile().mkdirs();
    builder.build(sourceFile,targetFile,_config);
    index++;
  }

  public String getLabel()
  {
    return _file.getPath();
  }
}
