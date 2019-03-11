package delta.tools.images;

import java.awt.Dimension;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import org.apache.log4j.Logger;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncodeParam;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.JPEGEncodeParam;

import delta.common.utils.io.StreamTools;
import delta.common.utils.traces.UtilsLoggers;

/**
 * Tool methods for reading, scaling and writing images.
 * @author DAM
 */
public class ImageTools
{
  private static final Logger _logger=UtilsLoggers.getUtilsLogger();

  /**
   * Read an image from a file.
   * @param sourcePath Source file.
   * @return A JAI image or <code>null</code> if an error occurred.
   */
  public static RenderedOp readImage(File sourcePath)
  {
    RenderedOp sourceImage=null;
    FileSeekableStream stream=null;
    try
    {
      stream=new FileSeekableStream(sourcePath);
      sourceImage=JAI.create("stream",stream);
    }
    catch (IOException ioe)
    {
      _logger.error("",ioe);
    }
    finally
    {
      //StreamTools.close(stream);
    }
    return sourceImage;
  }

  /**
   * Scale an image.
   * @param sourceImage Source image.
   * @param newDimensions New image dimensions.
   * @return A new JAI image.
   */
  public static RenderedOp scaleImage(RenderedOp sourceImage, Dimension newDimensions)
  {
    if (sourceImage==null) return null;
    RenderedOp scaledImage=null;
    Interpolation interp=Interpolation.getInstance(Interpolation.INTERP_BICUBIC_2);
    ParameterBlock params=new ParameterBlock();
    int width=sourceImage.getWidth();
    int height=sourceImage.getHeight();
    int newWidth=newDimensions.width;
    int newHeight=newDimensions.height;
    params.addSource(sourceImage);
    float xFactor=((float)newWidth/(float)width);
    params.add(xFactor); // x scale factor
    float yFactor=((float)newHeight/(float)height);
    params.add(yFactor); // x scale factor
    params.add(0.0F); // x translate
    params.add(0.0F); // y translate
    params.add(interp); // interpolation method
    scaledImage=JAI.create("scale",params);
    return scaledImage;
  }

  /**
   * Write an image as a JPG file.
   * @param image Source image.
   * @param to File to write.
   * @return <code>true</code> if operation was successful, <code>false</code> otherwise.
   */
  public static boolean writeJPEGImage(RenderedOp image, File to)
  {
    if (image==null) return false;
    if (to==null) return false;
    to.getParentFile().mkdirs();

    // Set the encoding parameters if necessary.
    JPEGEncodeParam encodeParam=new JPEGEncodeParam();
    encodeParam.setQuality(0.75F);
    encodeParam.setHorizontalSubsampling(0,1);
    encodeParam.setHorizontalSubsampling(1,2);
    encodeParam.setHorizontalSubsampling(2,2);
    encodeParam.setVerticalSubsampling(0,1);
    encodeParam.setVerticalSubsampling(1,1);
    encodeParam.setVerticalSubsampling(2,1);
    encodeParam.setRestartInterval(64);

    FileOutputStream out=null;
    try
    {
      out=new FileOutputStream(to);
      ImageEncodeParam params=encodeParam;
      ImageEncoder encoder=ImageCodec.createImageEncoder("JPEG",out,params);
      encoder.encode(image);
    }
    catch (IOException ioe)
    {
      _logger.error("",ioe);
    }
    finally
    {
      StreamTools.close(out);
    }
    return true;
  }
}
