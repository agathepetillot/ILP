package uk.ac.ed.inf;

import com.mapbox.geojson.Polygon;
import java.awt.geom.Line2D;
import java.util.List;
import static uk.ac.ed.inf.GeoJson.NoFlyZone;
import static uk.ac.ed.inf.GeoJson.line_intersects;
import static uk.ac.ed.inf.GeoJson.point_intersects;

/**
 * Hello world!
 *
 */
public class App

{
    public static void main( String[] args )
    {
        System.out.println( "Hi Agathe!" );
        LongLat pos = new LongLat(-3.1904, 55.9441);
        LongLat nextPos = pos.nextPosition(90);
        GeoJson Zone = new GeoJson();
        System.out.println(point_intersects(pos.longitude, pos.latitude, GeoJson.NoFlyZone));
        System.out.println(point_intersects(nextPos.longitude, nextPos.latitude,GeoJson.NoFlyZone));
        //System.out.println(GeoJson.NoFlyZone);
//            LongLat pos1 = new LongLat(-3.1893, 55.9452);
//            LongLat pos2 = new LongLat(-3.1902, 55.9456);

        

        Line2D[] lines = new Line2D[]{
                new Line2D.Double(-3.1870,55.9452, -3.1902, 55.9456), //true
                new Line2D.Double(-3.1870, 55.9452, -3.1865, 55.9477) //false
        };

        for (Line2D line : lines) {
            //boolean bool1 = line_intersects(line, Zone.NoFlyZone);
            System.out.printf("%b\n", line_intersects(line, GeoJson.NoFlyZone));
            //System.out.printf("%b\n", bool1);
        }
    }
}

