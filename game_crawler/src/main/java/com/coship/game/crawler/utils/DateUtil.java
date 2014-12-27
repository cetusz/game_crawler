package com.coship.game.crawler.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 日期工具类
 * 
 * @author habibliu@gmail.com
 * 
 */
public class DateUtil {
	
	    public final static String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	    public final static String YYYYMMDDHHMMSS_SHORT = "yyyyMMddHHmmss";
	    public final static String YYYYMMDDHHMMSS_MILLIS = "yyyyMMddHHmmssSSS";
	    public final static String HHMMSS = "HH:mm:ss";
	    public final static String YYYYMMDDHH = "yyyy-MM-dd HH";
	    public final static String YYYYMMDDHH_SHORT = "yyyyMMddHH";
	    public final static String YYYYMM = "yyyyMM";
	    public final static String YYYYMMDD_SHORT = "yyyyMMdd";
	    public final static String YYYYMMDD = "yyyy-MM-dd"; 
	    
	    public final static String YYYYMMDDHHMMSS_UTC =  "yyyy-MM-dd'T'HH:mm:ss'Z'";
	   
	    private static SimpleDateFormat mongoDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		static {
			mongoDf.setTimeZone(TimeZone.getTimeZone("GMT+8")); 
		}
		
		/**
		 * 自定义年月日
		 * @param year
		 * @param month
		 * @param day
		 */
		public static Date getDefinedTime(int year,int month,int day){
			Calendar cal = Calendar.getInstance();
			if(year>0){
				cal.set(Calendar.YEAR, year);
			}
			if(month>0){
				cal.set(Calendar.MONTH, month-1);
			}
			if(day>0){
				cal.set(Calendar.DAY_OF_MONTH, day);
			}
			return cal.getTime();
		}
	
		
		/**
		 * 格式化当前时间
		 * @param date
		 * @param format
		 */
		public static Date getDateFormat(Date date,String format){
			Date currentFormat=null;
			SimpleDateFormat sdf=null;
			
			try {
				sdf=new SimpleDateFormat(format);
				String time = sdf.format(date);
				currentFormat=sdf.parse(time);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return currentFormat;
		}
		
		
		/**
		 * mongoDb中存储的时间是GMT（格林尼治标准时）时间
		 * 需要转成北京时间才能正确显示
		 * @param date
		 * @return
		 */
		public static String mongoDateFormate(Date date){
			
			if(date == null) {
				return null;
			}	
			return mongoDf.format(date);
				
		}
		
	    /**
	     * 计算当前日期 间隔num天的日期
	     * @param date
	     * @param num
	     * @return
	     */ 
	    public static Date raiseDay(Date date,int num){
	        if(date==null){return null;}
	        
	        Calendar cal=Calendar.getInstance();
	        cal.setTime(date);
	        cal.add(Calendar.DAY_OF_MONTH, num);

	        return cal.getTime();
	    }
	    
	    /**
	     * 计算当前日期 间隔num天的日期
	     * @param date
	     * @param num
	     * @return
	     */ 
	    public static Date raiseHour(Date date,int num){
	        if(date==null){return null;}
	        
	        return raiseMinutes(date,num*60);
	    }
	    
	    /**
	     * 给指定时间增加固定分钟数
	     * @param date
	     * @param min
	     * @return
	     */
	    public static Date raiseMinutes(Date date,Integer min){
	        if(date==null){return null;}
	        Calendar cal = Calendar.getInstance();
	        cal.setTimeInMillis(date.getTime()+(long)min*60000);
	        return  cal.getTime();
	    }
	    
	    /**
	     * 给指定时间增加固定秒数
	     * @param date
	     * @param second
	     * @return
	     */
	    public static Date raiseSeconds(Date date,Integer second){
	        if(date==null){return null;}
	        Calendar cal = Calendar.getInstance();
	        cal.setTimeInMillis(date.getTime()+(long)(second*1000));
	        return  cal.getTime();
	    }


	    /**
	     * @param dateStr
	     *            日期字符串 例如：0000-00-00 00:00:00
	     * @param formatStr
	     *            格式化字符串，例如：yyyy-MM-dd HH:mm:ss
	     * @return Date 日期类型
	     */
	    public static Date string2Date(String date, String format) {
	        Date d = null;
	        SimpleDateFormat sf = null;
	        try {
	            sf = new SimpleDateFormat(format);
	            d = sf.parse(date);
	        } catch (Exception e) {
	            e.printStackTrace();
	            d = null;
	        }
	        return d;
	    }
	    
	    /**
	     * @param dateStr
	     *            日期字符串 例如：0000-00-00 00:00:00
	     * @param formatStr
	     *            格式化字符串，例如：yyyy-MM-dd HH:mm:ss
	     * @return Date 日期类型
	     */
	    public static Date string2DateByYMDHMS(String date) {
	        // 如果参数为null、格式化字符串为null,返回null
	        if (null == date)
	        {
	            return null;
	        }
	        // 如果时间参数不是有效的格式，返回null
	        if (!Pattern.matches("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{2}:\\d{2}:\\d{2}", date))
	        {
	            return null;
	        }
	        String format = YYYYMMDDHHMMSS;
	        // 上一步的正则表达式的判断，时间格式可能是 yyyy-M-d HH:mm:ss 的情况，因此，给这种情况的月、日 加上0
	        // 更新后为 yyyy-0M-0d HH:mm:ss
	        String tmpDate = date;
	        if (format.length() > date.length())
	        {
	            String yearTail = date.substring(0, 5);
	            String timeTail = date.substring(date.length() - 9);
	            String middDate = date.substring(5, date.length() - 9);
	            String[] md = middDate.split("-");
	            StringBuilder sb = new StringBuilder();
	            sb.append(yearTail);
	            if (md[0].length() == 1)
	            {
	                sb.append("0");
	            }
	            sb.append(md[0]);
	            sb.append("-");
	            if (md[1].length() == 1)
	            {
	                sb.append("0");
	            }
	            sb.append(md[1]);
	            sb.append(timeTail);
	            tmpDate = sb.toString();
	        }
	        Date returnDate = null;
	        SimpleDateFormat sf = null;
	        
	        try {
	            sf = new SimpleDateFormat(format);
	            returnDate = sf.parse(tmpDate);
	            // 将字符串格式化后的时间，再转成字符串
	            String returnDate2Str = DateUtil.date2String(returnDate, format);
	            // 比较2者是否相同，相同，说明日期格式正确，不相同，日期格式不正确
	            if (!tmpDate.equals(returnDate2Str))
	            {
	                returnDate = null;
	            }
	        } catch (ParseException e) {
	            e.printStackTrace();
	            returnDate = null;
	        }
	        return returnDate;
	    }

	    /**
	     * 将时间字符串转换成Date类型时间
	     * 
	     * @param date
	     *            yyyy-MM-dd HH:mm:ss 类型的时间字符串
	     * @return 字符串对应的Date类型时间
	     */
	    public static Date string2Date(String date) {
	        return string2Date(date, YYYYMMDDHHMMSS);
	    }

	    /**
	     * 将时间字符串转换成时间对应的毫秒值
	     * 
	     * @param date
	     *            时间字符串
	     * @param format
	     *            字符串格式
	     * @return 字符串对应的时间毫秒值
	     */
	    public static long string2TimeMillis(String date, String format) {
	        long d = 0;
	        Date dt = string2Date(date, format);
	        if (dt != null) {
	            d = dt.getTime();
	        }
	        return d;
	    }

	    /**
	     * 将时间字符串转换成时间对应的毫秒值
	     * 
	     * @param date
	     *            yyyy-MM-dd HH:mm:ss 类型的时间字符串
	     * @return 字符串对应的时间毫秒值
	     */
	    public static long string2TimeMillis(String date) {
	        return string2TimeMillis(date, YYYYMMDDHHMMSS);
	    }

	    /**
	     * 获取当前时间  
	     * 
	     * @param format
	     *            yyyy-MM-dd 转换格式
	     * @return 字符串对应的日期
	     */
	    public static String getCurrentDate(String format) {

	        SimpleDateFormat formatter = new SimpleDateFormat(format);
	        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
	        String str = formatter.format(curDate);
	        return str;

	    }

	    /**
	     * 日期比较
	     * 
	     * @param beginTime
	     *            开始时间
	     * @param endTime
	     *            结束时间
	     * @return true：开始时间大于结束时间返回true, false：返回false
	     */
	    public static boolean dateComparison(Date beginTime, Date endTime) {

	        if (beginTime == null || endTime == null) {
	            return false;
	        }
	        try {
	            if (beginTime.getTime() >= endTime.getTime()) {
	                return true;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	    }

	    /**
	     * 日期字符串 0000-01-01 00:00:00 或 0000-01-01 或 00-1-1 0:0:0 或 00-1-1 
	     * 兼容 00-1-32 [Java时间兼容]
	     * 903889 修改 2010-09-29
	     * @param dateStr
	     *            日期字符串 0000-00-00 00:00:00
	     * @return Date 日期类型
	     */
	    public static Date format(String dateStr) {
	    	if (dateStr == null ) {
	            return null;
	        }
	        dateStr = dateStr.trim();
	        String longFormat = "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}";
	        String shortFormat = "\\d{4}-\\d{1,2}-\\d{1,2}";
	        String longFormatWithShortYear = "\\d{2}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}";
	        String shortFormatWithShortYear = "\\d{2}-\\d{1,2}-\\d{1,2}";
	                
	        SimpleDateFormat sf = null;
	        if(Pattern.matches(longFormat,dateStr)){
	        	sf = new SimpleDateFormat(YYYYMMDDHHMMSS);
	        }else if(Pattern.matches(shortFormat,dateStr)){
	        	sf = new SimpleDateFormat(YYYYMMDD);
	        }else if(Pattern.matches(longFormatWithShortYear,dateStr)){
	        	dateStr = "20" + dateStr;
	        	sf = new SimpleDateFormat(YYYYMMDDHHMMSS);
	        }else if(Pattern.matches(shortFormatWithShortYear,dateStr)){
	        	dateStr = "20" + dateStr;
	        	sf = new SimpleDateFormat(YYYYMMDD);
	        }else{
	        	return null;
	        }
	        java.util.Date dd = null;
	        try {           
	            dd = sf.parse(dateStr);
	        } catch (Exception e) {
	            e.printStackTrace();
	            dd = null;
	        }
	        return dd;
	    }

	    /**
	     * 
	     * @param dateStr
	     *            日期字符串 0000-00-00
	     * @return Date 日期类型
	     */
	    public static Date formatDay(String dateStr) {
	        if (dateStr == null || dateStr.trim().length() < 1) {
	            return null;
	        }
	        SimpleDateFormat sf = new SimpleDateFormat(YYYYMMDD);
	        java.util.Date dd = null;
	        try {
	            String compare = "yy-MM-dd";
	            if (null != dateStr && dateStr.length() == compare.length()) {
	                dateStr = "20" + dateStr;
	            }
	            dd = sf.parse(dateStr);
	        } catch (Exception e) {
	            e.printStackTrace();
	            dd = null;
	        }
	        return dd;
	    }
	    
	    /**
		 * 把字符型日期转换成Timestamp型日期
		 * 
		 * @param s 字符型日期，格式：2005-01-05 hh:mm:ss
		 * @return Timestamp
		 */
	    public static Timestamp stringToTimestamp(String inTime) {

			Timestamp retValue = null;
			SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
			try {
					java.util.Date tempDate = sdf.parse(inTime);
					retValue = new Timestamp(tempDate.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return retValue;
		}
	    
	    /**
	     * 将日期转换成字符串
	     * @param date
	     * @param format
	     * @return
	     */
	    public static String date2String(Date date){
	        return date2String(date,DateUtil.YYYYMMDDHHMMSS);
	    }
	    /**
	     * 将日期转换成字符串
	     * @param date
	     * @param format
	     * @return
	     */
	    public static String date2String(Date date,String format){
	        if(date==null) return "";
	        SimpleDateFormat sdf = new SimpleDateFormat(format);
	        String result = null;
	        sdf.setLenient(false);
	        try {
	            result = sdf.format(date) ;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
	    
	      /**
	      * 计算相对于 1970-01-01 00:00:00的时间
	      * @param date 字符型日期，格式：YYYY-MM-DD hh:mm:ss
	      * @return
	      */
	     public static long calculateTimesFrom2010(Date date){
	       long time2010 = DateUtil.string2Date("2010-01-01 00:00:00").getTime();
	       
	       long time = date.getTime();
	       
	       return (time - time2010) / 1000;
	     }
	     
	      /**
	       * 获取 增量时间
	       * @param date    时间
	       * @param hour    小时增量
	       * @return
	       */
	      public static Date getAddHourOfDate(Date date, int hour){
	        Calendar cal  = Calendar.getInstance();
	        cal.setTime(date);
	        cal.add(Calendar.HOUR_OF_DAY, hour);
	        
	        return cal.getTime();
	      }
	     
	      /**
	       * 
	       * 方法getNextMondayDate的作用:  获得下周星期一的日期   
	       * 适用条件:[可选]
	       * 执行流程:[可选]
	       * 使用方法:[可选]
	       * 注意事项:[可选]
	       * 修改记录:[时间_修改人_修改描述]
	       * @return 
	       * @Exception 异常对象
	       * @version  1.0[修改时小数点后+1]
	       */
	       public static Date getNextMondayDate() 
	       {   
	          
	           String date = getNextMonday();
	          
	           return string2Date(date,DateUtil.YYYYMMDD);
	       }

	       /**
	        * 
	        * 方法getNextMondayDate的作用:  获得下周星期一的日期   
	        * 适用条件:[可选]
	        * 执行流程:[可选]
	        * 使用方法:[可选]
	        * 注意事项:[可选]
	        * 修改记录:[时间_修改人_修改描述]
	        * @return 
	        * @Exception 异常对象
	        * @version  1.0[修改时小数点后+1]
	        */ 
	      public static String getNextMonday() 
	      {   
	        int mondayPlus = getMondayPlus();  
	        
	        GregorianCalendar currentDate = new GregorianCalendar();   
	        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7); 
	        
	        Date monday = currentDate.getTime();   
	        DateFormat df = DateFormat.getDateInstance();  
	        
	        String preMonday = df.format(monday);   
	        
	        return preMonday;   
	      } 
	    
	      // 获得当前日期与本周日相差的天数   
	      private static int getMondayPlus() 
	      {   
	          Calendar cd = Calendar.getInstance();   
	          
	          // 获得今天是一周的第几天，星期日是第一天，星期二是第二天 
	          // 因为按中国礼拜一作为第一天所以这里减1  
	          int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK)-1;  
	          
	          if (dayOfWeek == 1) 
	          {   
	              return 0;   
	          } else 
	          {   
	              return 1 - dayOfWeek;   
	          }   
	      }  
	      
	  	/**
	  	 * 将字符串时间HH:mm:ss转换成持续时间（秒）
	  	 */
	  	public static Integer parsePlayTimeToDuration(String playTime ,boolean end) {
	  		Integer durationTemp = 0;
	  		if ("".equals(playTime)) {
	  			return durationTemp;
	  		}
	  		String[] array = playTime.split(":");
	  		try 
	  		{
	  			if(array.length==1)
	  			{
	  				if(end)
	  				{
	  					durationTemp = Integer.parseInt(array[0]) * 60 * 60
	  							+ Integer.parseInt(array[1]) * 60 ;
	  				}
	  				else
	  				{
	  					durationTemp = Integer.parseInt(array[0]) * 60 
	  							+ Integer.parseInt(array[1])  ;
	  				}
	  			}
	  			else
	  			{
	  				durationTemp = Integer.parseInt(array[0]) * 60 * 60
	  						+ Integer.parseInt(array[1]) * 60 + Integer.parseInt(array[2]);
	  			}

	  		} catch (Exception e) {
	  		}

	  		return durationTemp;
	  	}
	  	
	  	/**
	  	 * 从字符串提取日期YYYY-MM-DD
	  	 * @param dateStr
	  	 * @return
	  	 */
	  	public static String getDate4Str(String dateStr)
	  	{
	  		Pattern pattern = Pattern.compile("(\\d{4}-(\\d){2}-(\\d){2})");
			Matcher matcher = pattern.matcher(dateStr);
			
			if(matcher.find())
			{
				String playTime = matcher.group();
				
				return playTime;
			}
			
			return null;
	  	}
	  	
		/**
		 * 
		 * @param playTime
		 * @return
		 */
		public static Integer time2DurationStr(String playTime)
		{
			if(playTime==null||playTime.trim().isEmpty())
			{
				return 10;
			}

			playTime = playTime.trim();
			Pattern pattern = Pattern.compile("(\\d\\d[:]){1,2}(\\d\\d){0,1}");
			Matcher matcher = pattern.matcher(playTime);
			
			if(matcher.find())
			{
				playTime = matcher.group();
				if(playTime.endsWith(":"))
				{
					playTime = playTime +"00";
				}
				return DateUtil.parsePlayTimeToDuration(playTime, true);
			}
			
			return 10;//默认10秒
		}
}
