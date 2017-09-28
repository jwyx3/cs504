package demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import demo.util.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class FakeCrawler {
    private final static String QUEUE_NAME = "q_product";

    public void start() {
        Properties props = Config.getInstance().getProps();
        ClassLoader classLoader = getClass().getClassLoader();

        String mAdsDataFilePath = props.getProperty("adsDataFilePath");
        mAdsDataFilePath = new File(classLoader.getResource(mAdsDataFilePath).getFile()).getAbsolutePath();

        //load ads data
        try (BufferedReader brAd = new BufferedReader(new FileReader(mAdsDataFilePath))) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            String line;
            while ((line = brAd.readLine()) != null) {
                System.out.println(" [x] Sent '" + line + "'");
                channel.basicPublish("", QUEUE_NAME, null, line.getBytes("UTF-8"));
            }

            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FakeCrawler crawler = new FakeCrawler();
        crawler.start();
    }
}
