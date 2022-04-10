package com.epam.spsa.googleapi;

import com.google.maps.GeoApiContext;

public interface ApiConnector {

    default GeoApiContext getContext() {
        return new GeoApiContext.Builder()
                .apiKey("AIzaSyAGi7i4hDyO5vwtuGWWh4Qc4NmGuj7MTzM")
                .build();
    }

}
