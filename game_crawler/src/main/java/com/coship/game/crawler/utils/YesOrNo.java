/**
 * 
 * @cms-domain
 * @PubState.java
 * @907708
 * @2014年3月15日-下午4:05:43
 */
package com.coship.game.crawler.utils;

import java.util.Hashtable;

/**
 * @cms-domain
 * @PubState.java
 * @author 904032
 * @2014年3月15日-下午4:05:43
 */
public enum YesOrNo implements EnumPower {
	yes("1","yes"),
	no("0","no"),
	;
	private String code;
	private String name;


	@Override
	public Integer getInt() {
		return Integer.valueOf(code);
	}
	
	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public static Hashtable<String, EnumPower> getHashMap() {
		return aliasEnums;
	}
	
	private static Hashtable<String, EnumPower> aliasEnums;
	
	YesOrNo(String code ,String name){
		init(code,name);
	}
	
	private void init(String code ,String name){
        this.code = code;
        this.name = name;
        synchronized (this.getClass()) {
            if (aliasEnums == null) {
                aliasEnums = new Hashtable<String, EnumPower>();
            }
        }
        aliasEnums.put(code, this);
	}

	@Override
	public Hashtable<String, EnumPower> getAllEums() {
		return aliasEnums;
	}
	
}
