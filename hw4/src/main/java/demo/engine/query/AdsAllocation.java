package demo.engine.query;

import demo.domain.Ad;

import java.util.List;

public class AdsAllocation {

    private static AdsAllocation instance = null;
    private static double mainLinePriceThreshold = 4.5;

    public static AdsAllocation getInstance() {
        if (instance == null) {
            instance = new AdsAllocation();
        }
        return instance;
    }

    public void AllocateAds(List<Ad> ads) {
        for (Ad ad : ads) {
            if (ad.getCostPerClick() >= mainLinePriceThreshold) {
                ad.setPosition(1);
            } else {
                ad.setPosition(2);
            }
        }
    }
}
