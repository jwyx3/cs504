package demo;

import com.rabbitmq.client.*;
import demo.domain.Ad;
import demo.domain.Campaign;
import demo.engine.index.IndexBuilder;
import demo.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

@Slf4j
public class IndexServer {
    private final IndexBuilder indexBuilder;
    private final static String OUT_QUEUE_NAME = "q_product";

    public IndexServer() {
        // read configuration
        Properties props = Config.getInstance().getProps();
        ClassLoader classLoader = getClass().getClassLoader();
        String mBudgetFilePath = props.getProperty("budgetDataFilePath");
        mBudgetFilePath = new File(classLoader.getResource(mBudgetFilePath).getFile()).getAbsolutePath();
        String memcachedServer = props.getProperty("memcachedServer");
        String mysqlHost = props.getProperty("mysqlHost");
        String mysqlDb = props.getProperty("mysqlDB");
        String mysqlUser = props.getProperty("mysqlUser");
        String mysqlPass = props.getProperty("mysqlPass");
        int memcachedPortal = Integer.parseInt(props.getProperty("memcachedPortal"));

        // create IndexBuilder
        indexBuilder = new IndexBuilder(memcachedServer, memcachedPortal, mysqlHost, mysqlDb, mysqlUser, mysqlPass);

        // load budget data
        try (BufferedReader brBudget = new BufferedReader(new FileReader(mBudgetFilePath))) {
            String line;
            while ((line = brBudget.readLine()) != null) {
                JSONObject campaignJson = new JSONObject(line);
                Long campaignId = campaignJson.getLong("campaignId");
                double budget = campaignJson.getDouble("budget");
                Campaign camp = new Campaign();
                camp.setCampaignId(campaignId);
                camp.setBudget(budget);
                if (!indexBuilder.updateBudget(camp)) {
                    log.error("Failed to update campaign: {}", camp.getCampaignId());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // create one fake crawler and read ad file and publish into rabbitmq
    // IndexServer will read it from rabbitmq and create Inverted index
    public void start() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        Connection connection = factory.newConnection();
        Channel inChannel = connection.createChannel();
        inChannel.basicQos(10); // Per consumer limit
        inChannel.queueDeclare(OUT_QUEUE_NAME, true, false, false, null);

        Consumer consumer = new DefaultConsumer(inChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                log.info(" [x] Received '" + envelope.getRoutingKey() + ":" + message + "'");

                JSONObject adJson = new JSONObject(message);
                Ad ad = new Ad();
                if (adJson.isNull("adId") || adJson.isNull("campaignId")) {
                    return;
                }
                ad.setAdId(adJson.getLong("adId"));
                ad.setCampaignId(adJson.getLong("campaignId"));
                ad.setBrand(adJson.isNull("brand") ? "" : adJson.getString("brand"));
                ad.setPrice(adJson.isNull("price") ? 100.0 : adJson.getDouble("price"));
                ad.setThumbnail(adJson.isNull("thumbnail") ? "" : adJson.getString("thumbnail"));
                ad.setTitle(adJson.isNull("title") ? "" : adJson.getString("title"));
                ad.setDetailUrl(adJson.isNull("detail_url") ? "" : adJson.getString("detail_url"));
                ad.setBidPrice(adJson.isNull("bidPrice") ? 1.0 : adJson.getDouble("bidPrice"));
                ad.setPClick(adJson.isNull("pClick") ? 0.0 : adJson.getDouble("pClick"));
                ad.setCategory(adJson.isNull("category") ? "" : adJson.getString("category"));
                ad.setDescription(adJson.isNull("description") ? "" : adJson.getString("description"));
                ad.setKeywords(new ArrayList<String>());
                JSONArray keyWords = adJson.isNull("keyWords") ? null : adJson.getJSONArray("keyWords");
                for (int j = 0; j < keyWords.length(); j++) {
                    ad.getKeywords().add(keyWords.getString(j));
                }
                if (!indexBuilder.buildInvertIndex(ad) || !indexBuilder.buildForwardIndex(ad)) {
                    log.error("Failed to build index for ad: {}", ad.getAdId());
                }
            }
        };

        inChannel.basicConsume(OUT_QUEUE_NAME, true, consumer);
    }

    public static void main(String[] args) throws Exception {
        IndexServer server = new IndexServer();
        server.start();
    }
}
