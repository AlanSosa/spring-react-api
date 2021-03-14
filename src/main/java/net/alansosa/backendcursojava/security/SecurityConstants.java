package net.alansosa.backendcursojava.security;

import net.alansosa.backendcursojava.SpringApplicationContext;

public class SecurityConstants {

    public static final long EXPIRATION_DATE = 1800000; // 600000 10 minutes in miliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SING_UP_URL = "/users";
    //public static final String TOKEN_SECRET = "S5dn7DrfQh7OfdLvvN44xncufcSAEo27";

    //Here we make use of the SpringApplicationContext Bean to make use of the Spring Dependency AppProperties
    //That reads from the .properties file.
    public static String getTokenSecret(){
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenSecret();
    }
}
