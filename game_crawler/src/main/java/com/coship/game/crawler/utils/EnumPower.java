package com.coship.game.crawler.utils;

import java.util.Hashtable;

public interface EnumPower {

	public abstract Integer getInt();
	
	public abstract String getCode();

	public abstract String getName();
	
	public Hashtable<String, EnumPower> getAllEums();

}