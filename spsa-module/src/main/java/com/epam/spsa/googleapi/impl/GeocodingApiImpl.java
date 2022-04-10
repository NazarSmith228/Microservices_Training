package com.epam.spsa.googleapi.impl;

import com.epam.spsa.googleapi.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class GeocodingApiImpl implements GeocodingApi {

    @Override
    public String getAddress(double latitude, double longitude) throws InterruptedException, ApiException, IOException {
        log.info("Getting Address by latitude: {}, longitude: {}", latitude, longitude);
        GeocodingResult[] directionsResult = com.google.maps.GeocodingApi.newRequest(getContext())
                .latlng(new LatLng(latitude, longitude))
                .await();
        if (directionsResult.length != 0) {
            return directionsResult[0].formattedAddress;
        } else {
            return null;
        }
    }

    @Override
    public LatLng getLatLng(String address) throws InterruptedException, ApiException, IOException {
        log.info("Getting latitude, longitude from address: {}", address);
        GeocodingResult[] directionsResult = com.google.maps.GeocodingApi.newRequest(getContext())
                .address(address)
                .await();
        if (directionsResult.length != 0) {
            return directionsResult[0].geometry.location;
        } else {
            return null;
        }
    }

    @Override
    public String getCity(double latitude, double longitude) throws InterruptedException, ApiException, IOException {
        log.info("Getting Address by latitude: {}, longitude: {}", latitude, longitude);
        GeocodingResult[] directionsResult = com.google.maps.GeocodingApi.newRequest(getContext())
                .latlng(new LatLng(latitude, longitude))
                .await();
        if (directionsResult.length != 0) {
            return directionsResult[0].addressComponents[3].longName;
        } else {
            return null;
        }
    }

}
