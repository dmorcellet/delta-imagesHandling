package delta.tools.images.sitebuilder;

import java.io.File;
import java.util.HashMap;

import delta.common.utils.files.TextFileReader;

/**
 * Manages comments for a set of images.
 * @author DAM
 */
public class ImageCommentsManager
{
  private HashMap<String,String> _commentsMap;

  /**
   * Constructor.
   * @param commentsFile File where comments are read.
   */
  public ImageCommentsManager(File commentsFile)
  {
    _commentsMap=new HashMap<String,String>();
    TextFileReader reader=new TextFileReader(commentsFile);
    if (reader.start())
    {
      String line;
      String key,value;
      while (true)
      {
        line=reader.getNextLine();
        if (line==null) break;
        int index=line.indexOf('=');
        if (index!=-1)
        {
          key=line.substring(0,index);
          value=line.substring(index+1);
          _commentsMap.put(key,value);
        }
      }
      reader.terminate();
    }
  }

  /**
   * Get the comment for an image.
   * @param imageName Name of image.
   * @return A comment or <code>null</code> if none.
   */
  public String getComment(String imageName)
  {
    return _commentsMap.get(imageName);
  }
}
