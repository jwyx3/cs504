package demo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static Config instance = null;
    private final Properties props;

    public Config() {
        String resourceName = "application.properties"; // could also be a constant
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(resourceName);
        props = new Properties();
        try {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public Properties getProps() {
        return props;
    }

    public static void main(String[] args) {
        Config config = Config.getInstance();
        System.out.println(config.getProps().toString());
    }
}
