package delta.tools.images.sitebuilder.comments;

import java.io.File;

import delta.common.utils.files.TextFileReader;
import delta.common.utils.text.EndOfLine;

/**
 * Loader for a comments file.
 * @author DAM
 */
public class CommentsLoader
{
  private String _encoding;
  private boolean _doCheckSourceFile;

  /**
   * Constructor.
   * @param encodingName Encoding to use.
   */
  public CommentsLoader(String encodingName)
  {
    _encoding=encodingName;
    _doCheckSourceFile=true;
  }

  /**
   * Load a set of comments.
   * @param sourceDir Directory to use as a base path for files. 
   * @param commentsFile File to read.
   * @return A comments manager.
   */
  public Comments load(File sourceDir, File commentsFile)
  {
    Comments ret=null;
    TextFileReader reader=new TextFileReader(commentsFile,_encoding);
    if (reader.start())
    {
      Comments comments=new Comments();
      StringBuilder comment=new StringBuilder();
      String line;
      String file=null;
      while (true)
      {
        line=reader.getNextLine();
        if (line==null) break;
        if (line.startsWith(".\\"))
        {
          if (file!=null)
          {
            flush(sourceDir,file,comments,comment.toString());
            comment.setLength(0);
          }
          file=line;
        }
        else
        {
          if (comment.length()>0) {
            comment.append(EndOfLine.UNIX);
          }
          comment.append(line);
        }
      }
      flush(sourceDir,file,comments,comment.toString());
      comment.setLength(0);
      reader.terminate();
      ret=comments;
    }
    return ret;
  }

  private void flush(File sourceDir, String file, Comments comments, String comment)
  {
    if (file!=null)
    {
      if ((comment!=null) && (comment.length()>0))
      {
        File sourceFile=new File(sourceDir,normalizeFileName(file));
        if (_doCheckSourceFile)
        {
          if (!sourceFile.canRead())
          {
            System.err.println("Cannot read file ["+sourceFile+"]");
          }
        }
        comments.setComment(sourceFile,comment);
      }
    }
  }

  private String normalizeFileName(String fileName)
  {
    fileName=fileName.replace('\\','/');
    if (fileName.startsWith("./"))
    {
      fileName=fileName.substring(2);
    }
    return fileName;
  }

  /*
  public static void main(String[] args)
  {
    File f=new File("/home/dm/tmp/usa/album photos USA/texte.txt");
    File sourceDir=new File("/home/dm/tmp/usa/album photos USA");
    CommentsLoader l=new CommentsLoader(EncodingNames.ISO8859_1);
    Comments c=l.load(sourceDir,f);
  }
  */
}
