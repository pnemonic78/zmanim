package com.kosherjava.zmanim.hebrewcalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;

import org.junit.*;

import junit.framework.TestCase;
public class UT_YerushalmiTest {
	private static HebrewDateFormatter hdf = new HebrewDateFormatter();
	static {
		hdf.setHebrewFormat(true);		
	}
	 
	@Test
	public void testCorrectDaf1() {
		JewishCalendar jewishCalendar = new JewishCalendar(5777,JewishCalendar.ELUL,10);
		assertEquals(8, jewishCalendar.getDafYomiYerushalmi().getDaf());
		assertEquals(29, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

	@Test
	public void testCorrectDaf2() {
		JewishCalendar jewishCalendar = new JewishCalendar(5744,JewishCalendar.KISLEV,1);
		assertEquals(26, jewishCalendar.getDafYomiYerushalmi().getDaf());
		assertEquals(32, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}
	
	@Test
	public void testCorrectDaf3() {
		JewishCalendar jewishCalendar = new JewishCalendar(5782,JewishCalendar.SIVAN,1);
		assertEquals(15, jewishCalendar.getDafYomiYerushalmi().getDaf());
		assertEquals(33, jewishCalendar.getDafYomiYerushalmi().getMasechtaNumber());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

	@Test
	public void testCorrectSpecialDate() {
		JewishCalendar jewishCalendar = new JewishCalendar(5775,JewishCalendar.TISHREI,10);
		assertNull(jewishCalendar.getDafYomiYerushalmi());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
		
		jewishCalendar = new JewishCalendar(5783,JewishCalendar.AV,9);
		assertNull(jewishCalendar.getDafYomiYerushalmi());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
		
		jewishCalendar = new JewishCalendar(5775,JewishCalendar.AV,10);// 9 Av delayed to Sunday 10 Av
		assertNull(jewishCalendar.getDafYomiYerushalmi());
		System.out.println(hdf.formatDafYomiYerushalmi(jewishCalendar.getDafYomiYerushalmi()));
	}

}
