package io.liang.jsidekiq.client.common.utils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
    private static Logger log = LoggerFactory.getLogger(DateUtils.class);

    /**
     * milliseconds in a second.
     */
    public static final long   SECOND                = 1000;

    /**
     * milliseconds in a minute.
     */
    public static final long   MINUTE                = SECOND * 60;

    /**
     * milliseconds in a hour.
     */
    public static final long   HOUR                  = MINUTE * 60;

    /**
     * milliseconds in a day.
     */
    public static final long   DAY                   = 24 * HOUR;

    /**
     * yyyy-MM
     */
    public static final String MONTH_PATTERN         = "yyyy-MM";

    /**
     * yyyyMMdd
     */
    public static final String DEFAULT_PATTERN       = "yyyyMMdd";

    /**
     * yyyyMMddHHmmss
     */
    public static final String FULL_PATTERN          = "yyyyMMddHHmmss";

    /**
     * yyyyMMdd HH:mm:ss
     */
    public static final String FULL_STANDARD_PATTERN = "yyyyMMdd HH:mm:ss";

    /**
     * MM.dd HH:mm
     */
    public static final String FULL_MATCH_PATTERN    = "MM.dd HH:mm";

    /**
     * HH:mm
     */
    public static final String SHORT_MATCH_PATTERN   = "HH:mm";

    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String DATE_TIME_MINUTE      = "yyyy-MM-dd HH:mm";

    /**
     * <pre>
     * yyyy-MM-dd HH:mm:ss
     * </pre>
     */
    public static final String DATE_TIME_SHORT       = "yyyy-MM-dd HH:mm:ss";

    /**
     * <pre>
     * yyyy-MM-dd HH:mm:ss.SSS
     * </pre>
     */
    public static final String DATE_TIME_FULL        = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * <pre>
     * yyyy-MM-dd HH:mm:ss
     * </pre>
     */
    public static final String FILE_INDEX  = "yyyyMM/dd";

    public static final String DATE_PATTERN          = "yyyy-MM-dd";

    public static final String YEAR_PATTERN          = "yyyy";


    /**
     * Add specified number of days to the given date.
     * 
     * @param date date
     * @param days Int number of days to add
     * @return revised date
     */
    public static Date addDays(final Date date, int days) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);

        return new Date(cal.getTime().getTime());
    }

    public static Date addMins(final Date date, int mins) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, mins);

        return new Date(cal.getTime().getTime());
    }

    /**
     * Add specified number of months to the date given.
     * 
     * @param date Date
     * @param months Int number of months to add
     * @return Date
     */
    public static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * Get date one day after specified one.
     * 
     * @param date1 Date 1
     * @param date2 Date 2
     * @return true if after day
     */
    public static boolean afterDay(final Date date1, final Date date2) {
        return getStartOfDate(date1).after(getStartOfDate(date2));
    }

    /**
     * Get date one day before specified one.
     * 
     * @param date1 test date
     * @param date2 date when
     * @return true if date2 is before date1
     */
    public static boolean beforeDay(final Date date1, final Date date2) {
        return getStartOfDate(date1).before(getStartOfDate(date2));
    }

    /**
     * 转换long类型到时,分,秒,毫秒的格式.
     * 
     * @param time long type
     * @return
     */
    public static String convert(long time) {
        long ms = time % 1000;
        time /= 1000;

        int h = Integer.valueOf("" + (time / 3600));
        int m = Integer.valueOf("" + ((time - h * 3600) / 60));
        int s = Integer.valueOf("" + (time - h * 3600 - m * 60));

        return h + "小时(H)" + m + "分(M)" + s + "秒(S)" + ms + "毫秒(MS)";
    }

    /**
     * 转换long类型到时,分,秒,毫秒的格式.
     * 
     * @param time long type
     * @return
     */
    public static String convertEn(long time) {
        long ms = time % 1000;
        time /= 1000;

        int h = Integer.valueOf("" + (time / 3600));
        int m = Integer.valueOf("" + ((time - h * 3600) / 60));
        int s = Integer.valueOf("" + (time - h * 3600 - m * 60));

        return h + "H" + m + "M" + s + "S" + ms + "MS";
    }

    /**
     * @param aDate
     * @return
     */
    public static String convertDateToString(String pattern, Date aDate) {
        return getDateTime(pattern, aDate);
    }

    /**
     * This method generates a string representation of a date/time in the format you specify on input
     * 
     * @param aMask the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @see SimpleDateFormat
     * @throws ParseException when String doesn't match the expected format
     */
    public static Date convertStringToDate(String aMask, String strDate) {
        SimpleDateFormat df;
        Date date = null;
        df = new SimpleDateFormat(aMask);



        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            log.error("ParseException: " + pe);
        }

        return date;
    }

    /**
     * @return the current date without time component
     */
    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * Format date as "yyyy-MM-dd".
     * 
     * @param date 日期
     * @return 格式化后的日期字符串
     */
    public static String formatDates(final Date date) {
        return formatDate(date, DATE_PATTERN);
    }

    /**
     * Format date as "yyyyMMdd".
     *
     * @param date 日期
     * @return 格式化后的日期字符串
     */
    public static String formatFileIndex(final Date date) {
        return formatDate(date, FILE_INDEX);
    }

    /**
     * Format date as given date format.
     * 
     * @param date 日期
     * @return 格式化后的日期字符串
     */
    public static String formatDate(final Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String formatDateTime(Date date) {
        return formatDate(date, DATE_TIME_SHORT);
    }

    /**
     * Format date as "MM月dd日 HH:mm".
     * 
     * @param date 日期
     * @return 格式化后的日期字符串
     */
    public static String formatFullMatchDate(final Date date) {
        return formatDate(date, FULL_MATCH_PATTERN);
    }

    /**
     * 返回MM月dd日
     * 
     * @param srcDate
     * @return
     */
    public static String formatMonthAndDay(Date srcDate) {
        Calendar cal1 = GregorianCalendar.getInstance();
        cal1.setTime(srcDate);
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");

        return formatter.format(srcDate);
    }

    /**
     * 返回短日期格式
     * 
     * @return [yyyy-mm-dd]
     */
    public static String formatShort(String strDate) {
        String ret = "";
        if (strDate != null && !"1900-01-01 00:00:00.0".equals(strDate) && strDate.indexOf("-") > 0) {
            ret = strDate;
            if (ret.indexOf(" ") > -1) ret = ret.substring(0, ret.indexOf(" "));
        }
        return ret;
    }

    /**
     * 格式化中文日期短日期格式
     * 
     * @param gstrDate 输入欲格式化的日期
     * @return [yyyy年MM月dd日]
     */

    public static String formatShortDateC(Date gstrDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        String pid = formatter.format(gstrDate);
        return pid;
    }

    /**
     * Format date as "HH:mm".
     * 
     * @param date 日期
     * @return 格式化后的日期字符串
     */
    public static String formatShortMatchDate(final Date date) {
        return formatDate(date, SHORT_MATCH_PATTERN);
    }

    public static Date getCurrentMonday() {
        Calendar cd = Calendar.getInstance();

        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        Date date;
        if (dayOfWeek == 1) {
            date = cd.getTime();
        } else {
            date = addDays(cd.getTime(), 1 - dayOfWeek);
        }

        return getStartOfDate(date);
    }

    /**
     * Return default datePattern (yyyy-MM-dd)
     * 
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        return "yyyy-MM-dd";
    }

    public static String getDateTime(Date date) {
        return formatDate(date, DATE_TIME_SHORT);
    }

    /**
     * This method generates a string representation of a date's date/time in the format you specify on input
     * 
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * @see SimpleDateFormat
     */
    public static String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            log.error("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    public static String getDateTimeFull(Date date) {
        return formatDate(date, DATE_TIME_FULL);
    }

    public static String getDateTimePattern() {
        return DateUtils.getDatePattern() + " HH:mm:ss.S";
    }

    /**
     * 返回当前日
     * 
     * @return [dd]
     */

    public static String getDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        Date nowc = new Date();
        String pid = formatter.format(nowc);
        return pid;
    }

    /**
     * 一天的结束时间，【注：只精确到毫秒】
     * 
     * @param date
     * @return
     */
    public static Date getEndOfDate(final Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        return new Date(cal.getTime().getTime());
    }

    /**
     * Return the end of the month based on the date passed as input parameter.
     * 
     * @param date Date
     * @return Date endOfMonth
     */
    public static Date getEndOfMonth(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        calendar.set(Calendar.DATE, 0);

        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * Get first day of month.
     * 
     * @param date Date
     * @return Date
     */
    public static Date getFirstOfMonth(final Date date) {
        Date lastMonth = addMonths(date, -1);
        lastMonth = getEndOfMonth(lastMonth);
        return addDays(lastMonth, 1);
    }

    public static Date getMondayBefore4Week() {
        Calendar cd = Calendar.getInstance();

        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        Date date;
        if (dayOfWeek == 1) {
            date = addDays(cd.getTime(), -28);
        } else {
            date = addDays(cd.getTime(), -27 - dayOfWeek);
        }

        return getStartOfDate(date);
    }

    /**
     * 返回当前月份
     * 
     * @return [MM]
     */

    public static String getMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        Date nowc = new Date();
        String pid = formatter.format(nowc);
        return pid;
    }

    /**
     * 返回标准格式的当前时间
     * 
     * @return [yyyy-MM-dd k:mm:ss]
     */

    public static String getNow() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        Date nowc = new Date();
        String pid = formatter.format(nowc);
        return pid;
    }

    /**
     * 计算2个日前直接相差的天数
     * 
     * @param cal1
     * @param cal2
     * @return
     */
    public static long getNumberOfDaysBetween(Calendar cal1, Calendar cal2) {
        cal1.clear(Calendar.MILLISECOND);
        cal1.clear(Calendar.SECOND);
        cal1.clear(Calendar.MINUTE);
        cal1.clear(Calendar.HOUR_OF_DAY);

        cal2.clear(Calendar.MILLISECOND);
        cal2.clear(Calendar.SECOND);
        cal2.clear(Calendar.MINUTE);
        cal2.clear(Calendar.HOUR_OF_DAY);

        long elapsed = cal2.getTime().getTime() - cal1.getTime().getTime();
        return elapsed / DAY;
    }

    /**
     * 返回两个时间间隔的小时数
     * 
     * @param before 起始时间
     * @param end 终止时间
     * @return 小时数
     */
    public static long getNumberOfHoursBetween(final Date before, final Date end) {
        long millisec = end.getTime() - before.getTime() + 1;
        return millisec / (60 * 60 * 1000);
    }

    /**
     * 返回两个时间间隔的分钟数
     * 
     * @param before 起始时间
     * @param end 终止时间
     * @return 分钟数
     */
    public static long getNumberOfMinuteBetween(final Date before, final Date end) {
        long millisec = end.getTime() - before.getTime();
        return millisec / (60 * 1000);
    }

    public static int getNumberOfMonthsBetween(final Date before, final Date end) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(before);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(end);
        return (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12
               + (cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH));
    }

    public static int getNumberOfSecondsBetween(final double end, final double start) {
        if ((end == 0) || (start == 0)) {
            return -1;
        }

        return (int) (Math.abs(end - start) / SECOND);
    }

    public static Date getPreviousMonday() {
        Calendar cd = Calendar.getInstance();

        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        Date date;
        if (dayOfWeek == 1) {
            date = addDays(cd.getTime(), -7);
        } else {
            date = addDays(cd.getTime(), -6 - dayOfWeek);
        }

        return getStartOfDate(date);
    }

    /**
     * 返回中文格式的当前日期
     * 
     * @return [yyyy-mm-dd]
     */
    public static String getShortNow() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date nowc = new Date();
        String pid = formatter.format(nowc);
        return pid;
    }

    /**
     * Get start of date.
     * 
     * @param date Date
     * @return Date Date
     */
    public static Date getStartOfDate(final Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return new Date(cal.getTime().getTime());
    }

    /**
     * 返回当前时间24小时制式
     * 
     * @return [H:mm]
     */

    public static String getTimeBykm() {
        SimpleDateFormat formatter = new SimpleDateFormat("H:mm");
        Date nowc = new Date();
        String pid = formatter.format(nowc);
        return pid;
    }

    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 检查日期的合法性
     * 
     * @param sourceDate
     * @return
     */
    public static boolean inFormat(String sourceDate, String format) {
        if (sourceDate == null) {
            return false;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setLenient(false);
            dateFormat.parse(sourceDate);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = GregorianCalendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = GregorianCalendar.getInstance();
        cal2.setTime(date2);

        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
               && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && (cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE)));
    }

    /**
     * Compare the two calendars whether they are in the same month.
     * 
     * @param cal1 the first calendar
     * @param cal2 the second calendar
     * @return whether are in the same month
     */
    public static boolean isSameMonth(Calendar cal1, Calendar cal2) {
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
               && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH));
    }

    /**
     * Compare the two dates whether are in the same month.
     * 
     * @param date1 the first date
     * @param date2 the second date
     * @return whether are in the same month
     */
    public static boolean isSameMonth(Date date1, Date date2) {
        Calendar cal1 = GregorianCalendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = GregorianCalendar.getInstance();
        cal2.setTime(date2);
        return isSameMonth(cal1, cal2);
    }

