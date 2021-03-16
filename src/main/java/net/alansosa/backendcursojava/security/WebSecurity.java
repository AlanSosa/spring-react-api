package net.alansosa.backendcursojava.security;

import net.alansosa.backendcursojava.services.UserServiceInterface;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserServiceInterface userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserServiceInterface userService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web) throws Exception {
        //http://localhost:8080/swagger-ui/
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                // -- Swagger UI v3 (OpenAPI)
                "/v3/api-docs/**",
                "/swagger-ui/**");
                // other public endpoints of your API may be appended to this array);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
        Here we are configuring the security. By setting the http security parameters
        the configuration is letting ALL post request go through to the /users endpoint and /posts/lasts
        but with anyRequest() we're telling that the reset of request have to be authenticated.
        */
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers(HttpMethod.GET, "/posts/lasts").permitAll() //Make this endpoit without authentication
                .antMatchers(HttpMethod.GET, "/posts/{id}").permitAll() //Make this endpoint without authentication
        .anyRequest().authenticated()
        .and().addFilter(getAuthenticationFilter()) // here we add the authentication we configured.
                .addFilter(new AuthorizationFilter(authenticationManager()))
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); /*<--- When
        an authentication is successful between the client and the server, makes the API NOT TO HAVE a
        session variable in memory because we're using JWT to generate tokens for each request.
        So we don't need to save up the session in memory like a normal application would do.
        */
        //super.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
        //super.configure(auth);
    }

    //in this method we customize the login url.
    public AuthenticationFilter getAuthenticationFilter() throws Exception{
        //Get the authentication filter
        final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
        //Configure a new url for login
        filter.setFilterProcessesUrl("/users/login");
        return filter;
    }
}
