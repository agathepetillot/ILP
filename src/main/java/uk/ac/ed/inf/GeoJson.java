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

public class GeoJson {

    private static final HttpClient client = HttpClient.newHttpClient();
    public static HttpResponse<String> response;
    public static List<Polygon> g = new ArrayList<Polygon>();


    public static List<Polygon> NoFlyZone(String machine_name, String web_server_port) {

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
                g.add((Polygon) i.geometry());
            }
        }
        return (g);
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

    public static boolean line_intersects(List<Polygon> polygons) {

//        boolean intersects = false;
        for (Polygon poly : polygons) {
            System.out.println(poly.toJson());
        }//{
//                intersects = true;
//            }
//        for (int i = 0; i < poly.npoints; i++) {
//            int nextI = (i + 1) % poly.npoints;
//            Line2D edge = new Line2D.Double(poly.xpoints[i], poly.ypoints[i], poly.xpoints[nextI], poly.ypoints[nextI]);
//            if (line.intersectsLine(edge)) {
//                intersects = true;
//            }
//        }
        return false;
    }

//    public static void GoToNextPosition(ArrayList<Double> startPoint, ArrayList<Double> endPoint, double increment) {
//
//
//        LongLat start_LongLat = new LongLat(startPoint.get(0), startPoint.get(1));
//        LongLat end_LongLat = new LongLat(endPoint.get(0), endPoint.get(1));
//
////        if line_intersects(start_LongLat, end_LongLat, LongLat.MOVE_LENGTH){
////          go to closest way point (check for intersection again)(use line_intersects())
////            GoToNextPosition(new positions);
////        }
////        else{
//////          go to the end point using 10 degree increments and checking whether or not these 10 degree increments intersect at any point
////            with a polygon.
////        }
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
//    }


        public static void main (String[]args){

            LongLat pos = new LongLat(-3.1904, 55.9441);
            LongLat nextPos = pos.nextPosition(90);
            System.out.println(point_intersects(pos.longitude, pos.latitude, NoFlyZone("localhost", "9898")));
            System.out.println(point_intersects(nextPos.longitude, nextPos.latitude, NoFlyZone("localhost", "9898")));

//            LongLat pos1 = new LongLat(-3.1893, 55.9452);
//            LongLat pos2 = new LongLat(-3.1902, 55.9456);

            List<Polygon> poly = (GeoJson.NoFlyZone("localhost", "9898"));

            line_intersects(poly);

//            Line2D[] lines = new Line2D[]{
//                    new Line2D.Double(55.9452, -3.1893, 55.9456, -3.1902), //true
//                    new Line2D.Double(55.9452, -3.1893, 55.9457, -3.1870) //false
//            };
//
//            for (Line2D line : lines) {
//                System.out.printf("%s: %b\n", line, line_intersects(line, (java.awt.Polygon) poly));
//            }

        }
}

//    // create ring: P1(0,0) - P2(0,10) - P3(10,10) - P4(0,10)
//    LinearRing lr = new GeometryFactory().createLinearRing(new Coordinate[]{new Coordinate(0,0), new Coordinate(0,10), new Coordinate(10,10), new Coordinate(10,0), new Coordinate(0,0)});
//    // create line: P5(5, -1) - P6(5, 11) -> crossing the ring vertically in the middle
//    LineString ls = new GeometryFactory().createLineString(new Coordinate[]{new Coordinate(5,-1), new Coordinate(5,11)});
//    // calculate intersection points
//    Geometry intersectionPoints = lr.intersection(ls);

//https://gis.stackexchange.com/questions/290438/turfjs-intersect-line-and-polygon - line to line intersection
