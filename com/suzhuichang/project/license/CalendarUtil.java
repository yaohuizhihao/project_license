package com.suzhuichang.project.license;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarUtil {
  public static String getNowStr() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:SS");
    return sdf.format(Calendar.getInstance().getTime());
  }

  public static Long getNowLong() {
    return Long.valueOf(Calendar.getInstance().getTimeInMillis());
  }
}
