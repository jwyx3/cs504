package demo;

import demo.domain.Ad;
import demo.engine.query.AdsEngine;
import demo.util.Config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

@WebServlet("/SearchAds")
public class AdsServer extends HttpServlet {
    private ServletConfig config = null;
    private AdsEngine adsEngine = null;
    private String uiTemplate = "";
    private String adTemplate = "";

    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        super.init(config);

        ServletContext application = config.getServletContext();
        Properties props = Config.getInstance().getProps();

        String uiTemplateFilePath = props.getProperty("uiTemplateFilePath");
        uiTemplateFilePath = application.getRealPath(uiTemplateFilePath);
        String adTemplateFilePath = props.getProperty("adTemplateFilePath");
        adTemplateFilePath = application.getRealPath(adTemplateFilePath);

        String memcachedServer = props.getProperty("memcachedServer");
        String mysqlHost = props.getProperty("mysqlHost");
        String mysqlDb = props.getProperty("mysqlDB");
        String mysqlUser = props.getProperty("mysqlUser");
        String mysqlPass = props.getProperty("mysqlPass");
        int memcachedPortal = Integer.parseInt(props.getProperty("memcachedPortal"));

        this.adsEngine = new AdsEngine(memcachedServer, memcachedPortal, mysqlHost, mysqlDb, mysqlUser, mysqlPass);
        System.out.println("adsEngine initialized");

        //load UI template
        try {
            byte[] uiData;
            byte[] adData;
            uiData = Files.readAllBytes(Paths.get(uiTemplateFilePath));
            uiTemplate = new String(uiData, StandardCharsets.UTF_8);
            adData = Files.readAllBytes(Paths.get(adTemplateFilePath));
            adTemplate = new String(adData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("UI template initialized");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("q");
        List<Ad> adsCandidates = adsEngine.selectAds(query);
        String result = uiTemplate;
        String list = "";
        for (Ad ad : adsCandidates) {
            System.out.println("final selected ad id = " + ad.getAdId());
            System.out.println("final selected ad rank score = " + ad.getRankScore());
            String adContent = adTemplate;
            adContent = adContent.replace("$title$", ad.getTitle());
            adContent = adContent.replace("$brand$", ad.getBrand());
            adContent = adContent.replace("$img$", ad.getThumbnail());
            adContent = adContent.replace("$link$", ad.getDetailUrl());
            adContent = adContent.replace("$price$", Double.toString(ad.getPrice()));
            //System.out.println("adContent: " + adContent);
            list = list + adContent;
        }
        result = result.replace("$list$", list);
        //System.out.println("list: " + list);
        //System.out.println("RESULT: " + result);
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(result);
    }
}
