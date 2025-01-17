/*
 * Zmanim Java API
 * Copyright (C) 2004-2025 Eliyahu Hershfeld
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA,
 * or connect to: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package com.kosherjava.zmanim.util;

import java.util.Calendar;

/**
 * Implementation of sunrise and sunset methods to calculate astronomical times based on the <a
 * href="https://noaa.gov">NOAA</a> algorithm. This calculator uses the Java algorithm based on the implementation by <a
 * href="https://noaa.gov">NOAA - National Oceanic and Atmospheric Administration</a>'s <a href =
 * "https://www.srrb.noaa.gov/highlights/sunrise/sunrise.html">Surface Radiation Research Branch</a>. NOAA's <a
 * href="https://www.srrb.noaa.gov/highlights/sunrise/solareqns.PDF">implementation</a> is based on equations from <a
 * href="https://www.amazon.com/Astronomical-Table-Sun-Moon-Planets/dp/1942675038/">Astronomical Algorithms</a> by <a
 * href="https://en.wikipedia.org/wiki/Jean_Meeus">Jean Meeus</a>. Added to the algorithm is an adjustment of the zenith
 * to account for elevation. The algorithm can be found in the <a
 * href="https://en.wikipedia.org/wiki/Sunrise_equation">Wikipedia Sunrise Equation</a> article.
 * 
 * @author &copy; Eliyahu Hershfeld 2011 - 2025
 */
public class NOAACalculator extends AstronomicalCalculator {
	
	/**
	 * The <a href="https://en.wikipedia.org/wiki/Julian_day">Julian day</a> of January 1, 2000, known as
	 * <a href="https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 */
	private static final double JULIAN_DAY_JAN_1_2000 = 2451545.0;

	/**
	 * Julian days per century.
	 */
	private static final double JULIAN_DAYS_PER_CENTURY = 36525.0;
	
	/**
	 * An <code>enum</code> to indicate what type of solar event ({@link #SUNRISE SUNRISE}, {@link #SUNSET SUNSET},
	 * {@link #NOON NOON} or {@link #MIDNIGHT MIDNIGHT}) is being calculated.
	 * .
	 */
	protected enum SolarEvent {
		/**SUNRISE A solar event related to sunrise*/SUNRISE, /**SUNSET A solar event related to sunset*/SUNSET,
		/**NOON A solar event related to noon*/NOON, /**MIDNIGHT A solar event related to midnight*/MIDNIGHT
	}
	
	/**
	 * Default constructor of the NOAACalculator.
	 */
	public NOAACalculator() {
		super();
	}

	/**
	 * @see com.kosherjava.zmanim.util.AstronomicalCalculator#getCalculatorName()
	 */
	public String getCalculatorName() {
		return "US National Oceanic and Atmospheric Administration Algorithm"; // Implementation of the Jean Meeus algorithm
	}

	/**
	 * @see com.kosherjava.zmanim.util.AstronomicalCalculator#getUTCSunrise(Calendar, GeoLocation, double, boolean)
	 */
	public double getUTCSunrise(Calendar calendar, GeoLocation geoLocation, double zenith, boolean adjustForElevation) {
		double elevation = adjustForElevation ? geoLocation.getElevation() : 0;
		double adjustedZenith = adjustZenith(zenith, elevation);
		double sunrise = getSunRiseSetUTC(calendar, geoLocation.getLatitude(), -geoLocation.getLongitude(),
				adjustedZenith, SolarEvent.SUNRISE);
		sunrise = sunrise / 60;
		return sunrise > 0  ? sunrise % 24 : sunrise % 24 + 24; // ensure that the time is >= 0 and < 24
	}