//    public static void main(String[] args) {
//        /*
//         * System.out.println(DateUtils.getFirstOfMonth(DateUtils.addMonths( DateUtils.currentDate(), -3)));
//         * System.out.println(DateUtils.getEndOfMonth(DateUtils.currentDate())); System.out.println("now:" +
//         * DateUtils.now());
//         */
//        /*
//         * Calendar cal1 = GregorianCalendar.getInstance(); cal1.add(Calendar.MINUTE, 2);
//         * System.out.println(getNumberOfMinuteBetween(new Date(), cal1.getTime()));
//         */
//        // System.out.println("previous monday:" +
//        // DateUtils.getPreviousMonday());
//        // System.out.println("current monday:" + DateUtils.getCurrentMonday());
//        // System.out.println("monday before 4 weeks:"
//        // + DateUtils.getMondayBefore4Week());
////        System.out.println(formatDate(addDays(new Date(), 1), "yyyyMMdd"));
////        System.out.println("formatDate(new Date(), \"yyyyMMdd\"):" + DateUtils.formatDate(new Date(), "yyyyMMdd"));
//
//        System.out.println(DateUtils.formatDateTime(DateUtils.getEndOfDate(new Date())));
//
//        System.out.println(DateUtils.formatDateTime(DateUtils.getToday()));
//
//    }

    /**
     * get date time as "yyyyMMddhhmmss"
     * 
     * @return the current date with time component
     */
    public static String now() {
        return formatDate(new Date(), FULL_PATTERN);
    }

    /**
     * change the string to date
     * 
     * @param date
     * @return Date if failed return <code>null</code>
     */
    public static Date parseDate(String date) {
        return parseDate(date, DEFAULT_PATTERN, null);
    }

    /**
     * change the string to date
     * 
     * @param date
     * @param df
     * @return Date
     */
    public static Date parseDate(String date, String df) {
        return parseDate(date, df, null);
    }

    /**
     * change the string to date
     * 
     * @param date
     * @param df df
     * @param defaultValue if parse failed return the default value
     * @return Date
     */
    public static Date parseDate(String date, String df, Date defaultValue) {
        if (date == null) {
            return defaultValue;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(df);

        try {
            return formatter.parse(date);
        } catch (ParseException e) {
        }

        return defaultValue;
    }

    private DateUtils(){

    }

    public static int equalDate(Date d1,Date d2){
        int flag = -1;
        if(d1 == null){
            if(d2 == null){
                flag = 0;
            }else{
                flag = -1;
            }
        }else{
            if(d2 == null){
                flag = 1;
            }else{
                flag = d1.compareTo(d2);
            }
        }
        return flag;
    }

    public static String getCurrentYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(YEAR_PATTERN);
        return sdf.format(date);
    }

    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        return sdf.format(date);
    }

}
