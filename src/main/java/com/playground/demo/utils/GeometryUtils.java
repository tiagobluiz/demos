package com.playground.demo.utils;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtils {
    public static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

}
