package net.alansosa.backendcursojava.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/*
We make it a @Component so we can access to Spring dependency injection objects
* */
@Component
public class AppProperties {

    /*
    Because we made it a @Component we can use the @Autowired to get the Environment dependency.
    * */
    @Autowired
    private Environment env;

    public String getTokenSecret(){
        return env.getProperty("tokenSecret");
    }

}
