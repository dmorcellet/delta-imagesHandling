package delta.tools.images.sitebuilder.comments;

import java.io.File;

import delta.common.utils.files.TextFileReader;
import delta.common.utils.text.EncodingNames;
import delta.common.utils.text.EndOfLine;

/**
 * @author DAM
 */
public class CommentsLoader
{
  private String _encoding;
  private File _sourceDir;
  private Comments _comments;
  private StringBuilder _comment;
  private String _file;
  private boolean _doCheckSourceFile;

  public CommentsLoader(String encodingName)
  {
    _encoding=encodingName;
    _doCheckSourceFile=true;
    reset();
  }

  private void reset()
  {
    _file=null;
    _comment=new StringBuilder();
  }

  public Comments load(File sourceDir, File commentsFile)
  {
    Comments ret=null;
    _sourceDir=sourceDir;
    TextFileReader reader=new TextFileReader(commentsFile,_encoding);
    if (reader.start())
    {
      _comments=new Comments();
      reset();
      String line;
      while (true)
      {
        line=reader.getNextLine();
        if (line==null) break;
        if (line.startsWith(".\\"))
        {
          if (_file!=null)
          {
            flush();
          }
          _file=line;
        }
        else
        {
          if (_comment.length()>0) {
            _comment.append(EndOfLine.UNIX);
          }
          _comment.append(line);
        }
      }
      flush();
      reader.terminate();
      ret=_comments;
      _comments=null;
    }
    return ret;
  }

  private void flush()
  {
    if (_comments!=null)
    {
      if (_file!=null)
      {
        if ((_comment!=null) && (_comment.length()>0))
        {
          String comment=_comment.toString();
          File sourceFile=new File(_sourceDir,normalizeFileName(_file));
          if (_doCheckSourceFile)
          {
            if (!sourceFile.canRead())
            {
              System.err.println("Cannot read file ["+sourceFile+"]");
            }
          }
          _comments.setComment(sourceFile,comment);
          _comment.setLength(0);
        }
      }
    }
    _file=null;
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

  public static void main(String[] args)
  {
    File f=new File("/home/dm/tmp/usa/album photos USA/texte.txt");
    File sourceDir=new File("/home/dm/tmp/usa/album photos USA");
    CommentsLoader l=new CommentsLoader(EncodingNames.ISO8859_1);
    Comments c=l.load(sourceDir,f);
  }
}
