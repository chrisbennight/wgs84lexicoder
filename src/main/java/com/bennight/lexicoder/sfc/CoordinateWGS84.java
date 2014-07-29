package com.bennight.lexicoder.sfc;

public class CoordinateWGS84 {

	private double _latitude = Double.NaN; //latitude from 0->180
	private double _longitude = Double.NaN; //longitude from 0->360


	public CoordinateWGS84(){}
	public CoordinateWGS84(double longitude, double latitude){
		setLatitude(latitude);
		setLongitude(longitude);
	}

	public CoordinateWGS84(long longitudeNormal, long latitudeNormal, long precision){
		setNormalizedLatitude(latitudeNormal, precision);
		setNormalizedLongitude(longitudeNormal, precision);
	}

	public CoordinateWGS84(long[] point, long precision){
		setNormalizedLongitude(point[0], precision);
		setNormalizedLatitude(point[1], precision);
	}

	public double getLatitude(){
		return _latitude -90;
	}

	public double getLongitude(){
		return _longitude -180;
	}

	/***
	 * @return latitude normalized from [0,PRECISON) 
	 */
	public long getNormalLatitude(long precision){
		return normalizeLatitude(_latitude, precision);
	}

	/***
	 * @return longitude normalized from [0,PRECISON)
	 */
	public long getNormalLongitude(long precision){
		return  normalizeLongitude(_longitude, precision);
	}


	public void setLatitude(double latitude){
		if (latitude < -90 || latitude > 90){
			throw new NumberFormatException("Latitude must be in the WGS84 CRS - (90 >= latitude >= -90)");
		}
		_latitude = latitude + 90;
	}

	public void setLongitude(double longitude){
		if (longitude < -180 || longitude > 180){
			throw new NumberFormatException("Longitude must be in the WGS84 CRS - (180 >= longitude >= -180)");
		}
		_longitude = longitude + 180;

	}

	public void setNormalizedLatitude(long latitudeNormal, long precision){
		if (latitudeNormal < 0 || latitudeNormal > precision){
			throw new NumberFormatException("Normalized latitude must be greater than 0 and less than the maximum precision");
		}
		_latitude = denormalizeLatitude(latitudeNormal, precision);
	}

	public void setNormalizedLongitude(long longitudeNormal, long precision){
		if (longitudeNormal < 0 || longitudeNormal > precision){
			throw new NumberFormatException("Normalized longitude must be greater than 0 and less than the maximum precision");
		}
		_longitude = denormalizeLongitude(longitudeNormal, precision);		
	}



	/***
	 * Normalizes using [A,B) pattern - start inclusive, end exclusive
	 * @param numberBuckets
	 * @return
	 */
	private long normalizeLatitude(double latitude, long precision){
		return (long)(latitude * (precision -1) / 180d);
	}

	private double denormalizeLatitude(long latitude, long precision){
		return latitude * 180d / (precision -1);
	}

	/***
	 * Normalizes using [A,B) pattern - start inclusive, end exclusive
	 * @param numberBuckets
	 * @return
	 */
	private long normalizeLongitude(double longitude, long precision){
		return (long)(longitude * (precision -1) / 360d);
	}

	private double denormalizeLongitude(long longitude, long precision){
		return longitude * 360d / (precision -1);
	}



}
