package delta.utils.images;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import delta.common.utils.files.TextFileReader;

/**
 * EXIF data reader.
 * @author DAM
 */
public class ExifReader
{
  private static final Logger LOGGER=Logger.getLogger(ExifReader.class);

  static class DateFile
  {
    private Date _date;
    private String _file;

    public DateFile(Date date, String file)
    {
      _date=date;
      _file=file;
    }
  }

  static class DateFileComparator implements Comparator<DateFile>,Serializable
  {
    /**
     * Compares date/file objects by their date.
     * @param o1 a date/file.
     * @param o2 another date/file.
     */
    public int compare(DateFile o1, DateFile o2)
    {
      return o1._date.compareTo(o2._date);
    }
  }

  private List<DateFile> _list;

  /**
   * Constructor.
   */
  public ExifReader()
  {
    _list=new ArrayList<DateFile>();
  }

  private void go()
  {
    File exif=new File("c:\\dada\\normandie\\exif.txt");
    TextFileReader fp=new TextFileReader(exif);
    fp.start();
    String line=null;
    String hour;
    String completePath;
    Date realDate;
    while (true)
    {
      line=fp.getNextLine();
      if (line==null) break;

      StringTokenizer st=new StringTokenizer(line,"\t");
      st.nextToken(); // filename
      hour=st.nextToken();
      for(int i=0;i<12;i++) st.nextToken();
      completePath=st.nextToken();
      realDate=parseDate(hour);
      //handleOffsets(realDate,fileName);
      DateFile df=new DateFile(realDate,completePath);
      _list.add(df);
      Collections.sort(_list,new DateFileComparator());
    }
    int nb=_list.size();
    StringBuilder sb=new StringBuilder();
    File f;
    File to;
    for(int i=1;i<=nb;i++)
    {
      sb.setLength(0);
      if (i<10) sb.append('0');
      if (i<100) sb.append('0');
      if (i<1000) sb.append('0');
      if (i<10000) sb.append('0');
      sb.append(i);
      sb.append('_');
      f=new File(_list.get(i-1)._file);
      to=new File(f.getParentFile(),sb.toString()+f.getName());
      f.renameTo(to);
    }
    fp.terminate();
  }

  private Date parseDate(String hourStr)
  {
    Calendar c=Calendar.getInstance();
    StringTokenizer st=new StringTokenizer(hourStr," :");
    Date ret=null;
    try
    {
      c.set(Calendar.YEAR,Integer.parseInt(st.nextToken()));
      c.set(Calendar.MONTH,Integer.parseInt(st.nextToken())-1);
      c.set(Calendar.DAY_OF_MONTH,Integer.parseInt(st.nextToken()));
      c.set(Calendar.HOUR_OF_DAY,Integer.parseInt(st.nextToken()));
      c.set(Calendar.MINUTE,Integer.parseInt(st.nextToken()));
      c.set(Calendar.SECOND,Integer.parseInt(st.nextToken()));
      ret=new Date(c.getTimeInMillis());
    }
    catch(NumberFormatException nfe)
    {
      LOGGER.error("",nfe);
    }
    return ret;
  }

  private static final long OFFSET_JACQUES=-1000L*(6*60+47);
  private static final long OFFSET_MICHEL=-1000L*(12*60+36);

  /**
   * Apply offsets to the given date (function of image name).
   * @param date Input date.
   * @param name Image name.
   */
  public void handleOffsets(Date date, String name)
  {
    if (name.startsWith("IMG_"))
    {
      // Jacques
      date.setTime(date.getTime()+OFFSET_JACQUES);
    }
    else if (name.startsWith("ST_"))
    {
      // Jacques
      date.setTime(date.getTime()+OFFSET_JACQUES);
    }
    else if (name.startsWith("corse"))
    {
      // Jacques
      date.setTime(date.getTime()+OFFSET_JACQUES);
    }
    else if (name.startsWith("DSCN"))
    {
      // Michel
      date.setTime(date.getTime()+OFFSET_MICHEL);
    }
  }

  /**
   * Main method for this tool.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new ExifReader().go();
  }
}
