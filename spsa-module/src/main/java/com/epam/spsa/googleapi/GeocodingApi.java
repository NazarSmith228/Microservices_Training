package com.epam.spsa.googleapi;

import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;

import java.io.IOException;

public interface GeocodingApi extends ApiConnector {

    String getAddress(double latitude, double longitude) throws InterruptedException, ApiException, IOException;

    LatLng getLatLng(String address) throws InterruptedException, ApiException, IOException;

    String getCity(double latitude, double longitude) throws InterruptedException, ApiException, IOException;

}
