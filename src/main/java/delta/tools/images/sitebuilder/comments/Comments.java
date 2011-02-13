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

  /**
   * Constructor.
   */
  public Comments()
  {
    _commentsByFile=new HashMap<File,String>();
    _commentsByFileName=new HashMap<String,String>();
  }

  /**
   * Set the comments for a given image file.
   * @param file Image file.
   * @param text Comment to set.
   */
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

  /**
   * Get the comment for a given image file.
   * @param file Image name.
   * @return A comment.
   */
  public String getComment(String file)
  {
    String ret=_commentsByFileName.get(file);
    return ret;
  }
}
