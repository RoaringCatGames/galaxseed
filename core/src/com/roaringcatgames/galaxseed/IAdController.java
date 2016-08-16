package com.roaringcatgames.galaxseed;

/**
 * Design an Interface to control Advertisements.
 */
public interface IAdController {

    enum AdPlacement{
        TOP,
        BOTTOM,
        FULLSCREEN
    }

    void showBannerAd(AdPlacement placement);
    void hideBannerAd(AdPlacement placement);
}
