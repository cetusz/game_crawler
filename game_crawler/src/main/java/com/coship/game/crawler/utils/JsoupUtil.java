package com.coship.game.crawler.utils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupUtil {
	/**
	 * 过滤特殊字符如:\t\n
	 * @return
	 */
	public static String trim(String str){
		return str.replaceAll("\t", "").replaceAll("\n", "").replaceAll("\\s+","");
		
	}
	/**
	 * 替换斜干和空格
	 * @param str
	 * @return
	 */
	public static String replaceStr(String str){
		return str.replaceAll("/",";").replaceAll("\\?", "");
	}
	
	/**
	 * 替换书名号
	 * @return
	 */
	public static String replaceBookQ(String str){
		return str.replaceAll("《","").replaceAll("》", "");
	}
	
	/**
	 * jsoup获取dom节点的值
	 * @param css
	 * @param doc
	 * @param index
	 * @return
	 */
	public static String getText(String css,Document doc,int index){
		String result = "";
		Element el = getElementByCss(css,doc,index);
		if(el!=null){
			result = el.text().replaceAll("\\s+","")
			.replaceAll("\\?","");
		}
		return result;
	}
	
	public static String getTextAndSpace(String css,Document doc,int index){
		String result = "";
		Element el = getElementByCss(css,doc,index);
		if(el!=null){
			result = el.text();
		}
		return result;
	}
	/**
	 * 获取自己标签内的值
	 * @param css
	 * @param doc
	 * @param index
	 * @return
	 */
	public static String getOwnText(String css,Document doc,int index){
		String result = "";
		Element el = getElementByCss(css,doc,index);
		if(el!=null){
			result = el.ownText().replaceAll("\\s+","").replaceAll("\\?","");
		}
		return result;
	}
	/**
	 * 抽取公用方法取得节点
	 * @param css
	 * @param doc
	 * @param index
	 * @return
	 */
	public static Element getElementByCss(String css,Document doc,int index){
		Elements els = doc.select(css);
		Element el = null;
		if(els.size()>index){
			el = els.get(index);	
		}
		return el;
	}
	
	/**
	 * 获取节点的属性值
	 * @param css
	 * @param attribute
	 * @param doc
	 * @param index
	 * @return
	 */
	public static String getAttributeValue(String css,String attribute,Document doc,int index){
		Element el = getElementByCss(css,doc,index);
		String value = "";
		if(el!=null){
			value = el.attr(attribute);
		}
		return value;
	}
	/**
	 * 引用优酷的id作为id存储
	 * @param baseUri
	 * @return
	 */
	public static String getId(Document doc){
		String id= "";
		String baseUri = doc.baseUri();
		if(baseUri.indexOf("id")>-1){
			id = baseUri.substring(baseUri.lastIndexOf("_")+1,baseUri.lastIndexOf("."));
		}
		return id;
	}
	    /**
	     * 转码
	     * @param source 源字符串
	     * @return
	     * @throws UnsupportedEncodingException
	     */
	    public static String decode(String source) throws UnsupportedEncodingException{
	    	return java.net.URLDecoder.decode(source, "utf-8");
	    }
	    /**
	     * 设置编码
	     * @param source
	     * @return
	     * @throws UnsupportedEncodingException
	     */
	    public static String encode(String source) throws UnsupportedEncodingException{
	    	return java.net.URLEncoder.encode(source,"utf-8");
	    }
	    
		/**
		 * 构造查询分析用的用,隔开的条件语句
		 * @param ids
		 * @return
		 */
		public static String genIds(List<String> ids){
			StringBuilder result = new StringBuilder(50);
			for(int index=0,len=ids.size();index<len;index++){
				if(index>0){
					result.append(",");
				}
				result.append("'"+ids.get(index)+"'");
			}
			return result.toString();
		}
		
		/**
		 * 构造数组字符串
		 * @param doc
		 * @param css
		 * @param operator
		 * @return
		 */
		public static String getArrayString(String css,Document doc,String operator){
			Elements els= doc.select(css);
			String result = "";
			int index = 0;
			for(Element el:els){
				if(index>0){
					result+=operator;
				}
				result+=el.ownText();
				index++;
			
			}
			return result;
		}
		/**
		 * 正则匹配数字
		 * @param source
		 * @return
		 */
		public static String getNumberString(String source,int index){
			List<String> matches = new ArrayList<String>();
			Pattern pattern = Pattern.compile("[0-9]{1,}(\\.[0-9]{1,})?");
			Matcher match = pattern.matcher(source);
			while(match.find()){
				matches.add(trim(match.group()));
			}
			if(matches.size()==0){
				return "";
			}
			return matches.get(index);
		}
		/**
		 * 搜索包含查询值的dom节点的，兄弟节点的值，用分隔符分开
		 * @param delimeter 分隔符
		 * @param searText
		 * @param notQuery 不查询的条件
		 * @param index 匹配第几个
		 * @return
		 */
		public static String getSearchDomSiblingsValue(String searText,Document doc,int position,String delimeter,String notQuery){
			Elements els = doc.getElementsContainingOwnText(searText);
			String result = "";
			if(els.size()>0){
				Element el = els.get(0);
				Elements siblings = null;
				if(StringUtils.isNotBlank(notQuery)){
					siblings = el.siblingElements().not(notQuery);
				}else{
					siblings = el.siblingElements();
				}
				for(int index=0,len=siblings.size();index<len;index++){
					if(index>0){
						result += delimeter;
					}
					result += siblings.get(index).ownText();
				}
			}
			return result; 
		}
		
		/**
		 * 取日期
		 * @param source
		 * @param delimetor
		 * @return
		 */
		public static String getDateString(String source,String delimetor){
			Pattern pattern = Pattern.compile("[0-9]{1,}"+delimetor+"[0-9]{1,}"+delimetor+"[0-9]{1,}");
			Matcher match = pattern.matcher(source);
			String result = "";
			if(match.find()){
				result = match.group();
			}
			return result;
		}
		
		/**
		 * 获取包含查询的某个字符的dom节点旁边的text
		 * @param searchText
		 * @param doc
		 * @return
		 */
		public static String getTextNextToHtmlTag(String searchText,Document doc){
			Elements els = doc.getElementsMatchingOwnText(searchText);
			if(els.size()>0){
				return els.get(0).nextSibling().toString();
			}
			return "";
		}
		
		public static void main(String[] args){
			System.out.println(getStr2Numbers("23,456,788"));
		}
		
		public static boolean matchNotNumber(String source){
			Pattern pattern = Pattern.compile("[^0-9]");
			Matcher match = pattern.matcher(source);
			if(match.find())
				return true;
			return false;
		}
		
		
		 //截取数字  
		   public static List<String> getNumbers(String content) {  
			   List<String> stringlist = new ArrayList<String>();
		       Pattern pattern = Pattern.compile("\\d+");  
		       Matcher matcher = pattern.matcher(content);  
		       while (matcher.find()) {  
		    	   stringlist.add(matcher.group());
		       }  
		       return stringlist;  
		   }  
		   
		   public static String getNumberString(String content){
			   String numbers = "";
			   Pattern pattern = Pattern.compile("\\d+");  
		       Matcher matcher = pattern.matcher(content);  
		       while (matcher.find()) {  
		    	   numbers += matcher.group();
		       }  
		       return numbers;
		   }
		   
		   /**
		    * 例如获取23,456,788 --> 23456788
		    * @param content
		    * @return
		    */
		   public static Long getStr2Numbers(String content) {  
			   String numbers = "";
		       Pattern pattern = Pattern.compile("\\d+");  
		       Matcher matcher = pattern.matcher(content);  
		       while (matcher.find()) {  
		    	   numbers+=matcher.group();
		       }  
		       if(StringUtils.isNotEmpty(numbers)){
		    	   return Long.valueOf(numbers); 
		       }
		       return 0L;
		   }
		   
		   public static Document getDocument(String url){
			    Document doc = null;
			    try {
				   doc = Jsoup.connect(url).timeout(5000).get();
			    } catch (IOException e) {
			       e.printStackTrace();
				   return null;
			    }
			   return doc;
		   }
		   
		   public static Document getDocumentWithRetry(String url){
			   Document doc = getDocument(url);
			   if(doc==null){
				   for(int count=1;count<=3;count++){
					   doc = getDocument(url);
					   if(doc!=null){
						   break;
					   }
				   }
			   }
			   return doc;
		   }
		   
		  
		
		
	

}
