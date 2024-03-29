package delta.tools.images.sitebuilder;

import java.io.File;
import java.util.HashMap;

import delta.common.utils.files.TextFileReader;

/**
 * Manages labels for a set of directories.
 * @author DAM
 */
public class SiteLabelsManager
{
  private HashMap<String,String> _labelsMap;

  /**
   * Constructor.
   * @param labelsFile File where labels are read.
   */
  public SiteLabelsManager(File labelsFile)
  {
    _labelsMap=new HashMap<String,String>();
    TextFileReader reader=new TextFileReader(labelsFile);
    if (reader.start())
    {
      while (true)
      {
        String line=reader.getNextLine();
        if (line==null) break;
        int index=line.indexOf('=');
        if (index!=-1)
        {
          String key=line.substring(0,index);
          String value=line.substring(index+1);
          _labelsMap.put(key,value);
        }
      }
      reader.terminate();
    }
  }

  /**
   * Get the label for a directory.
   * @param dirName Name of the directory.
   * @return A label or <code>dirName</code> if none.
   */
  public String getLabel(String dirName)
  {
    String value=_labelsMap.get(dirName);
    if (value==null) value=dirName;
    return value;
  }

  /**
   * Get the label for the home directory.
   * @return the label for the home directory.
   */
  public String getHomeLabel()
  {
    return getLabel("racine");
  }

  /**
   * Get the label for the "others" directory.
   * @return the label for the "others" directory.
   */
  public String getOtherLabel()
  {
    return getLabel("autres");
  }
}

