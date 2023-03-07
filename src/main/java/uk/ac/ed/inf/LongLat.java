package uk.ac.ed.inf;

public class LongLat {

    public double longitude;
    public double latitude;
    public static final double MOVE_LENGTH = 0.00015;
    public static final double DIST_TOL = 0.00015;
    public static final double MAX_LONG = -3.184319;
    public static final double MAX_LAT = 55.946233;
    public static final double MIN_LONG = -3.192473;
    public static final double MIN_LAT = 55.942617;

    /**
     * The constructor for this class accepts two double-precision numbers,
     * the first of which is a longitude and the second of which is a latitude. The class has
     * two public double fields named longitude and latitude.
     * @param longitude represents the longitude at which the drone is positioned
     * @param latitude represents the latitude at which the drone is positioned
     */
    public LongLat(double longitude, double latitude) {

        this.longitude = longitude;
        this.latitude = latitude;

    }

    /**
     * The constructor for this class accepts a LongLat object position,
     * This is used to create a copy constructor which will be useful for Coursework 2
     * as it will allow a deep copy of the longitude and latitude (LongLat object)
     * @param position used to change longitude and latitude if necessary whilst keeping
     *                 the original.
     */
    public LongLat(LongLat position)
    {
        this.longitude = position.longitude;
        this.latitude = position.latitude;
    }

    /**
     * isConfined has no parameters and returns true if the point is within the drone
     * confinement area and false if it is not.
     * @return tells us whether or not the drone is within the given confinement area.
     */
    public boolean isConfined()
    {
/*
        All locations which can be visited by the drone have a latitude which lie between 55.942617 and 55.946233.
        They also have a longitude which lie between −3.184319 and −3.192473
*/
        if (longitude > LongLat.MIN_LONG & longitude < LongLat.MAX_LONG & latitude > LongLat.MIN_LAT & latitude < LongLat.MAX_LAT) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * distanceTo takes a LongLat object as a parameter and returns the Pythagorean
     * distance between the two points as a value of type double. The two points being the
     * current object and the object and the one it is going to
     * @param obj1 the point where the drone is in terms of longitude and latitude.
     * @return the distance between the current object and obj1.
     */
    public double distanceTo(LongLat obj1)
    {
        // Calculate euclidean distance between current object and obj1
        return Math.sqrt(Math.pow(this.longitude - obj1.longitude, 2) + Math.pow(this.latitude - obj1.latitude, 2));
    }

    /**
     * closeTo takes a LongLat object as a parameter and returns true if the points are close to each
     * other (in the sense that the distance tolerance is 0.00015 degrees) and false otherwise.
     * @param obj1 point where the drone is in terms of longitude and latitude.
     * @return whether or not this distance is within the distance tolerance.
     */
    public boolean closeTo(LongLat obj1)
    {
        //Finding distance
        double distance = distanceTo(obj1);
        //Checking distance is within the distance tolerance
        if (distance < LongLat.DIST_TOL) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * nextPosition takes an int angle as a parameter and returns a LongLat object which
     * represents the new position of drone if it makes a move in the direction of the angle.
     * When the drone is hovering (this is considered a move), we use the junk value of
     * −999 for the angle, to indicate that the angle does not play a role in determining the
     * next latitude and longitude of the drone.
     * @param angle the angle at which the drone has to move in the direction of.
     * @return the new position of the drone once it has moved in the direction of the angle.
     */
    public LongLat nextPosition(int angle)
    {
        //Initialise longitude and latitude change.
        double longChange = 0;
        double latChange = 0;

        if(angle != -999) {
            //Change the angle from degrees to radians as Math function uses radians.
            double radianAngle = Math.toRadians(angle);
            //Using SohCahToa
            longChange = LongLat.MOVE_LENGTH * Math.cos(radianAngle);
            latChange = LongLat.MOVE_LENGTH * Math.sin(radianAngle);
        }

        return new LongLat(this.longitude + longChange, this.latitude + latChange);

    }
}
