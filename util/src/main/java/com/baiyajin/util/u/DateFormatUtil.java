package com.baiyajin.util.u;

import com.baiyajin.entity.bean.DataTempVo;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.DateUtil;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateFormatUtil {




	/**
	 * 设置日期时间
	 * 
	 * @param date
	 *            需要设置的时间
	 * @param field
	 *            设置的类型 （1：年，2月，5天，10小时，12分，13秒）
	 * @param value
	 *            设置的值
	 * @return 设置好的日期
	 */
	public static Date setDate(Date date, Integer field, Integer value) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(field, value);
		return gc.getTime();
	}

	/**
	 * 字符串转日期（默认字符串格式："yyyy-MM-dd HH:mm:ss"）
	 * 
	 * @param dateString
	 *            字符串
	 * @return
	 * @throws ParseException
	 *             参数字符串转换异常
	 */
	public static Date stringToDate(String dateString) throws ParseException {
		return stringToDate(dateString, "yyyy-MM-dd HH:mm:ss");
	}




	/**
	 * 字符串转日期
	 * 
	 * @param dateString
	 *            字符串
	 * @param format
	 *            日期字符串格式
	 * @return
	 * @throws ParseException
	 *             参数字符串转换异常
	 */
	public static Date stringToDate(String dateString, String format) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(dateString);
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 * @return （默认字符串格式："yyyy-MM-dd HH:mm:ss"）
	 */
	public static String dateToString(Date date) {
		return dateToString(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 * @param formatString:日期字符串格式
	 * @return
	 */
	public static String dateToString(Date date, String formatString) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatString);
		try {

			return formatter.format(date);
		} catch (Exception e) {

			e.printStackTrace();
			return formatter.format(new Date());
		}
	}

	/**
	 * 日期计算
	 * 
	 * @param date
	 * @param type（1：年，2月，3周，5天，10小时，12分，13秒）
	 * @param val
	 *            加减天数
	 * @return
	 */
	public static Date dateCompute(Date date, Integer type, Integer val) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(type, val);
		return gc.getTime();
	}

	/**
	 * 日期计算并返回string
	 * 
	 * @param date
	 *            （日期格式默认为"yyyy-MM-dd HH:mm:ss"）
	 * @param type
	 *            1：年，2：月份，3：周，5天
	 * @param val
	 *            加减天数
	 * @return
	 */
	public static String dateComputeToString(Date date, Integer type, Integer val) {
		return dateToString(dateCompute(date, type, val));
	}

	/**
	 * 日期计算并返回string
	 * 
	 * @param date
	 * @param format:日期格式
	 * @param type
	 *            1：年，2：月份，3：周，5天
	 * @param val
	 *            加减天数
	 * @return
	 */
	public static String dateComputeToString(Date date, String format, Integer type, Integer val) {
		date = dateCompute(date, type, val);
		return dateToString(date, format);
	}

	/**
	 * @Title: compareDate
	 * @Description: TODO(日期比较，如果s>e 返回1, s=e 返回0,s<e 返回-1)
	 * @param s
	 * @param e
	 * @return boolean
	 */
	public static int compareDate(Date s, Date e) {
		Long sLong = s.getTime();
		Long eLong = e.getTime();
		Long result = sLong - eLong;

		return (int) (result == 0 ? result : result / Math.abs(result));
	}

	/**
	 * @Title: compareDate
	 * @Description: TODO(日期比较，如果s>e 返回1, s=e 返回0,s<e 返回-1)
	 * @param s
	 * @param e
	 * @param type
	 *            比较类型
	 * @return boolean
	 * @throws ParseException
	 */
	public static int compareDate(Date s, Date e, String type) throws ParseException {

		if ("day".equals(type)) {
			s = stringToDate(dateToString(s), "yyyy-MM-dd");
			e = stringToDate(dateToString(e), "yyyy-MM-dd");
		}

		if ("month".equals(type)) {
			s = stringToDate(dateToString(s), "yyyy-MM");
			e = stringToDate(dateToString(e), "yyyy-MM");
		}

		if ("hour".equals(type)) {
			s = stringToDate(dateToString(s), "yyyy-MM-dd HH");
			e = stringToDate(dateToString(e), "yyyy-MM-dd HH");
		}

		if ("half".equals(type)) {
			s = stringToDate(dateToString(s), "yyyy-MM-dd HH:mm");
			e = stringToDate(dateToString(e), "yyyy-MM-dd HH:mm");
		}

		if ("ten".equals(type)) {
			s = stringToDate(dateToString(s), "yyyy-MM-dd HH:mm");
			e = stringToDate(dateToString(e), "yyyy-MM-dd HH:mm");
		}
		return compareDate(s, e);
	}

	/**
	 * 获取当前季度
	 */
