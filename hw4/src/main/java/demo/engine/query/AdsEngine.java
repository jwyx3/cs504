package demo.engine.query;

import demo.domain.Ad;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdsEngine {
    private String mMemcachedServer;
    private int mMemcachedPortal;
    private String mysql_host;
    private String mysql_db;
    private String mysql_user;
    private String mysql_pass;

    public AdsEngine(String memcachedServer, int memcachedPortal, String mysqlHost, String mysqlDb, String user, String pass) {
        mMemcachedServer = memcachedServer;
        mMemcachedPortal = memcachedPortal;
        mysql_host = mysqlHost;
        mysql_db = mysqlDb;
        mysql_user = user;
        mysql_pass = pass;
    }

    public List<Ad> selectAds(String query) {
        //query understanding
        List<String> queryTerms = QueryParser.getInstance().QueryUnderstand(query);
        //select ads candidates
        List<Ad> adsCandidates = AdsSelector.getInstance(mMemcachedServer, mMemcachedPortal, mysql_host, mysql_db, mysql_user, mysql_pass).selectAds(queryTerms);
        //L0 filter by pClick, relevance score
        List<Ad> L0unfilteredAds = AdsFilter.getInstance().LevelZeroFilterAds(adsCandidates);
        System.out.println("L0unfilteredAds ads left = " + L0unfilteredAds.size());

        //sort by relevance sore
        Collections.sort(L0unfilteredAds, new Comparator<Ad>() {
            @Override
            public int compare(Ad o1, Ad o2) {
                int result = 1;
                if (o1.getRelevanceScore() > o2.getRelevanceScore()) {
                    result = -1;
                }
                return result;
            }
        });

        //L1 filter by relevance score : select top K ads
        int k = 20;
        List<Ad> unfilteredAds = AdsFilter.getInstance().LevelOneFilterAds(L0unfilteredAds, k);
        System.out.println("unfilteredAds ads left = " + unfilteredAds.size());

        //Dedupe ads per campaign
        List<Ad> dedupedAds = AdsCampaignManager.getInstance(mysql_host, mysql_db, mysql_user, mysql_pass).DedupeByCampaignId(unfilteredAds);
        System.out.println("dedupedAds ads left = " + dedupedAds.size());


        List<Ad> ads = AdsCampaignManager.getInstance(mysql_host, mysql_db, mysql_user, mysql_pass).ApplyBudget(dedupedAds);
        System.out.println("AdsCampaignManager ads left = " + ads.size());

        //allocation
        AdsAllocation.getInstance().AllocateAds(ads);
        return ads;
    }
}
