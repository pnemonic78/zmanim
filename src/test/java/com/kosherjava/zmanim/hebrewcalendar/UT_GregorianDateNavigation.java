/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.kosherjava.zmanim.hebrewcalendar;

import org.junit.*;

import java.util.Calendar;

/**
 * Checks that we can roll forward & backward the gregorian dates...
 */
@SuppressWarnings({ "MagicNumber" })
public class UT_GregorianDateNavigation {

	@Test
	public void gregorianForwardMonthToMonth() {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 31);

		JewishDate hebrewDate = new JewishDate(cal);
		Assert.assertEquals(5771, hebrewDate.getJewishYear());
		Assert.assertEquals(JewishDate.SHEVAT, hebrewDate.getJewishMonth());
		Assert.assertEquals(26, hebrewDate.getJewishDayOfMonth());

		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(Calendar.FEBRUARY, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.SHEVAT, hebrewDate.getJewishMonth());
		Assert.assertEquals(27, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		cal.set(Calendar.DATE, 28);
		hebrewDate.setDate(cal);
		Assert.assertEquals(Calendar.FEBRUARY, hebrewDate.getGregorianMonth());
		Assert.assertEquals(28, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.ADAR, hebrewDate.getJewishMonth());
		Assert.assertEquals(24, hebrewDate.getJewishDayOfMonth());

		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(Calendar.MARCH, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.ADAR, hebrewDate.getJewishMonth());
		Assert.assertEquals(25, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.MARCH);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(Calendar.APRIL, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.ADAR_II, hebrewDate.getJewishMonth());
		Assert.assertEquals(26, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.APRIL);
		cal.set(Calendar.DATE, 30);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(Calendar.MAY, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.NISSAN, hebrewDate.getJewishMonth());
		Assert.assertEquals(27, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.MAY);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(Calendar.JUNE, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.IYAR, hebrewDate.getJewishMonth());
		Assert.assertEquals(28, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.JUNE);
		cal.set(Calendar.DATE, 30);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(Calendar.JULY, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.SIVAN, hebrewDate.getJewishMonth());
		Assert.assertEquals(29, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.JULY);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(Calendar.AUGUST, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.AV, hebrewDate.getJewishMonth());
		Assert.assertEquals(1, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.AUGUST);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(Calendar.SEPTEMBER, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.ELUL, hebrewDate.getJewishMonth());
		Assert.assertEquals(2, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
		cal.set(Calendar.DATE, 30);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(Calendar.OCTOBER, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.TISHREI, hebrewDate.getJewishMonth());
		Assert.assertEquals(3, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.OCTOBER);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(Calendar.NOVEMBER, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(5772, hebrewDate.getJewishYear());
		Assert.assertEquals(JewishDate.CHESHVAN, hebrewDate.getJewishMonth());
		Assert.assertEquals(4, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.NOVEMBER);
		cal.set(Calendar.DATE, 30);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(Calendar.DECEMBER, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.KISLEV, hebrewDate.getJewishMonth());
		Assert.assertEquals(5, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DATE, 31);
		hebrewDate.setDate(cal);
		hebrewDate.forward(Calendar.DATE, 1);
		Assert.assertEquals(2012, hebrewDate.getGregorianYear());
		Assert.assertEquals(Calendar.JANUARY, hebrewDate.getGregorianMonth());
		Assert.assertEquals(1, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.TEVES, hebrewDate.getJewishMonth());
		Assert.assertEquals(6, hebrewDate.getJewishDayOfMonth());
	}


	@Test
	public void gregorianBackwardMonthToMonth() {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);

		JewishDate hebrewDate = new JewishDate(cal);
		hebrewDate.back();
		Assert.assertEquals(2010, hebrewDate.getGregorianYear());
		Assert.assertEquals(Calendar.DECEMBER, hebrewDate.getGregorianMonth());
		Assert.assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.TEVES, hebrewDate.getJewishMonth());
		Assert.assertEquals(24, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.YEAR, 2010);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(Calendar.NOVEMBER, hebrewDate.getGregorianMonth());
		Assert.assertEquals(30, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.KISLEV, hebrewDate.getJewishMonth());
		Assert.assertEquals(23, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.NOVEMBER);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(Calendar.OCTOBER, hebrewDate.getGregorianMonth());
		Assert.assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.CHESHVAN, hebrewDate.getJewishMonth());
		Assert.assertEquals(23, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.OCTOBER);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(Calendar.SEPTEMBER, hebrewDate.getGregorianMonth());
		Assert.assertEquals(30, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.TISHREI, hebrewDate.getJewishMonth());
		Assert.assertEquals(22, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(Calendar.AUGUST, hebrewDate.getGregorianMonth());
		Assert.assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(5770, hebrewDate.getJewishYear());
		Assert.assertEquals(JewishDate.ELUL, hebrewDate.getJewishMonth());
		Assert.assertEquals(21, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.AUGUST);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(Calendar.JULY, hebrewDate.getGregorianMonth());
		Assert.assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.AV, hebrewDate.getJewishMonth());
		Assert.assertEquals(20, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.JULY);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(Calendar.JUNE, hebrewDate.getGregorianMonth());
		Assert.assertEquals(30, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.TAMMUZ, hebrewDate.getJewishMonth());
		Assert.assertEquals(18, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.JUNE);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(Calendar.MAY, hebrewDate.getGregorianMonth());
		Assert.assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.SIVAN, hebrewDate.getJewishMonth());
		Assert.assertEquals(18, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.MAY);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(Calendar.APRIL, hebrewDate.getGregorianMonth());
		Assert.assertEquals(30, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.IYAR, hebrewDate.getJewishMonth());
		Assert.assertEquals(16, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.APRIL);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(Calendar.MARCH, hebrewDate.getGregorianMonth());
		Assert.assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.NISSAN, hebrewDate.getJewishMonth());
		Assert.assertEquals(16, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.MARCH);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(Calendar.FEBRUARY, hebrewDate.getGregorianMonth());
		Assert.assertEquals(28, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.ADAR, hebrewDate.getJewishMonth());
		Assert.assertEquals(14, hebrewDate.getJewishDayOfMonth());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		hebrewDate.setDate(cal);
		hebrewDate.back();
		Assert.assertEquals(Calendar.JANUARY, hebrewDate.getGregorianMonth());
		Assert.assertEquals(31, hebrewDate.getGregorianDayOfMonth());
		Assert.assertEquals(JewishDate.SHEVAT, hebrewDate.getJewishMonth());
		Assert.assertEquals(16, hebrewDate.getJewishDayOfMonth());

	}



} // End of UT_GregorianDateNavigation class
