package demo.engine.query;

import demo.util.Utility;

import java.util.List;

public class QueryParser {
    private static QueryParser instance = null;

    public static QueryParser getInstance() {
        if(instance == null) {
            instance = new QueryParser();
        }
        return instance;
    }

    public List<String> QueryUnderstand(String query) {
        List<String> tokens = Utility.cleanedTokenize(query);
        return tokens;
    }
}
