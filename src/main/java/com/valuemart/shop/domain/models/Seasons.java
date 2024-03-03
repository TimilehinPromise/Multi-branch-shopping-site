package com.valuemart.shop.domain.models;

import java.time.LocalDate;
import java.time.Month;
public enum Seasons {
    RAINY, DRY, HARMATTAN,NONE;

    public static Seasons getCurrentSeason() {
        Month currentMonth = LocalDate.now().getMonth();
        switch (currentMonth) {

            case JULY:
            case AUGUST:
            case SEPTEMBER:
            case OCTOBER:
                return RAINY;
            case NOVEMBER:
            case DECEMBER:
            case JANUARY:
            case FEBRUARY:
                return HARMATTAN;
            case MARCH:
            case APRIL:
            case MAY:
            case JUNE:
                return DRY;
            default:
                // This default case may not be necessary given all months are covered
                return NONE;
        }
    }

    }