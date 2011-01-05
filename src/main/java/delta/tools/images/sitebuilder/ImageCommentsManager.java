package delta.tools.images.sitebuilder;

import java.io.File;

import delta.common.utils.text.EncodingNames;
import delta.tools.images.sitebuilder.comments.Comments;
import delta.tools.images.sitebuilder.comments.CommentsLoader;

/**
 * Manages comments for a set of images.
 * @author DAM
 */
public class ImageCommentsManager
{
  private Comments _comments;
  private File _commentsFile;

  /**
   * Constructor.
   * @param sourceDir Source directory.
   * @param commentsFile File where comments are read.
   */
  public ImageCommentsManager(File sourceDir, File commentsFile)
  {
    _commentsFile=commentsFile;
    if ((_commentsFile!=null) && (_commentsFile.exists()))
    {
      CommentsLoader loader=new CommentsLoader(EncodingNames.ISO8859_1);
      _comments=loader.load(sourceDir,_commentsFile);
    }
  }

  /**
   * Get the comment for an image.
   * @param imageName Name of image.
   * @return A comment or <code>null</code> if none.
   */
  public String getComment(String imageName)
  {
    String ret=null;
    if (_comments!=null)
    {
      ret=_comments.getComment(imageName);
    }
    return ret;
  }
}
