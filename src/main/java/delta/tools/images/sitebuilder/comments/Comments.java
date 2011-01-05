package delta.tools.images.sitebuilder.comments;

import java.io.File;
import java.util.HashMap;

/**
 * Stores comments for a set of images.
 * @author DAM
 */
public class Comments
{
  private HashMap<File,String> _commentsByFile;
  private HashMap<String,String> _commentsByFileName;

  public Comments()
  {
    _commentsByFile=new HashMap<File,String>();
    _commentsByFileName=new HashMap<String,String>();
  }

  public void setComment(File file, String text)
  {
    _commentsByFile.put(file,text);
    String fileName=file.getName();
    String old=_commentsByFileName.put(fileName,text);
    if (old!=null)
    {
      System.err.println("There's already a comment for filename ["+fileName+"]");
    }
  }

  public String getComment(String file)
  {
    String ret=_commentsByFileName.get(file);
    return ret;
  }

  public String getComment(File file)
  {
    String ret=_commentsByFile.get(file);
    return ret;
  }
}
