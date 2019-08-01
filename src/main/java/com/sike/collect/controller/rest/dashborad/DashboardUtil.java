package com.sike.collect.controller.rest.dashborad;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.netty.util.internal.MathUtil;

public class DashboardUtil {
	
	public void testCalendar() { 
		SimpleDateFormat sdf2=new SimpleDateFormat("MMM dd HH:mm:ss");
        long time = 1559163600000l;
        Date date = new Date(time);
        System.out.println(sdf2.format(date));
       
   } 
	public void math() {
		double price = 89.836; 
		
		System.out.println(String.format("%.2f", price));
	}
	public String createNewJson() {
		JsonObject json = new JsonObject();
		JsonObject json2 = new JsonObject();
		JsonObject json3 = new JsonObject();
		JsonArray  jsonArray = new JsonArray();
		JsonArray  varray = new JsonArray();
		varray.add("6");
		json3.add("value", varray);
		json3.addProperty("field", "entry_time");
		json3.addProperty("type", "date");
		json3.addProperty("operator", "last_n_hours");
		jsonArray.add(json3);
		json2.addProperty("condition", "AND");
		json2.add("rules",jsonArray);
		json.add("query", json2);
		return json.toString();
	}
	public void removeSpecialString() {
		String s ="Con.t_r-ol up.@-";
		String ss =s.replaceAll("\\p{Punct}","").replaceAll("\\s*", "");
		System.out.println(ss);
	}
	public static void main(String[] args) {
		DashboardUtil d = new DashboardUtil();
		//d.testCalendar();
		//d.math();
		d.removeSpecialString();
	}

}
