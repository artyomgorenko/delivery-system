package ru.delivery.system.common;

import ru.delivery.system.model.other.GeoPoint;

import java.util.List;

import static java.lang.Math.*;

public class GeoUtils {

    private static final int AVERAGE_EARTH_RADIUS = 6371;


    public static double calacRouteDistance(List<GeoPoint> geoPoints) {
        double distance = 0F;

        GeoPoint prevGeoPoint = null;
        for (GeoPoint geoPoint : geoPoints) {
            if (prevGeoPoint != null) {
                distance += calcDistanceBetweenCoords(prevGeoPoint, geoPoint);
            }
            prevGeoPoint = geoPoint;
        }
        return distance;
    }

    /**
     * Calculates distance between tow points on Earth by formula:
     * distance = arccos {sin(Фa)·sin(Фb) + cos(Фa)·cos(Фb)·cos(Лa - Лb)} * R
     *
     * @return distance between two geoPoints in kilometers
     */
    public static double calcDistanceBetweenCoords(GeoPoint geoPoint1, GeoPoint geoPoint2) {
        double distance = acos(sin(geoPoint1.getLatitude()) * sin(geoPoint2.getLatitude()) +
                cos(geoPoint1.getLatitude()) * cos(geoPoint2.getLatitude()) * cos(geoPoint1.getLongitude() - geoPoint2.getLongitude()));
        distance *= AVERAGE_EARTH_RADIUS;
        return distance;
    }
}
