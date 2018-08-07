package com.sinergise.io;

import com.sinergise.geometry.Geometry;
import com.sinergise.geometry.GeometryCollection;
import com.sinergise.geometry.LineString;
import com.sinergise.geometry.MultiLineString;
import com.sinergise.geometry.MultiPoint;
import com.sinergise.geometry.MultiPolygon;
import com.sinergise.geometry.Point;
import com.sinergise.geometry.Polygon;

@SuppressWarnings("unchecked")
public class WKTWriter {
	
	/**
	 * Transforms the input Geometry object into WKT-formatted String. e.g.
	 * <pre><code>
	 * new WKTWriter().write(new LineString(new double[]{30, 10, 10, 30, 40, 40}));
	 * //returns "LINESTRING (30 10, 10 30, 40 40)"
	 * </code></pre>
	 */
	public String write(Geometry geom) {
		//TODO: Implement this
		
		String wktString = "";
		
		//Check if null
		if (geom == null) {
			
			return "INVALID GEOMETRIC OBJECT";
		}
		//POINT
		else if (geom instanceof Point) {
			
			Point p = (Point) geom;
			
			if (p.isEmpty()) {
				wktString += "POINT EMPTY";
			}else {
				wktString += "POINT (" + p.getX() + " " + p.getY() + ")";
			}
		}
		//MULTIPOINT
		else if (geom instanceof MultiPoint) {
			
			MultiPoint mp = (MultiPoint) geom;
			
			if (mp.isEmpty()) {
				wktString += "MULTIPOINT EMPTY";
			}else {
				wktString += "MULTIPOINT (";
				for (int i = 0; i < mp.size() - 1; i++) {
					wktString += "POINT(" + mp.get(i).getX() + " " + mp.get(i).getY() + "), ";
				}
				wktString += "POINT(" + mp.get(mp.size() - 1).getX() + " " + mp.get(mp.size() - 1).getY() + "))";
			}
		}
		//LINESTRING
		else if (geom instanceof LineString) {
			return writeLineString(geom);
		}
		//MULTILINESTRING
		else if (geom instanceof MultiLineString) {
			
			MultiLineString mls = (MultiLineString) geom;
			
			if (mls.isEmpty()) {
				wktString += "MULTILINESTRING EMPTY";
			}else {
				wktString += "MULTILINESTRING (";
				for(int i = 0; i < mls.size(); i++) {
					for (int j = 0; j < mls.get(i).getNumCoords() - 1; j++) {
						wktString += "LINESTRING (" + Double.toString(mls.get(i).getX(j)) + " " + Double.toString(mls.get(i).getY(j)) + "), ";
					}
				}
				wktString += "LINESTRING (" + mls.get(mls.size() - 1).getX(mls.size()) + " " + mls.get(mls.size() - 1).getY(mls.size()) + "))";
			}
		}
		//POLYGON
		else if (geom instanceof Polygon) {
			return writePolygon(geom);
		}
		//MULTIPOLYGON
		else if (geom instanceof MultiPolygon) {
			
			MultiPolygon mp = (MultiPolygon) geom;
			int count = mp.size();
			
			if (count == 0) {
				wktString += "MULTIPOLYGON EMPTY";
			}else {
				wktString += "MULTIPOLYGON (";
				
				String[] polygons = new String[count];
				for(int i = 0; i < count; i++) {
					polygons[i] = writePolygon(mp.get(i));
				}
				
				wktString += ")";
			}
		}
		//GEOMETRYCOLLECTION
		else if (geom instanceof GeometryCollection<?>) {
			
			GeometryCollection<Geometry> gc = (GeometryCollection<Geometry>) geom;
			int count = gc.size();
			
			if (geom.isEmpty()) {
				wktString += "GEOMETRYCOLLECTION EMPTY";
			}else {
				wktString += "GEOMETRYCOLLECTION (";
				Geometry gObject;
				for(int i = 0; i < count; i++) {
					gObject = gc.get(i);
					wktString += write(gObject);
				}
				wktString += ")";
			}
		}
		else {
			wktString += "WRONG GEOMETRIC OBJECT FORMAT";
		}
		
		
		return wktString;
		
	}
	
	/**
	 * I want to split LineString from write method because 
	 * I cannot find a suitable solution polygon and multi-polygon
	 * @param geometry
	 * @return
	 */
	public String writeLineString(Geometry geometry) {
		
		LineString ls = (LineString) geometry;
		String wktString = "";
		
		int level = ls.getNumCoords();
		if (level != 0) {
			wktString = "LINESTRING (";
			for (int i = 0; i < level - 1; i++) {
				wktString += ls.getX(i) + " " + ls.getY(i) + ", ";
			}
			wktString += ls.getX(level - 1) + " " + ls.getY(level - 1) + ")";
		}else {
			wktString += "LINESTRING EMPTY";
		}
		return wktString;
	}
	
	/**
	 * I want to split Polygon from write method because 
	 * I cannot find a suitable solution polygon and multi-polygon
	 * @param geometry
	 * @return
	 */
	public String writePolygon(Geometry geometry) {
		
		Polygon pol = (Polygon) geometry;
		String wktString = "";
		int numOfHoles = pol.getNumHoles();
		
		if (pol.isEmpty()) {
			wktString += "POLYGON EMPTY";
		}else {
			wktString += "POLYGON (";
			
			StringBuilder strPolygon = new StringBuilder();
			strPolygon.append(writeLineString(pol.getOuter()));
			String[] holes = new String[numOfHoles];
			
			for(int i = 0; i < holes.length; i++) {
				holes[i] = writeLineString(pol.getHole(i));
				wktString += "(" + holes[i] + ")";
			}
			
			wktString += ")";
		}
		return wktString;
	}
	
}
