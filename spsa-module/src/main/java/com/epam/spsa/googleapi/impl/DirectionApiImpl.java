package com.epam.spsa.googleapi.impl;

import com.epam.spsa.googleapi.DirectionApi;
import com.google.maps.DirectionsApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class DirectionApiImpl implements DirectionApi {

    private DirectionsResult connectToDirectionApi(double latitudeStart, double longitudeStart,
                                                   double latitudeEnd, double longitudeEnd)
            throws InterruptedException, ApiException, IOException {
        log.info("Getting JsonObject for latitudeStart: {}, longitudeStart: {}, ",
                latitudeStart, longitudeStart);
        log.info("\tlatitudeEnd: {}, longitudeEnd: {}", latitudeEnd, longitudeEnd);
        return DirectionsApi.newRequest(getContext())
                .origin(new LatLng(latitudeStart, longitudeStart))
                .destination(new LatLng(latitudeEnd, longitudeEnd))
                .await();

    }

    public String getDuration(double latitudeStart, double longitudeStart,
                              double latitudeEnd, double longitudeEnd)
            throws InterruptedException, ApiException, IOException {
        log.info("Getting duration for latitudeStart: {}, longitudeStart: {}, ",
                latitudeStart, longitudeStart);
        log.info("\tlatitudeEnd: {}, longitudeEnd: {}", latitudeEnd, longitudeEnd);
        DirectionsResult location = connectToDirectionApi(latitudeStart, longitudeStart,
                latitudeEnd, longitudeEnd);

        return location.routes[0].legs[0].duration.humanReadable;
    }

    public double getDistance(double latitudeStart, double longitudeStart,
                              double latitudeEnd, double longitudeEnd)
            throws InterruptedException, ApiException, IOException {
        log.info("Getting distance between two users. latitudeStart: {}, longitudeStart: {}, ",
                latitudeStart, longitudeStart);
        log.info("\tlatitudeEnd: {}, longitudeEnd: {}", latitudeEnd, longitudeEnd);
        DirectionsResult location = connectToDirectionApi(latitudeStart, longitudeStart,
                latitudeEnd, longitudeEnd);

        return location.routes[0].legs[0].distance.inMeters;
    }

}