//	public static String getQuarter() {
//		return getQuarter( Calendar.getInstance());
//	}


	public static Map<String,Date> getDateByYear(int year) throws ParseException {
		//设置的类型 （1：年，2月，5天，10小时，12分，13秒）
		Map<String,Date> map = new HashMap<>();
		Date stDate =  stringToDate(year+"-01-01 00:00:00");

		Date endDate = stringToDate(year+"-12-31 23:59:59");
		map.put("startDate",stDate);
		map.put("endDate",endDate);
		return map;
	}



	public static Map<String,Date> getDateByQuarter(int quarter,int year){
		Map<String,Date> map = new HashMap<>();
		int stMonth = quarter * 3 - 2;
		int endMonth = stMonth + 2;
		Date stDate =  new Date();
		Date endDate =  new Date();
		stDate = setDate(stDate,1,year);
		stDate = setDate(stDate,2,stMonth-1);
		stDate = setDate(stDate,5,1);
		endDate = setDate(stDate,1,year);
		endDate = setDate(stDate,2,endMonth-1);
		endDate = DateUtils.parseDate(getDateLastDay(endDate),"yyyy-MM-dd");
		map.put("startDate",stDate);
		map.put("endDate",endDate);
		return map;
	}

	/**
	 * 获取起始时间
	 * @param type 1代表月度，2代表季度，3代表年度
	 * @param param
	 * @return
	 */
	public static Map<String,Date> getStAndEndTime(int type,String param) throws ParseException {
		Map<String,Date> map = new HashMap<>();
		if (type == 1){
			Date stDate =  new Date();
			Date endDate =  new Date();
			stDate = setDate(DateUtils.parseDate(param,"yyyy-MM"),5,1);
			endDate = DateUtils.parseDate(getDateLastDay(DateUtils.parseDate(param,"yyyy-MM")),"yyyy-MM-dd");
			map.put("startDate",stDate);
			map.put("endDate",endDate);
			return map;
		}
		if (type == 2){
			return DateFormatUtil.getDateByQuarter(Integer.valueOf(param),DateUtils.getCurrentYear());
		}

		if (type == 3){
			return  DateFormatUtil.getDateByYear(Integer.valueOf(param));
		}
		return null;
	}



	/**
	 * 获取季度
	 */
	public static Integer getQuarter(Date date) {
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		return getQuarter(cal);
	}


	/**
	 * 获取季度
	 */
	public static Integer getQuarter(Calendar c) {

		int month = c.get(c.MONTH) + 1;
		int quarter = 0;
		if (month >= 1 && month <= 3) {
			quarter = 1;
		} else if (month >= 4 && month <= 6) {
			quarter = 2;
		} else if (month >= 7 && month <= 9) {
			quarter = 3;
		} else {
			quarter = 4;
		}
		return quarter;
	}


	public static Date getQuarterFrsitDay(Calendar c) {

		int quarter = getQuarter(c);
		if(quarter==1){
//			stringToDate();
		}



		int s = c.get(c.MONTH)+1;


		return null;
	}

	public static String getDateLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Integer year = calendar.get(Calendar.YEAR);
		Integer month = calendar.get(Calendar.MONTH);
		return getDateLastDay(year.toString(),month.toString());

	}

	public static String getDateLastDay(String year, String month) {
		//year="2018" month="2"
		Calendar calendar = Calendar.getInstance();
		// 设置时间,当前时间不用设置
		calendar.set(Calendar.YEAR, Integer.parseInt(year));
		calendar.set(Calendar.MONTH, Integer.parseInt(month));

		// System.out.println(calendar.getTime());

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		return format.format(calendar.getTime());
	}

	/**
	 * 日期转换成字符串
	 * @param date
	 * @return str
	 */
	public static String dateToStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;
	}

	public static String dateToStr2(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String str = format.format(date);
		return str;
	}

	public static Map<String,Integer> getYearByDate(Date endDate) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(DateFormatUtil.dateToStr(endDate));
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1; // 0-based!
		int day = now.get(Calendar.DAY_OF_MONTH);
		Map<String,Integer> map = new HashMap<>();
		map.put("year",year);
		map.put("month",month);
		return map;
	}


	public static  List<String>  getYearAndMonth (String beginDate,String endDate) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(beginDate));

		List<String> a = new ArrayList();
		for (long d = cal.getTimeInMillis(); d <= sdf.parse(endDate).getTime(); d = get_D_Plaus_1(cal)) {
			if(!a.contains(sdf.format(d))){
				a.add(sdf.format(d));
			}
		}
		return  a;
	}

	public static long get_D_Plaus_1(Calendar c) {
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
		return c.getTimeInMillis();
	}
	public static  List<DataTempVo> fillUp(List<String> list, List<DataTempVo> entityList) {
		List<DataTempVo> dataTempVoList = new ArrayList<>();
		dataTempVoList.addAll(entityList);
		DataTempVo mm =  new DataTempVo();
		if (list != null && list.size() > 0) {
			boolean flg = true;
			for (String s : list) {
				for (DataTempVo d : entityList) {
					mm = d;
					flg = true;
					if (s.equals(d.getMaDate())) {
						flg = false;
						mm = d;
						break;
					}
				}

				if (flg) {
					DataTempVo dataTempVo = new DataTempVo();
					dataTempVo.setMaDate(s);
					dataTempVo.setAreaId(mm.getAreaId());
					dataTempVo.setMId(mm.getMId());
					dataTempVo.setAreaName(mm.getAreaName());
					dataTempVo.setMaName(mm.getMaName());



					dataTempVoList.add(dataTempVo);

				}
			}
		}
			DateFormatUtil.listSort2(dataTempVoList);
			return dataTempVoList;
		}


	public static  List<Map<String,Object>> fillUpMap(List<String> list, List<Map<String,Object>> entityList) throws ParseException {
		List<Map<String,Object>> dataTempVoList = new ArrayList<>();
		dataTempVoList.addAll(entityList);
		Map<String,Object> mm =  new HashMap<String,Object>();
		if (list != null && list.size() > 0){
			boolean flg = true;
			for (String s:list){
				for (Map<String,Object> m : entityList){
					mm.clear();
					mm.putAll(m);
					String mDate = "";
					if(m.get("mdate")!=null) {
						mDate = DateFormatUtil.dateToString(DateFormatUtil.stringToDate(m.get("mdate").toString()), "YYYY-MM");
					}
					flg = true;
					if (s.equals(mDate)){
						flg = false;
						mm.clear();
						mm.putAll(m);
						break;
					}
				}
				if (flg){
					Map<String,Object> map = new HashMap<String,Object>();
					map.putAll(mm);
					map.put("mdate",s);
					map.put("price",0.00);
					map.put("huanbi",0.00);
					map.put("tongbi",0.00);
					map.put("exponent",0.00);
					dataTempVoList.add(map);
				}
			}
			listSort(dataTempVoList,"mdate");
		}
		return dataTempVoList;
	}

	public static  List<Map<String,Object>> fillUpMapasmdate(List<String> list, List<Map<String,Object>> entityList) throws ParseException {
		List<Map<String,Object>> dataTempVoList = new ArrayList<>();
		dataTempVoList.addAll(entityList);
		Map<String,Object> mm =  new HashMap<String,Object>();
		if (list != null && list.size() > 0){
			boolean flg = true;
			for (String s:list){
				for (Map<String,Object> m : entityList){
					mm.clear();
					mm.putAll(m);
					String mDate = "";
					if(m.get("asmdate")!=null) {
						mDate = DateFormatUtil.dateToString(DateFormatUtil.stringToDate(m.get("asmdate").toString()), "YYYY-MM");
					}
					flg = true;
					if (s.equals(mDate)){
						flg = false;
						mm.clear();
						mm.putAll(m);
						break;
					}
				}
				if (flg){
					Map<String,Object> map = new HashMap<String,Object>();
					map.putAll(mm);
					map.put("asmdate",s);
					map.put("price",0.00);
					map.put("huanbi",0.00);
					map.put("tongbi",0.00);
					map.put("exponent",0.00);
					dataTempVoList.add(map);
				}
			}

			listSort(dataTempVoList,"asmdate");
		}
		return dataTempVoList;
	}

	public static void listSort2(List<DataTempVo> list) {
		Collections.sort(
				list, new Comparator<DataTempVo>() {
					public int compare(DataTempVo o1, DataTempVo o2) {
						String mdate1 = o1.getMaDate();//mdate1是从你list里面拿出来的一个
						String mdate2 = o2.getMaDate(); //mdate2是从你list里面拿出来的第二个
						return mdate1.compareTo(mdate2);
					}
				});
	}






		public static void listSort(List<Map<String, Object>> list,String type) {
			Collections.sort(
					list, new Comparator<Map<String, Object>>() {
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					String mdate1 = o1.get(type).toString() ;//mdate1是从你list里面拿出来的一个
					String mdate2 =o2.get(type).toString() ; //mdate2是从你list里面拿出来的第二个
					return mdate1.compareTo(mdate2);
				}
			});
		}

	}