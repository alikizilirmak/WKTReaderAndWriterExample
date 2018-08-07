package com.sinergise.io;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sinergise.geometry.Geometry;
import com.sinergise.geometry.GeometryCollection;
import com.sinergise.geometry.LineString;
import com.sinergise.geometry.MultiLineString;
import com.sinergise.geometry.MultiPoint;
import com.sinergise.geometry.MultiPolygon;
import com.sinergise.geometry.Point;
import com.sinergise.geometry.Polygon;

public class WKTReader {
	
	/**
	 * Transforms the input WKT-formatted String into Geometry object
	 */
	public Geometry read(String wktString) {
		//TODO: Implement this
		
		//Check if null
		if (wktString == null) {
			return null;
		}
		//POINT
		else if (wktString.startsWith("POINT")) {
			return readPoint(wktString);
		}
		//MULTIPOINT
		else if (wktString.startsWith("MULTIPOINT")) {
			return readMultiPoint(wktString);
		}
		//LINESTRING
		else if (wktString.startsWith("LINESTRING")) {
			return readLineString(wktString);
		}
		//MULTILINESTRING
		else if (wktString.startsWith("MULTILINESTRING")) {
			return readMultiLineString(wktString);
		}
		//POLYGON
		else if (wktString.startsWith("POLYGON")) {
			return readPolygon(wktString);
		}
		//MULTIPOLYGON
		else if (wktString.startsWith("MULTIPOLYGON")) {
			return readMultiPolygon(wktString);
		}
		//GEOMETRYCOLLECTION
		else if (wktString.startsWith("GEOMETRYCOLLECTION")) {
			return readGeometryCollection(wktString);
		}
		
		System.err.println("INVALID GEOMETRY TYPE");
		return null;
	}
	
	/**
	 * Reads WKT for Point object
	 * @param wktString
	 * @return
	 */
	public Point readPoint(String wktString) {
		
		Pattern wktPattern = Pattern.compile("([\\d]+)\\s([\\d]+)");
		Matcher matcher = wktPattern.matcher(wktString);
		
		Point point = null;
		if (matcher.find()) {
			double x = Double.parseDouble(matcher.group(1));
			double y = Double.parseDouble(matcher.group(2));
			point = new Point(x, y);
		} else {
			point = new Point();
		}
		return point;
	}
	
	/**
	 * Reads WKT for MultiPoint object
	 * @param wktString
	 * @return
	 */
	public MultiPoint readMultiPoint(String wktString) {
		
		Pattern wktPattern = Pattern.compile("(\\([\\d]+)\\s([\\d]+\\))|(EMPTY)");
		Matcher matcher = wktPattern.matcher(wktString);
		
		List<Point> points = new ArrayList<Point>();
		while(matcher.find()) {
			points.add(readPoint(matcher.group()));
		}
		if (points.isEmpty()) {
			return new MultiPoint();
		}else {
			Point pointsArr[] = new Point[points.size()];
			points.toArray(pointsArr);
			return new MultiPoint(pointsArr);
		}
		
	}
	
	/**
	 * Reads WKT for LineString object
	 * @param wktString
	 * @return
	 */
	public LineString readLineString(String wktString) {
		
		Pattern wktPattern = Pattern.compile("([\\d]+)\\s([\\d]+)");
		Matcher matcher = wktPattern.matcher(wktString);
		
		List<Double> lsList = new ArrayList<Double>();
		while(matcher.find()) {
			lsList.add(Double.parseDouble(matcher.group(1)));
			lsList.add(Double.parseDouble(matcher.group(2)));
		}
		if (lsList.isEmpty()) {
			return new LineString();
		}else {
			double[] ds = new double[lsList.size()];
			int i = 0;
			for(Double ls : lsList) {
				ds[i++] = ls;
			}
			return new LineString(ds);
		}
	}
	
	/**
	 * Reads WKT for MultiLineString object
	 * @param wktString
	 * @return
	 */
	public MultiLineString readMultiLineString(String wktString) {
		
		Pattern wktPattern = Pattern.compile("(\\([\\d\\s,]+\\))|(EMPTY)");
		Matcher matcher = wktPattern.matcher(wktString);
		
		List<LineString> lineStrings = new ArrayList<LineString>();
		while(matcher.find()) {
			lineStrings.add(readLineString(matcher.group()));
		}
		if (lineStrings.isEmpty()) {
			return new MultiLineString();
		}else {
			LineString[] lineStringArr = new LineString[lineStrings.size()];
			lineStrings.toArray(lineStringArr);
			return new MultiLineString(lineStringArr);
		}
	}
	
	/**
	 * Reads WKT for Polygon object
	 * @param wktString
	 * @return
	 */
	public Polygon readPolygon(String wktString) {
		
		Pattern wktPattern = Pattern.compile("(\\([\\d\\s,]+\\))");
		Matcher matcher = wktPattern.matcher(wktString);
		
		List<LineString> lineStrings = new ArrayList<LineString>();
		while (matcher.find()) {
			lineStrings.add(readLineString(matcher.group()));
		}
		
		if (lineStrings.isEmpty()) {
			return new Polygon();
		}else {
			LineString outer = lineStrings.remove(0);
			LineString[] holes = new LineString[lineStrings.size()];
			lineStrings.toArray(holes);
			return new Polygon(outer, holes);
		}
	}
	
	/**
	 * Reads WKT for MultiPolygon object
	 * @param wktString
	 * @return
	 */
	public MultiPolygon readMultiPolygon(String wktString) {
		
		Pattern wktPattern = Pattern.compile("((\\([\\d\\s,]+\\))(?:,\\s(\\([\\d\\s,]+\\)))*)|(EMPTY)");
		Matcher matcher = wktPattern.matcher(wktString);
		
		List<Polygon> polygons = new ArrayList<Polygon>();
		while(matcher.find()) {
			polygons.add(readPolygon(matcher.group()));
		}
		
		if (polygons.isEmpty()) {
			return new MultiPolygon();
		}
		
		Polygon polygonsArr[] = new Polygon[polygons.size()];
		polygons.toArray(polygonsArr);
		return new MultiPolygon(polygonsArr);
	}
	
	/**
	 * Reads WKT for Geometry Collection objects
	 * @param wktString
	 * @return
	 */
	public GeometryCollection<Geometry> readGeometryCollection(String wktString) {
		
		if (wktString.equals("GEOMETRYCOLLECTION EMPTY")) {
			return new GeometryCollection<Geometry>();
		}
		
		String wktStringExtracted = wktString.replaceFirst("GEOMETRYCOLLECTION \\(", "");
		wktStringExtracted = wktStringExtracted.substring(0, wktStringExtracted.length() - 1);
		String collectionParts[] = wktStringExtracted.split("(,\\s)(?=[PLMG])");
		
		List<Geometry> geometries = new ArrayList<Geometry>();
		for (String part: collectionParts) {
			geometries.add(read(part));
		} 
		
		return new GeometryCollection<Geometry>(geometries);
	}
	

}
