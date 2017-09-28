package demo.engine.index;

import demo.domain.Ad;
import demo.domain.Campaign;
import demo.util.MySQLAccess;
import demo.util.Utility;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IndexBuilder {
    private int EXP = 0; //0: never expire
    private String mMemcachedServer;
    private int mMemcachedPortal;
    private String mysql_host;
    private String mysql_db;
    private String mysql_user;
    private String mysql_pass;
    private MySQLAccess mysql;
    private MemcachedClient cache;

    public void close() {
        if (mysql != null) {
            try {
                mysql.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public IndexBuilder(String memcachedServer, int memcachedPortal, String mysqlHost, String mysqlDb, String user, String pass) {
        mMemcachedServer = memcachedServer;
        mMemcachedPortal = memcachedPortal;
        mysql_host = mysqlHost;
        mysql_db = mysqlDb;
        mysql_user = user;
        mysql_pass = pass;
        mysql = new MySQLAccess(mysql_host, mysql_db, mysql_user, mysql_pass);
        String address = mMemcachedServer + ":" + mMemcachedPortal;
        try {
            cache = new MemcachedClient(new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(), AddrUtil.getAddresses(address));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean buildInvertIndex(Ad ad) {
        String keyWords = Utility.strJoin(ad.getKeywords(), ",");
        List<String> tokens = Utility.cleanedTokenize(keyWords);
        for (int i = 0; i < tokens.size(); i++) {
            String key = tokens.get(i);
            if (cache.get(key) instanceof Set) {
                @SuppressWarnings("unchecked")
                Set<Long> adIdList = (Set<Long>) cache.get(key);
                adIdList.add(ad.getAdId());
                cache.set(key, EXP, adIdList);
            } else {
                Set<Long> adIdList = new HashSet<>();
                adIdList.add(ad.getAdId());
                cache.set(key, EXP, adIdList);
            }
        }
        return true;
    }

    public Boolean buildForwardIndex(Ad ad) {
        try {
            mysql.addAdData(ad);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean updateBudget(Campaign camp) {
        try {
            mysql.addCampaignData(camp);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
