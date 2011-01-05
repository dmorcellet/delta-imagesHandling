package delta.tools.images.sitebuilder;

import java.awt.Dimension;

/**
 * @author DAM
 */
public class SitePreferences
{
  private static final int MAX_WIDTH_IMAGE=900;
  private static final int MAX_HEIGHT_IMAGE=600;
  private static final int MAX_WIDTH_THUMB=150;
  private static final int MAX_HEIGHT_THUMB=150;

  public Dimension computeSmallImageDimension(Dimension d)
  {
    int maxWidth=MAX_WIDTH_IMAGE;
    int maxHeight=MAX_HEIGHT_IMAGE;
    int width=d.width;
    int height=d.height;

    float factor1=(float)width/maxWidth;
    float factor2=(float)height/maxHeight;
    if (factor1>factor2)
    {
      height=(int)(height/factor1);
      width=maxWidth;
    }
    else
    {
      float factor=(float)height/maxHeight;
      width=(int)(width/factor);
      height=maxHeight;
    }
    return new Dimension(width,height);
  }

  public Dimension computeThumbnailDimension(Dimension d)
  {
    int maxWidth=MAX_WIDTH_THUMB;
    int maxHeight=MAX_HEIGHT_THUMB;
    int width=d.width;
    int height=d.height;
    if (width>height)
    {
      float factor=(float)width/maxWidth;
      height=(int)(height/factor);
      width=maxWidth;
    }
    else
    {
      float factor=(float)height/maxHeight;
      width=(int)(width/factor);
      height=maxHeight;
    }
    return new Dimension(width,height);
  }
}