	/**
	 * @see com.kosherjava.zmanim.util.AstronomicalCalculator#getUTCSunset(Calendar, GeoLocation, double, boolean)
	 */
	public double getUTCSunset(Calendar calendar, GeoLocation geoLocation, double zenith, boolean adjustForElevation) {
		double elevation = adjustForElevation ? geoLocation.getElevation() : 0;
		double adjustedZenith = adjustZenith(zenith, elevation);
		double sunset = getSunRiseSetUTC(calendar, geoLocation.getLatitude(), -geoLocation.getLongitude(),
				adjustedZenith, SolarEvent.SUNSET);
		sunset = sunset / 60;
		return sunset > 0  ? sunset % 24 : sunset % 24 + 24; // ensure that the time is >= 0 and < 24
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Julian_day">Julian day</a> from a Java Calendar.
	 * 
	 * @param calendar
	 *            The Java Calendar
	 * @return the Julian day corresponding to the date Note: Number is returned for start of day. Fractional days
	 *         should be added later.
	 */
	private static double getJulianDay(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if (month <= 2) {
			year -= 1;
			month += 12;
		}
		int a = year / 100;
		int b = 2 - a + a / 4;
		return Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + b - 1524.5;
	}

	/**
	 * Convert <a href="https://en.wikipedia.org/wiki/Julian_day">Julian day</a> to centuries since <a href=
	 * "https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * 
	 * @param julianDay
	 *            the Julian Day to convert
	 * @return the centuries since 2000 Julian corresponding to the Julian Day
	 */
	private static double getJulianCenturiesFromJulianDay(double julianDay) {
		return (julianDay - JULIAN_DAY_JAN_1_2000) / JULIAN_DAYS_PER_CENTURY;
	}

	/**
	 * Returns the Geometric <a href="https://en.wikipedia.org/wiki/Mean_longitude">Mean Longitude</a> of the Sun.
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since <a href=
	 *            "https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the Geometric Mean Longitude of the Sun in degrees
	 */
	private static double getSunGeometricMeanLongitude(double julianCenturies) {
		double longitude = 280.46646 + julianCenturies * (36000.76983 + 0.0003032 * julianCenturies);
		return longitude > 0  ? longitude % 360 : longitude % 360 + 360; // ensure that the longitude is >= 0 and < 360
	}

	/**
	 * Returns the Geometric <a href="https://en.wikipedia.org/wiki/Mean_anomaly">Mean Anomaly</a> of the Sun in degrees.
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since <a href=
	 *            "https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the Geometric Mean Anomaly of the Sun in degrees
	 */
	private static double getSunGeometricMeanAnomaly(double julianCenturies) {
		return 357.52911 + julianCenturies * (35999.05029 - 0.0001537 * julianCenturies);
	}

	/**
	 * Return the unitless <a href="https://en.wikipedia.org/wiki/Eccentricity_%28orbit%29">eccentricity of earth's orbit</a>.
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since <a href=
	 *            "https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the unitless eccentricity
	 */
	private static double getEarthOrbitEccentricity(double julianCenturies) {
		return 0.016708634 - julianCenturies * (0.000042037 + 0.0000001267 * julianCenturies);
	}

	/**
	 * Returns the <a href="https://en.wikipedia.org/wiki/Equation_of_the_center">equation of center</a> for the sun in degrees.
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since <a href=
	 *            "https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the equation of center for the sun in degrees
	 */
	private static double getSunEquationOfCenter(double julianCenturies) {
		double m = getSunGeometricMeanAnomaly(julianCenturies);
		double mrad = Math.toRadians(m);
		double sinm = Math.sin(mrad);
		double sin2m = Math.sin(mrad + mrad);
		double sin3m = Math.sin(mrad + mrad + mrad);
		return sinm * (1.914602 - julianCenturies * (0.004817 + 0.000014 * julianCenturies)) + sin2m
				* (0.019993 - 0.000101 * julianCenturies) + sin3m * 0.000289;
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/True_longitude">true longitude</a> of the sun.
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since <a href=
	 *            "https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the sun's true longitude in degrees
	 */
	private static double getSunTrueLongitude(double julianCenturies) {
		double sunLongitude = getSunGeometricMeanLongitude(julianCenturies);
		double center = getSunEquationOfCenter(julianCenturies); 
		return sunLongitude + center;
	}

	// /**
	// * Returns the <a href="https://en.wikipedia.org/wiki/True_anomaly">true anamoly</a> of the sun.
	// *
	// * @param julianCenturies
	// * the number of Julian centuries since J2000.0
	// * @return the sun's true anamoly in degrees
	// */
	// private static double getSunTrueAnomaly(double julianCenturies) {
	// double meanAnomaly = getSunGeometricMeanAnomaly(julianCenturies);
	// double equationOfCenter = getSunEquationOfCenter(julianCenturies);
	//
	// return meanAnomaly + equationOfCenter;
	// }

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Apparent_longitude">apparent longitude</a> of the sun.
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since <a href=
	 *            "https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return sun's apparent longitude in degrees
	 */
	private static double getSunApparentLongitude(double julianCenturies) {
		double sunTrueLongitude = getSunTrueLongitude(julianCenturies);
		double omega = 125.04 - 1934.136 * julianCenturies;
		double lambda = sunTrueLongitude - 0.00569 - 0.00478 * Math.sin(Math.toRadians(omega));
		return lambda;
	}

	/**
	 * Returns the mean <a href="https://en.wikipedia.org/wiki/Axial_tilt">obliquity of the ecliptic</a> (Axial tilt).
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since <a href=
	 *            "https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the mean obliquity in degrees
	 */
	private static double getMeanObliquityOfEcliptic(double julianCenturies) {
		double seconds = 21.448 - julianCenturies
				* (46.8150 + julianCenturies * (0.00059 - julianCenturies * (0.001813)));
		return 23.0 + (26.0 + (seconds / 60.0)) / 60.0;
	}

	/**
	 * Returns the corrected <a href="https://en.wikipedia.org/wiki/Axial_tilt">obliquity of the ecliptic</a> (Axial
	 * tilt).
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since <a href=
	 *            "https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return the corrected obliquity in degrees
	 */
	private static double getObliquityCorrection(double julianCenturies) {
		double obliquityOfEcliptic = getMeanObliquityOfEcliptic(julianCenturies);
		double omega = 125.04 - 1934.136 * julianCenturies;
		return obliquityOfEcliptic + 0.00256 * Math.cos(Math.toRadians(omega));
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Declination">declination</a> of the sun.
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since <a href=
	 *            "https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return
	 *            the sun's declination in degrees
	 */
	private static double getSunDeclination(double julianCenturies) {
		double obliquityCorrection = getObliquityCorrection(julianCenturies);
		double lambda = getSunApparentLongitude(julianCenturies);
		double sint = Math.sin(Math.toRadians(obliquityCorrection)) * Math.sin(Math.toRadians(lambda));
		double theta = Math.toDegrees(Math.asin(sint));
		return theta;
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Equation_of_time">Equation of Time</a> - the difference between
	 * true solar time and mean solar time
	 * 
	 * @param julianCenturies
	 *            the number of Julian centuries since <a href=
	 *            "https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @return equation of time in minutes of time
	 */
	private static double getEquationOfTime(double julianCenturies) {
		double epsilon = getObliquityCorrection(julianCenturies);
		double geomMeanLongSun = getSunGeometricMeanLongitude(julianCenturies);
		double eccentricityEarthOrbit = getEarthOrbitEccentricity(julianCenturies);
		double geomMeanAnomalySun = getSunGeometricMeanAnomaly(julianCenturies);
		double y = Math.tan(Math.toRadians(epsilon) / 2.0);
		y *= y;
		double geomMeanLongSunRad = Math.toRadians(geomMeanLongSun);
		double geomMeanAnomalySunRad = Math.toRadians(geomMeanAnomalySun);
		double sin2l0 = Math.sin(2.0 * geomMeanLongSunRad);
		double sinm = Math.sin(geomMeanAnomalySunRad);
		double cos2l0 = Math.cos(2.0 * geomMeanLongSunRad);
		double sin4l0 = Math.sin(4.0 * geomMeanLongSunRad);
		double sin2m = Math.sin(2.0 * geomMeanAnomalySunRad);
		double equationOfTime = y * sin2l0 - 2.0 * eccentricityEarthOrbit * sinm + 4.0 * eccentricityEarthOrbit * y
				* sinm * cos2l0 - 0.5 * y * y * sin4l0 - 1.25 * eccentricityEarthOrbit * eccentricityEarthOrbit * sin2m;
		return Math.toDegrees(equationOfTime) * 4.0;
	}
	
	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Hour_angle">hour angle</a> of the sun in
	 * <a href="https://en.wikipedia.org/wiki/Radian">radians</a> at for the latitude.
	 * 
	 * @param latitude
	 *            the latitude of observer in degrees
	 * @param solarDeclination
	 *            the declination angle of sun in degrees
	 * @param zenith
	 *            the zenith
	 * @param solarEvent
	 *             If the hour angle is for {@link SolarEvent#SUNRISE SUNRISE} or {@link SolarEvent#SUNSET SUNSET}
	 * @return hour angle of sunrise in <a href="https://en.wikipedia.org/wiki/Radian">radians</a>
	 */
	private static double getSunHourAngle(double latitude, double solarDeclination, double zenith, SolarEvent solarEvent) {
		double latRad = Math.toRadians(latitude);
		double sdRad = Math.toRadians(solarDeclination);
		double zRad = Math.toRadians(zenith);
		double hourAngle = (Math.acos(Math.cos(zRad) / (Math.cos(latRad) * Math.cos(sdRad))
				- Math.tan(latRad) * Math.tan(sdRad)));
		
		if (solarEvent == SolarEvent.SUNSET) {
			hourAngle = -hourAngle;
		}
		return hourAngle;
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Celestial_coordinate_system">Solar Elevation</a> for the
	 * horizontal coordinate system at the given location at the given time. Can be negative if the sun is below the
	 * horizon. Not corrected for altitude.
	 * 
	 * @param calendar
	 *            time of calculation
	 * @param latitude
	 *            latitude of location for calculation
	 * @param longitude
	 *            longitude of location for calculation
	 * @return solar elevation in degrees - horizon is 0 degrees, civil twilight is -6 degrees
	 */

	public static double getSolarElevation(Calendar calendar, double latitude, double longitude) {
		double julianDay = getJulianDay(calendar);
		double julianCenturies = getJulianCenturiesFromJulianDay(julianDay);
		Double eot = getEquationOfTime(julianCenturies);
		double adjustedLongitude = (calendar.get(Calendar.HOUR_OF_DAY) + 12.0)
				+ (calendar.get(Calendar.MINUTE) + eot + calendar.get(Calendar.SECOND) / 60.0) / 60.0;
		adjustedLongitude = -(adjustedLongitude * 360.0 / 24.0) % 360.0;
		double hourAngle_rad = Math.toRadians(longitude - adjustedLongitude);
		double declination = getSunDeclination(julianCenturies);
		double dec_rad = Math.toRadians(declination);
		double lat_rad = Math.toRadians(latitude);
		return Math.toDegrees(Math.asin((Math.sin(lat_rad) * Math.sin(dec_rad))
				+ (Math.cos(lat_rad) * Math.cos(dec_rad) * Math.cos(hourAngle_rad))));

	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Celestial_coordinate_system">Solar Azimuth</a> for the
	 * horizontal coordinate system at the given location at the given time. Not corrected for altitude. True south is 0
	 * degrees.
	 * 
	 * @param calendar
	 *            time of calculation
	 * @param latitude
	 *            latitude of location for calculation
	 * @param longitude
	 *            longitude of location for calculation
	 * @return the solar azimuth
	 */

	public static double getSolarAzimuth(Calendar calendar, double latitude, double longitude) {
		double julianDay = getJulianDay(calendar);
		double julianCenturies = getJulianCenturiesFromJulianDay(julianDay);
		Double eot = getEquationOfTime(julianCenturies);
		double adjustedLongitude = (calendar.get(Calendar.HOUR_OF_DAY) + 12.0)
				+ (calendar.get(Calendar.MINUTE) + eot + calendar.get(Calendar.SECOND) / 60.0) / 60.0;
		adjustedLongitude = -(adjustedLongitude * 360.0 / 24.0) % 360.0;
		double hourAngle_rad = Math.toRadians(longitude - adjustedLongitude);
		double declination = getSunDeclination(julianCenturies);
		double dec_rad = Math.toRadians(declination);
		double lat_rad = Math.toRadians(latitude);
		return Math.toDegrees(Math.atan(Math.sin(hourAngle_rad)
				/ ((Math.cos(hourAngle_rad) * Math.sin(lat_rad)) - (Math.tan(dec_rad) * Math.cos(lat_rad)))))+180;

	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Universal_Coordinated_Time">Universal Coordinated Time</a> (UTC)
	 * of <a href="https://en.wikipedia.org/wiki/Noon#Solar_noon">solar noon</a> for the given day at the given location
	 * on earth. This implementation returns true solar noon as opposed to the time halfway between sunrise and sunset.
	 * Other calculators may return a more simplified calculation of halfway between sunrise and sunset. See <a href=
	 * "https://kosherjava.com/2020/07/02/definition-of-chatzos/">The Definition of <em>Chatzos</em></a> for details on
	 * solar noon calculations.
	 * @see com.kosherjava.zmanim.util.AstronomicalCalculator#getUTCNoon(Calendar, GeoLocation)
	 * @see #getSolarNoonMidnightUTC(double, double, SolarEvent)
	 * 
	 * @param calendar
	 *            The Calendar representing the date to calculate solar noon for
	 * @param geoLocation
	 *            The location information used for astronomical calculating sun times. This class uses only requires
	 *            the longitude for calculating noon since it is the same time anywhere along the longitude line.
	 * @return the time in minutes from zero UTC
	 */
	public double getUTCNoon(Calendar calendar, GeoLocation geoLocation) {
		double noon = getSolarNoonMidnightUTC(getJulianDay(calendar), -geoLocation.getLongitude(), SolarEvent.NOON);
		noon = noon / 60;
		return noon > 0  ? noon % 24 : noon % 24 + 24; // ensure that the time is >= 0 and < 24
	}
	
	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Universal_Coordinated_Time">Universal Coordinated Time</a>
	 * (UTC) of the <a href="https://en.wikipedia.org/wiki/Midnight">solar midnight</a> for the end of the given civil
	 * day at the given location on earth (about 12 hours after solar noon). This implementation returns true solar
	 * midnight as opposed to the time halfway between sunrise and sunset. Other calculators may return a more
	 * simplified calculation of halfway between sunrise and sunset. See <a href=
	 * "https://kosherjava.com/2020/07/02/definition-of-chatzos/">The Definition of <em>Chatzos</em></a> for details on
	 * solar noon / midnight calculations.
	 * @see com.kosherjava.zmanim.util.AstronomicalCalculator#getUTCNoon(Calendar, GeoLocation)
	 * @see #getSolarNoonMidnightUTC(double, double, SolarEvent)
	 * 
	 * @param calendar
	 *            The Calendar representing the date to calculate solar noon for
	 * @param geoLocation
	 *            The location information used for astronomical calculating sun times. This class uses only requires
	 *            the longitude for calculating noon since it is the same time anywhere along the longitude line.
	 * @return the time in minutes from zero UTC
	 */
	public double getUTCMidnight(Calendar calendar, GeoLocation geoLocation) {
		double midnight = getSolarNoonMidnightUTC(getJulianDay(calendar), -geoLocation.getLongitude(), SolarEvent.MIDNIGHT);
		midnight = midnight / 60;
		return midnight > 0  ? midnight % 24 : midnight % 24 + 24; // ensure that the time is >= 0 and < 24
	}

	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Universal_Coordinated_Time">Universal Coordinated Time</a> (UTC)
	 * of the current day <a href="http://en.wikipedia.org/wiki/Noon#Solar_noon">solar noon</a> or the the upcoming
	 * midnight (about 12 hours after solar noon) of the given day at the given location on earth.
	 * 
	 * @param julianDay
	 *            The Julian day since <a href=
	 *            "https://en.wikipedia.org/wiki/Epoch_(astronomy)#J2000">J2000.0</a>.
	 * @param longitude
	 *            The longitude of observer in degrees
	 * @param solarEvent
	 *            If the calculation is for {@link SolarEvent#NOON NOON} or {@link SolarEvent#MIDNIGHT MIDNIGHT}
	 *            
	 * @return the time in minutes from zero UTC
	 * 
	 * @see com.kosherjava.zmanim.util.AstronomicalCalculator#getUTCNoon(Calendar, GeoLocation)
	 * @see #getUTCNoon(Calendar, GeoLocation)
	 */
	private static double getSolarNoonMidnightUTC(double julianDay, double longitude, SolarEvent solarEvent) {
		julianDay = (solarEvent == SolarEvent.NOON) ? julianDay : julianDay + 0.5;
		// First pass for approximate solar noon to calculate equation of time
		double tnoon = getJulianCenturiesFromJulianDay(julianDay + longitude / 360.0);
		double equationOfTime = getEquationOfTime(tnoon);
		double solNoonUTC = (longitude * 4) - equationOfTime; // minutes
		
		// second pass
		double newt = getJulianCenturiesFromJulianDay(julianDay + solNoonUTC / 1440.0);
		equationOfTime = getEquationOfTime(newt);
		return (solarEvent == SolarEvent.NOON ? 720 : 1440) + (longitude * 4) - equationOfTime;
	}
	
	/**
	 * Return the <a href="https://en.wikipedia.org/wiki/Universal_Coordinated_Time">Universal Coordinated Time</a> (UTC)
	 * of sunrise or sunset in minutes for the given day at the given location on earth.
	 * @todo Possibly increase the number of passes for improved accuracy, especially in the Arctic areas.
	 * 
	 * @param calendar
	 *            The calendar
	 * @param latitude
	 *            The latitude of observer in degrees
	 * @param longitude
	 *            Longitude of observer in degrees
	 * @param zenith
	 *            Zenith
	 * @param solarEvent
	 *             If the calculation is for {@link SolarEvent#SUNRISE SUNRISE} or {@link SolarEvent#SUNSET SUNSET}
	 * @return the time in minutes from zero Universal Coordinated Time (UTC)
	 */
	private static double getSunRiseSetUTC(Calendar calendar, double latitude, double longitude, double zenith,
			SolarEvent solarEvent) {
		double julianDay = getJulianDay(calendar);

		// Find the time of solar noon at the location, and use that declination.
		// This is better than start of the Julian day
		// TODO really not needed since the Julian day starts from local fixed noon. Changing this would be more
		// efficient but would likely cause a very minor discrepancy in the calculated times (likely not reducing
		// accuracy, just slightly different, thus potentially breaking test cases). Regardless, it would be within
		// milliseconds.
		double noonmin = getSolarNoonMidnightUTC(julianDay, longitude, SolarEvent.NOON);
																						
		double tnoon = getJulianCenturiesFromJulianDay(julianDay + noonmin / 1440.0);

		// First calculates sunrise and approximate length of day
		double equationOfTime = getEquationOfTime(tnoon);
		double solarDeclination = getSunDeclination(tnoon);
		double hourAngle = getSunHourAngle(latitude, solarDeclination, zenith, solarEvent);
		if (Double.isNaN(hourAngle)) {
			hourAngle = interpolateHourAngle1(julianDay, latitude, longitude, zenith, solarEvent);
			if (Double.isNaN(hourAngle)) {
				return Double.NaN;
			}
		}
		double delta = longitude - Math.toDegrees(hourAngle);
		double timeDiff = 4 * delta;
		double timeUTC = 720 + timeDiff - equationOfTime;

		// Second pass includes fractional Julian Day in gamma calc
		double newt = getJulianCenturiesFromJulianDay(julianDay + timeUTC / 1440.0);
		equationOfTime = getEquationOfTime(newt);

		solarDeclination = getSunDeclination(newt);
		hourAngle = getSunHourAngle(latitude, solarDeclination, zenith, solarEvent);
		if (Double.isNaN(hourAngle)) {
			hourAngle = interpolateHourAngle2(julianDay, latitude, longitude, zenith, solarEvent);
			if (Double.isNaN(hourAngle)) {
				return Double.NaN;
			}
		}
		delta = longitude - Math.toDegrees(hourAngle);
		timeDiff = 4 * delta;
		timeUTC = 720 + timeDiff - equationOfTime;
		return timeUTC;
	}

	private static double getSunHourAngle1(double julianDay, double latitude, double longitude, double zenith, SolarEvent solarEvent) {
		double noonmin = getSolarNoonMidnightUTC(julianDay, longitude, SolarEvent.NOON);
		double tnoon = getJulianCenturiesFromJulianDay(julianDay + noonmin / 1440.0);
		double solarDeclination = getSunDeclination(tnoon);
		return getSunHourAngle(latitude, solarDeclination, zenith, solarEvent);
	}

	private static double getSunHourAngle2(double julianDay, double latitude, double longitude, double zenith, SolarEvent solarEvent) {
		double noonmin = getSolarNoonMidnightUTC(julianDay, longitude, SolarEvent.NOON);
		double tnoon = getJulianCenturiesFromJulianDay(julianDay + noonmin / 1440.0);

		// First calculates sunrise and approximate length of day
		double equationOfTime = getEquationOfTime(tnoon);
		double solarDeclination = getSunDeclination(tnoon);
		double hourAngle = getSunHourAngle(latitude, solarDeclination, zenith, solarEvent);
		if (Double.isNaN(hourAngle)) {
			return Double.NaN;
		}
		double delta = longitude - Math.toDegrees(hourAngle);
		double timeDiff = 4 * delta;
		double timeUTC = 720 + timeDiff - equationOfTime;

		// Second pass includes fractional Julian Day in gamma calc
		double newt = getJulianCenturiesFromJulianDay(julianDay + timeUTC / 1440.0);

		solarDeclination = getSunDeclination(newt);
		return getSunHourAngle(latitude, solarDeclination, zenith, solarEvent);
	}

	/**
	 * Use linear interpolation to calculate a missing angle.
	 */
	private static double interpolateHourAngle1(double julianDay, double latitude, double longitude, double zenith, SolarEvent solarEvent) {
		double hourAngle;
		double x1 = 0;
		double y1 = 0;
		double x2 = 0;
		double y2 = 0;

		double d = julianDay - 1;
		final double dayFirst = Math.max(julianDay - 366, 1);
		while (d >= dayFirst) {
			hourAngle = getSunHourAngle1(d, latitude, longitude, zenith, solarEvent);

			if (!Double.isNaN(hourAngle)) {
				x1 = d;
				y1 = hourAngle;
				break;
			}
			d--;
		}

		d = julianDay + 1;
		final double dayLast = julianDay + 366;
		while (d <= dayLast) {
			hourAngle = getSunHourAngle1(d, latitude, longitude, zenith, solarEvent);

			if (!Double.isNaN(hourAngle)) {
				if (x1 == 0) {
					x1 = d;
					y1 = hourAngle;
					d++;
					continue;
				}
				x2 = d;
				y2 = hourAngle;
				break;
			}
			d++;
		}

		if ((x1 == 0) || (x2 == 0)) {
			return Double.NaN;
		}
		double dx = x2 - x1;
		if (dx == 0) {
			return Double.NaN;
		}
		double dy = y2 - y1;
		return y1 + ((julianDay - x1) * dy / dx);
	}

	/**
	 * Use linear interpolation to calculate a missing angle.
	 */
	private static double interpolateHourAngle2(double julianDay, double latitude, double longitude, double zenith, SolarEvent solarEvent) {
		double hourAngle;
		double x1 = 0;
		double y1 = 0;
		double x2 = 0;
		double y2 = 0;

		double d = julianDay - 1;
		final double dayFirst = Math.max(julianDay - 366, 1);
		while (d >= dayFirst) {
			hourAngle = getSunHourAngle2(d, latitude, longitude, zenith, solarEvent);

			if (!Double.isNaN(hourAngle)) {
				x1 = d;
				y1 = hourAngle;
				break;
			}
			d--;
		}

		d = julianDay + 1;
		final double dayLast = julianDay + 366;
		while (d <= dayLast) {
			hourAngle = getSunHourAngle2(d, latitude, longitude, zenith, solarEvent);

			if (!Double.isNaN(hourAngle)) {
				if (x1 == 0) {
					x1 = d;
					y1 = hourAngle;
					d++;
					continue;
				}
				x2 = d;
				y2 = hourAngle;
				break;
			}
			d++;
		}

		if ((x1 == 0) || (x2 == 0)) {
			return Double.NaN;
		}
		double dx = x2 - x1;
		if (dx == 0) {
			return Double.NaN;
		}
		double dy = y2 - y1;
		return y1 + ((julianDay - x1) * dy / dx);
	}
}
