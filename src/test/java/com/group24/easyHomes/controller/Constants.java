package com.group24.easyHomes.controller;

import com.group24.easyHomes.model.Property;
import com.group24.easyHomes.model.PropertyAddress;
import com.group24.easyHomes.model.PropertyListQuery;
import com.group24.easyHomes.model.Services;

public class Constants {

    public static final int propertyID = 10;
    public static final int noOfBedrooms_1 = 1;
    public static final int noOfBathrooms_1 = 1;
    public static final double propertyRent = 500.0;
    public static final int propertyIDDoesNotExist= 3000;
    public static final long serviceIDDoesNotExist= 3000L;
    public static final double paymentAmount = 500.0;
    public static final int userId = 1;
    public static final int paidForServiceID = 1;
    public static final Long serviceID = 1L;
    public static final int favoritePropertyID = 1;
    public static final Long favoritePropertyPrimaryID = 1L;
    public static final int favoriteServiceID = 1;
    public static final Long favoriteServicePrimaryID = 1L;
    public static final Long markedAsFavoriteByUserID = 1L;
    public static final long reviewForServiceID = 16L;
    public static final long reviewGivenByUserID = 1L;
    public static final int reviewRating = 4;
    public static final int reviewIDToBeDeleted = 3000;



    public static final PropertyAddress address = new PropertyAddress("University Street",
            "Halifax",
            "Canada",
            "H2Y8IK", "NS");

    public static final Property property = new Property("test", address, "Wifi", "House", true, 500.0, 2, 2, 1);

    public static final Services services = new Services(
            "Tiffin",
            "Food Delivery",
            199,
            "monthly",
            "Test description",
            "Halifax",
            "NS",
            "Canada",
            "H2Y8IK",
            "1234",
            2L
    );
}
