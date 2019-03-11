import java.io.File;
import java.io.FileFilter;
import java.util.List;

import delta.common.utils.files.FilesDeleter;
import delta.common.utils.files.FilesFinder;
import delta.common.utils.files.filter.ExtensionPredicate;


/**
 * Tool.
 * @author DAM
 */
public class Main
{
  /**
   * Main method for this tool.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    FileFilter copiesFiles=new ExtensionPredicate("db",false);
    File root=new File("/mnt/docs/nous/photos/2010/10-04 Turquie");
    FilesDeleter deleter=new FilesDeleter(root,copiesFiles,true);
    deleter.doIt();
  }

  /**
   * Another main method for this tool.
   * @param args Not used.
   */
  public static void main2(String[] args)
  {
    FileFilter copiesFiles=new FileFilter()
    {
      public boolean accept(File pathname)
      {
        String name=pathname.getName();
        return name.contains("(copie)");
      }
      
    };
    FilesFinder finder=new FilesFinder();
    File root=new File("/mnt/docs/nous/photos/2010/10-04 Turquie");
    List<File> copies=finder.find(FilesFinder.ABSOLUTE_MODE,root,copiesFiles,true);
    for(File copie : copies)
    {
      String newFileName=copie.getAbsolutePath();
      newFileName=newFileName.replace(" (copie)","");
      File normalFile=new File(newFileName);
      long oldSize=normalFile.length();
      long newSize=copie.length();
      if (oldSize==newSize)
      {
        System.out.println(copie);
        System.out.println(newFileName);
      }
      else
      {
        //copie.renameTo(normalFile);
      }
    }
  }
}
