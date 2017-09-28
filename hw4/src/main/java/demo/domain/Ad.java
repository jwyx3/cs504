package demo.domain;

import lombok.Data;

import java.util.List;

@Data
public class Ad {
    private Long adId;
    private Long campaignId;
    private List<String> keywords;
    private double relevanceScore;
    private double pClick;
    private double bidPrice;
    private double rankScore;
    private double qualityScore;
    private double costPerClick;
    private int position; //1: top , 2: bottom
    private String title; // required
    private double price; // required
    private String thumbnail; // required
    private String description; // required
    private String brand; // required
    private String detailUrl; // required
    private String query; //required
    private String category;
}
