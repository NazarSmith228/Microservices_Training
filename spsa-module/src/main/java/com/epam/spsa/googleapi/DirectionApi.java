package com.epam.spsa.googleapi;


public interface DirectionApi extends ApiConnector {

    double getDistance(double latitudeStart, double longitudeStart,
                       double latitudeEnd, double longitudeEnd) throws Exception;

    String getDuration(double latitudeStart, double longitudeStart,
                       double latitudeEnd, double longitudeEnd) throws Exception;

}
