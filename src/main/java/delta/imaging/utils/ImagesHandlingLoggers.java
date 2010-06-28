package delta.imaging.utils;

import org.apache.log4j.Logger;

import delta.common.utils.traces.LoggersRegistry;

/**
 * Management class for all images-handling loggers.
 * @author DAM
 */
public abstract class ImagesHandlingLoggers
{
  /**
   * Name of the "IMAGES_HANDLING" logger.
   */
  public static final String IMAGES_HANDLING="IMAGES_HANDLING";

  private static final Logger _imagesHandlingLogger=LoggersRegistry.getLogger(IMAGES_HANDLING);

  /**
   * Get the logger used for images handling (IMAGES_HANDLING).
   * @return the logger used for images handling.
   */
  public static Logger getImagesHandlingLogger()
  {
    return _imagesHandlingLogger;
  }
}
