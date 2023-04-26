package app;



public class Coordinates {
    private final double longitude;
    private final double latitude;

    private final double r = Constants.GLOBE_SCALE;

    public Coordinates(double lat, double lon) {
        this.longitude = lon;
        this.latitude = lat;
    }
        //Kugelkoordinaten parametrisierung
    /*
            r * cos(Breitengrad-winkel) * cos(Längengrad-winkel)
            r * cos(Breitengrad-winkel) * sin(Längengrad-winkel)
            r * sin(Breitengrad-winkel)
     */


    //Bogenmaß
    //Latitude --> Breitengrad , Longitude --> Längengrad

    public Vector toCartesian() {
        return new Vector(
                this.r * Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(this.longitude)),
                this.r * Math.cos(Math.toRadians(this.latitude)) * Math.sin(Math.toRadians(this.longitude)),
                this.r * Math.sin(Math.toRadians(this.latitude))
        );
    }
}
