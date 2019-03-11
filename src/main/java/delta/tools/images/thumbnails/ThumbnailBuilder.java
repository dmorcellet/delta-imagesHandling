package delta.tools.images.thumbnails;

import java.awt.Dimension;
import java.io.File;

import javax.media.jai.RenderedOp;

import org.apache.log4j.Logger;

import delta.common.utils.time.chronometers.Chronometer;
import delta.common.utils.time.chronometers.ChronometerManager;
import delta.imaging.utils.ImagesHandlingLoggers;
import delta.tools.images.ImageTools;

/**
 * Thumbnails builder.
 * @author DAM
 */
public class ThumbnailBuilder
{
  private static final Logger _logger=ImagesHandlingLoggers.getImagesHandlingLogger();

  /**
   * Build a thumbnail file from an image file.
   * @param sourcePath Source image file.
   * @param destPath Thumbnail file to write.
   * @param config Thumbnail size computer.
   * @return <code>true</code> if operation was successful, <code>false</code> otherwise.
   */
  public boolean build(File sourcePath, File destPath, ImageSizeComputer config)
  {
    boolean ok=false;
    try
    {
      ChronometerManager cm=ChronometerManager.getInstance();
      Chronometer globalC=cm.start("build");
      // Reading
      RenderedOp sourceImage=null;
      {
        Chronometer c=cm.start("read");
        sourceImage=ImageTools.readImage(sourcePath);
        cm.stop(c);
        if (sourceImage==null) return false;
      }
      // Scaling
      RenderedOp scaledImage=null;
      {
        Chronometer c=cm.start("scale");
        int width=sourceImage.getWidth();
        int height=sourceImage.getHeight();
        Dimension newDimensions=config.computeNewSize(new Dimension(width,height));
        scaledImage=ImageTools.scaleImage(sourceImage,newDimensions);
        cm.stop(c);
      }

      // Encoding
      {
        Chronometer c=cm.start("write");
        ImageTools.writeJPEGImage(scaledImage,destPath);
        cm.stop(c);
      }
      cm.stopRemoveAndDump(globalC);
    }
    catch(Exception e)
    {
      _logger.error("",e);
    }
    return ok;
  }
}
