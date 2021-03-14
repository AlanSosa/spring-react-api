package net.alansosa.backendcursojava.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.alansosa.backendcursojava.SpringApplicationContext;
import net.alansosa.backendcursojava.models.requests.UserLoginRequestModel;
import net.alansosa.backendcursojava.models.shared.dto.UserDto;
import net.alansosa.backendcursojava.services.UserServiceInterface;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

//This class handles the session login.
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    //This method tries the authentication
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            /*
            Here we are going to map the user's email and pass from the request to the UserLoginRequestModel
            */
            UserLoginRequestModel userModel = new ObjectMapper().readValue(
                    request.getInputStream(), UserLoginRequestModel.class
            );

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userModel.getEmail(),
                            userModel.getPassword(),
                            new ArrayList<>() //Authorities list. is empty we're not configuring this.
                    ));
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        //return super.attemptAuthentication(request, response);
    }

    //this method is called right after the authentication is successful
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String email = ((User)authResult.getPrincipal()).getUsername();
        /*
        Here we create the token to the user email, assign an expiration date and the key that's
        used to encrypt/decrypt the pass
        * */
        String token = Jwts.builder().setSubject(email).setExpiration(
                new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_DATE))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();

        /*
        Java Beans are named in CAMEL CASE format. So in order to retrieve the userService we have to call it
        in that format.
        * */
        UserServiceInterface userService = (UserServiceInterface) SpringApplicationContext.getBean("userService");
        UserDto user = userService.getUser(email);

        response.addHeader("userId", user.getUserId());

        //Add the authorization header to the response header.
        //JWT tokens are BEARER type.
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        //super.successfulAuthentication(request, response, chain, authResult);
    }
}
