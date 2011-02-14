package delta.tools.images.sitebuilder.selection;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import delta.common.utils.files.FilesFinder;
import delta.common.utils.files.filter.CompoundPredicate;
import delta.common.utils.files.filter.ExtensionPredicate;
import delta.common.utils.files.filter.FileTypePredicate;

/**
 * Selects image files using the file system.
 * @author DAM
 */
public class AutomaticFilesSelection implements FilesSelection
{
  private File _rootDir;
  private FileFilter _imgFilter;
  private FileFilter _dirFilter;

  /**
   * Constructor.
   * @param sourceImagesDir Source images directory.
   */
  public AutomaticFilesSelection(File sourceImagesDir)
  {
    _rootDir=sourceImagesDir;
    FileFilter jpgFilter=new ExtensionPredicate("JPG",false);
    FileFilter jpegFilter=new ExtensionPredicate("JPEG",false);
    _imgFilter=new CompoundPredicate(jpgFilter,jpegFilter,CompoundPredicate.MODE_OR);
    _dirFilter=new FileTypePredicate(FileTypePredicate.DIRECTORY);
  }

  public List<String> getDirectories(String dir)
  {
    FilesFinder finder=new FilesFinder();
    File root=new File(_rootDir,dir);
    List<File> dirs=finder.find(FilesFinder.RELATIVE_MODE,root,_dirFilter,false);
    List<String> ret=new ArrayList<String>();
    for(File file : dirs)
    {
      ret.add(file.getName());
    }
    Collections.sort(ret);
    return ret;
  }

  public List<String> getImages(String dir)
  {
    FilesFinder finder=new FilesFinder();
    File root=new File(_rootDir,dir);
    List<File> imgs=finder.find(FilesFinder.RELATIVE_MODE,root,_imgFilter,false);
    List<String> ret=new ArrayList<String>();
    for(File file : imgs)
    {
      ret.add(file.getName());
    }
    Collections.sort(ret);
    return ret;
  }
}
