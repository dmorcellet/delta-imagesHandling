package delta.utils.images;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGDecodeParam;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

import delta.common.utils.files.iterator.AbstractFileIteratorCallback;
import delta.common.utils.files.iterator.FileIterator;

/**
 * Extractor for JPEG infos.
 * @author DAM
 */
public class JPEGInfosExtractor extends AbstractFileIteratorCallback
{
  private static final Logger LOGGER=Logger.getLogger(JPEGInfosExtractor.class);

  /**
   * Constructor.
   */
  public JPEGInfosExtractor()
  {
  	// Nothing special to do
  }

  /**
   * Do it!
   * @param path Root path.
   */
  public void go(String path)
  {
    FileIterator fi=new FileIterator(new File(path), true, this);
    fi.run();
  }

  @Override
  public void handleFile(File absolute, File relative)
  {
    if((absolute.getPath().endsWith(".jpg"))||(absolute.getPath().endsWith(".JPG")))
    {
      try
      {
        handleImage(absolute);
      }
      catch(Exception e)
      {
        LOGGER.error("Error when handling file "+absolute, e);
      }
    }
  }

  private void decodeExif(byte[] data)
  {
    if (data.length<6) return;
    byte[] exif={69,120,105,102,0,0};
    for(int i=0;i<6;i++)
    {
      if (exif[i]!=data[i]) return;
    }
    System.out.println("EXIF OK");
  }

  /**
   * Handle an image file.
   * @param file File to handle.
   */
  public void handleImage(File file)
  {
    try
    {
      DataInputStream in=new DataInputStream(new FileInputStream(file));
      JPEGImageDecoder decoder=JPEGCodec.createJPEGDecoder(in);
      decoder.decodeAsBufferedImage();
      JPEGDecodeParam param=decoder.getJPEGDecodeParam();
      byte[][] data=param.getMarkerData(JPEGDecodeParam.APP1_MARKER);
      in.close();
      int length=0;
      int nbParts=0;
      int notNullPart=0;
      byte[] realData=null;
      for(int i=0;i<data.length;i++)
      {
        if (data[i]!=null)
        {
          System.out.println("Data "+i+" : "+data[i].length);
          length+=data[i].length;
          nbParts++;
          notNullPart=i;
        }
      }
      if (nbParts>1)
      {
        // Concatenation
        realData=new byte[length];
        int offset=0;
        for(int i=0;i<data.length;i++)
        {
          if (data[i]!=null)
          {
            System.arraycopy(data[i],0,realData,offset,data[i].length);
            offset+=data[i].length;
          }
        }
      }
      else if (nbParts==1)
      {
        realData=data[notNullPart];
      }
      if (realData!=null)
      {
        decodeExif(realData);
      }
    }
    catch(IOException ioException)
    {
      LOGGER.error("Error when handling file "+file, ioException);
    }
  }

  /**
   * 'enter directory' callback.
   * @param f Targeted directory.
   */
  public void enterDirectory(File f)
  {
  	// Nothing special to do
  }

  /**
   * 'leave directory' callback.
   * @param f Targeted directory.
   */
  public void leaveDirectory(File f)
  {
  	// Nothing special to do
  }

  /**
   * Main method for this tool.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    long time1=System.currentTimeMillis();
    JPEGInfosExtractor main=new JPEGInfosExtractor();
    main.go("D:\\Donnees\\transfert\\incoming\\portable\\Donnees\\docs\\conneries\\peluches");
    long time2=System.currentTimeMillis();
    System.out.println("Took : "+(time2-time1)+"ms");
  }
}
