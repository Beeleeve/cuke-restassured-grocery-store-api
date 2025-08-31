package com.grocerystore.api.utils;

import com.typesafe.config.ConfigFactory;

public final class ConfigReader {
    private static final String ENV;
    private static final String BASE_URL;
    private static final String TOKEN;

    static {
        String env = System.getenv("ENVIRONMENT");
        if (env == null || env.isEmpty()) {
            env = System.getProperty("env");
        }
        if (env == null || env.isEmpty()) {
            env = "dev";
        }
        ENV = env;

        String url = "";
        String token = "";

        try {
            com.typesafe.config.Config defaultConf = ConfigFactory.load("dev.conf");
            com.typesafe.config.Config conf = ConfigFactory.load(ENV + ".conf").withFallback(defaultConf);

            if (conf.hasPath("base.url")) {
                url = conf.getString("base.url");
            } else if (conf.hasPath("base.uri")) {
                url = conf.getString("base.uri");
            }

            if (conf.hasPath("bearer.token")) {
                token = conf.getString("bearer.token");
            } else if (conf.hasPath("auth.token")) {
                token = conf.getString("auth.token");
            }
        } catch (Throwable t) {
            // typesafe-config might not be present or file missing; leave defaults
        }

        BASE_URL = (url == null) ? "" : url;
        token = (token == null) ? "" : token;
        TOKEN = token.isEmpty() ? "" : (token.startsWith("Bearer ") ? token : "Bearer " + token);
    }

    private ConfigReader() {
        /* utility */
    }

    public static String getEnv() {
        return ENV;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getToken() {
        return TOKEN;
    }
}