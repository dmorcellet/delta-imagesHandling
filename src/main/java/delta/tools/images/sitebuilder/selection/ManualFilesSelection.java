package delta.tools.images.sitebuilder.selection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Selects image files using a list of selected files.
 * @author DAM
 */
public class ManualFilesSelection implements FilesSelection
{
  private HashMap<String,List<String>> _files;

  /**
   * Constructor.
   * @param selectedFiles List of selected files (relative paths).
   */
  public ManualFilesSelection(List<String> selectedFiles)
  {
    _files=new HashMap<String,List<String>>();
    for(String file : selectedFiles)
    {
      addItem(file);
    }
  }

  private void addItem(String file)
  {
    if (file==null) return;
    file=file.trim();
    if (file.length()==0) return;
    String currentDir="";
    String item;
    while(true)
    {
      List<String> files=_files.get(currentDir);
      if (files==null)
      {
        files=new ArrayList<String>();
        _files.put(currentDir,files);
      }
      int index=file.indexOf('/');
      if (index!=-1)
      {
        item=file.substring(0,index);
        file=file.substring(index+1);
        if (!files.contains(item)) files.add(item);
        if (currentDir.length()>0)
        {
          currentDir=currentDir+'/'+item;
        }
        else
        {
          currentDir=item;
        }
      }
      else
      {
        if (!files.contains(file)) files.add(file);
        break;
      }
    }
  }

  public List<String> getDirectories(String dir)
  {
    List<String> ret=new ArrayList<String>();
    List<String> files=_files.get(dir);
    if (files!=null)
    {
      for(String file : files)
      {
        String subDir=(dir.length()>0)?dir+'/'+file:file;
        if (_files.get(subDir)!=null)
        {
          ret.add(file);
        }
      }
    }
    return ret;
  }

  public List<String> getImages(String dir)
  {
    List<String> ret=new ArrayList<String>();
    List<String> files=_files.get(dir);
    if (files!=null)
    {
      for(String file : files)
      {
        String subDir=(dir.length()>0)?dir+'/'+file:file;
        if (_files.get(subDir)==null)
        {
          ret.add(file);
        }
      }
    }
    return ret;
  }
}
