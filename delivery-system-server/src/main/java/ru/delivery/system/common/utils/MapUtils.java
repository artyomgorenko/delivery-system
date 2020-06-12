package ru.delivery.system.common.utils;

import ru.delivery.system.model.other.GeoPoint;

public class MapUtils {
    public static final double DEG2RAD = Math.PI / 180.0;
    public static final int RADIUS_EARTH_METERS = 6378137; // http://en.wikipedia.org/wiki/Earth_radius#Equatorial_radius

    /**
     * Distance between two points in meters
     */
    public static double distanceAsDouble(final GeoPoint point1, final GeoPoint point2) {
        final double lat1 = DEG2RAD * point1.getLatitude();
        final double lat2 = DEG2RAD * point2.getLatitude();
        final double lon1 = DEG2RAD * point1.getLongitude();
        final double lon2 = DEG2RAD * point2.getLongitude();
        return RADIUS_EARTH_METERS * 2 * Math.asin(Math.min(1, Math.sqrt(
                Math.pow(Math.sin((lat2 - lat1) / 2), 2)
                        + Math.cos(lat1) * Math.cos(lat2)
                        * Math.pow(Math.sin((lon2 - lon1) / 2), 2)
        )));
    }
}
