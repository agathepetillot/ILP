package uk.ac.ed.inf;

import com.mapbox.geojson.*;
import com.mapbox.turf.TurfJoins;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class GeoJson {

    private static final HttpClient client = HttpClient.newHttpClient();
    public static HttpResponse<String> response;
    public static List<Polygon> NoFlyZone = new ArrayList<Polygon>();
    public static List<Point> Landmarks = new ArrayList<Point>();
    
    public GeoJson()
    {
        NoFlyZone("localhost", "9898");
        Landmarks("localhost", "9898");
        System.out.println(Landmarks);
    }

     public static void Landmarks(String machine_name, String web_server_port) {

        //Make a request so that we can get a response
        //HttpRequest assumes that it is a GET request by default
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://" + machine_name + ":" + web_server_port + "/buildings/landmarks.geojson")).build();
        //Call the send method on the client created

        try {

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException ex) {

            System.out.println("Cannot retrieve data from server: check server name and query");
        }

        if (response.statusCode() != 200) {
            System.exit(1);
        } else {

            FeatureCollection fc = FeatureCollection.fromJson(response.body());
            List<Feature> f = fc.features();

            for (Feature i : f) {
                Landmarks.add((Point) i.geometry());
            }
        }
    }

    public static void NoFlyZone(String machine_name, String web_server_port) {

        //Make a request so that we can get a response
        //HttpRequest assumes that it is a GET request by default
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://" + machine_name + ":" + web_server_port + "/buildings/no-fly-zones.geojson")).build();
        //Call the send method on the client created

        try {

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
           
        } catch (IOException | InterruptedException ex) {

            System.out.println("Cannot retrieve data from server: check server name and query");
        }

        if (response.statusCode() != 200) {
            System.exit(1);
        } else {

            FeatureCollection fc = FeatureCollection.fromJson(response.body());
            List<Feature> f = fc.features();

            for (Feature i : f) {
                NoFlyZone.add((Polygon) i.geometry());
            }
        }
    }

    public static boolean point_intersects(Double endPosLng, Double endPosLat, List<Polygon> polygons) {
        boolean intersects = false;
        Point end = Point.fromLngLat(endPosLng, endPosLat);
        for (Polygon poly : polygons) {
            if (TurfJoins.inside(end, poly)) {
                intersects = true;
            }
        }
        return intersects;
    }

    public static boolean line_intersects(Line2D line, List<Polygon> polygons) {

        boolean intersects = false;
        for (Polygon poly : polygons) 
        {
            List<List<Point>> coords = poly.coordinates();
            for (int i = 0; i < coords.size(); i++) // We are analysing the polygons one at a time
            {
                for (int j = 0; j < coords.get(i).size()-1; j++)
                {
                    Line2D edge = new Line2D.Double(coords.get(i).get(j).coordinates().get(0), coords.get(i).get(j).coordinates().get(1), coords.get(i).get(j+1).coordinates().get(0), coords.get(i).get(j+1).coordinates().get(1));
                    
                    if (line.intersectsLine(edge)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void GoToTarget(LongLat startPoint, LongLat endPoint) {

        Line2D line = new Line2D.Double(startPoint.longitude,startPoint.latitude,endPoint.longitude,endPoint.latitude);
        
//        if (line_intersects(line, GeoJson.NoFlyZone))
//        {
//            FindNearestWaypoint();
//            if(line_intersects())
//            
//        }
//        
//          go to closest way point (check for intersection again)(use line_intersects())
//            GoToNextPosition(new positions);
//        }
//        else{
////          go to the end point using 10 degree increments and checking whether or not these 10 degree increments intersect at any point
//            with a polygon.
//        }
//
//
//        double distanceTo = start_LongLat.distanceTo(end_LongLat);
//        double adjacent = endPoint.get(0) - startPoint.get(0);
//        double hypotenuse = distanceTo;
//
//        while (distanceTo > LongLat.DIST_TOL) {
//
//            double angle = Math.acos(adjacent / hypotenuse);
//
//            start_LongLat.nextPosition((int) ((angle / 10) * 10));
//
//        }
    }


        public static void main (String[]args){

            LongLat pos = new LongLat(-3.1904, 55.9441);
            LongLat nextPos = pos.nextPosition(90);
            System.out.println(point_intersects(pos.longitude, pos.latitude, GeoJson.NoFlyZone));
            System.out.println(point_intersects(nextPos.longitude, nextPos.latitude, GeoJson.NoFlyZone));

//            LongLat pos1 = new LongLat(-3.1893, 55.9452);
//            LongLat pos2 = new LongLat(-3.1902, 55.9456);
            

            Line2D[] lines = new Line2D[]{
                new Line2D.Double(-3.1893,55.9452, -3.1902, 55.9456), //true
                new Line2D.Double(-3.1893, 55.9452, -3.1870, 55.9457) //false
            };

            for (Line2D line : lines) {
                System.out.printf("%s: %b\n", line, line_intersects(line, NoFlyZone));
            }

        }
}

//    // create ring: P1(0,0) - P2(0,10) - P3(10,10) - P4(0,10)
//    LinearRing lr = new GeometryFactory().createLinearRing(new Coordinate[]{new Coordinate(0,0), new Coordinate(0,10), new Coordinate(10,10), new Coordinate(10,0), new Coordinate(0,0)});
//    // create line: P5(5, -1) - P6(5, 11) -> crossing the ring vertically in the middle
//    LineString ls = new GeometryFactory().createLineString(new Coordinate[]{new Coordinate(5,-1), new Coordinate(5,11)});
//    // calculate intersection points
//    Geometry intersectionPoints = lr.intersection(ls);

//https://gis.stackexchange.com/questions/290438/turfjs-intersect-line-and-polygon - line to line intersection
