package com.sinergise.io;

import com.sinergise.geometry.Geometry;
import com.sinergise.geometry.GeometryCollection;
import com.sinergise.geometry.LineString;
import com.sinergise.geometry.MultiLineString;
import com.sinergise.geometry.MultiPoint;
import com.sinergise.geometry.MultiPolygon;
import com.sinergise.geometry.Point;
import com.sinergise.geometry.Polygon;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/**
		 * WKTWRITER TEST
		 */
		WKTWriter wktWriter = new WKTWriter();
		try {
			System.out.println("################-WKTWRITER-#################\n");
			
			//POINT
			System.out.println(wktWriter.write(new Point()));
			System.out.println(wktWriter.write(new Point(15,15)));
			
			System.out.println("-------------------------------------");
			
			//MULTIPOINT
			System.out.println(wktWriter.write(new MultiPoint()));
			System.out.println(wktWriter.write(new MultiPoint(new Point[] {
					new Point(12, 45),
					new Point(33, 24)
			})));
			
			System.out.println("-------------------------------------");
			
			//LINESTRING
			System.out.println(wktWriter.write(new LineString()));
			System.out.println(wktWriter.write(new LineString(new double[]{30, 10, 10, 30, 40, 40})));

			System.out.println("-------------------------------------");
			
			//MULTILINESTRING
			System.out.println(wktWriter.write(new MultiLineString()));
			System.out.println(wktWriter.write(new MultiLineString(new LineString[] {
					new LineString(),
					new LineString(new double[] {
							25, 12, 14, 45, 33, 40
					})
			})));
			
			System.out.println("-------------------------------------");
			
			//POLYGON
			System.out.println(wktWriter.write(new Polygon()));
			
			System.out.println("-------------------------------------");
			
			//MULTIPOLYGON
			System.out.println(wktWriter.write(new MultiPolygon()));
			
			System.out.println("-------------------------------------");
			
			//GEOMETRYCOLLECTION
			System.out.println(wktWriter.write(new GeometryCollection<Geometry>()));
			System.out.println(wktWriter.write(
					new GeometryCollection<Geometry>(new Geometry[]{new Point(4,6), new LineString(new double[] {4,6,7,10})})));
			System.out.println("\n");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/**
		 * WKTREADER TEST
		 */
		WKTReader wktReader = new WKTReader();
		try {
			System.out.println("################-WKTREADER-################\n");
			
			//POINT
			System.out.println(wktReader.read("POINT EMPTY"));
			System.out.println(wktReader.read("POINT (15 15)"));

			System.out.println("-------------------------------------");
			
			//MULTIPOINT
			System.out.println(wktReader.read("MULTIPOINT EMPTY"));
			System.out.println(wktReader.read("MULTIPOINT ((12 45), (33 24))"));

			System.out.println("-------------------------------------");
			
			//LINESTRING
			System.out.println(wktReader.read("LINESTRING EMPTY"));
			System.out.println(wktReader.read("LINESTRING (30 10, 10 30, 40 40)"));

			System.out.println("-------------------------------------");
			
			//MULTILINESTRING
			System.out.println(wktReader.read("MULTILINESTRING EMPTY"));
			System.out.println(wktReader.read("MULTILINESTRING ((25 12), (14 45), (33 40))"));

			System.out.println("-------------------------------------");
			
			//POLYGON
			System.out.println(wktReader.read("POLYGON EMPTY"));
			System.out.println(wktReader.read("POLYGON (30 10, 40 40, 20 40, 10 20, 30 10)"));

			System.out.println("-------------------------------------");
			
			//MULTIPOLYGON
			System.out.println(wktReader.read("MULTIPOLYGON EMPTY"));
			System.out.println(wktReader.read("MULTIPOLYGON (((40 40, 20 45, 45 30, 40 40)),\n" + 
					"((20 35, 10 30, 10 10, 30 5, 45 20, 20 35),(30 20, 20 15, 20 25, 30 20)))"));

			System.out.println("-------------------------------------");
			
			//GEOMETRYCOLLECTION
			System.out.println(wktReader.read("GEOMETRYCOLLECTION EMPTY"));
			System.out.println(wktReader.read("GEOMETRYCOLLECTION (POINT (4 6), LINESTRING (4 6, 7 10))"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
