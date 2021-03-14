package net.alansosa.backendcursojava.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

//This class is used as a middleware when an already login user wants to consume any other endpoint
//and lets the request pass through the security so it can be consumed by validating the
//token that was sent in the headers.
public class AuthorizationFilter extends BasicAuthenticationFilter {

    public AuthorizationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String headerAuthorization = request.getHeader(SecurityConstants.HEADER_STRING);

        if(headerAuthorization == null || !headerAuthorization.startsWith(SecurityConstants.TOKEN_PREFIX)){
            //If header is empty or null and/or header doesn't start with 'BEARER ' then
            //the request and response is passed to the next filter
            chain.doFilter(request,response);
            return; // return so we don't continue running the code below.
        }

        //Get authentication token and validate that's created with the correct token secret.
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        //Set authentication token.
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //Continue the authorization filter to the next step.
        chain.doFilter(request, response);

        //super.doFilterInternal(request, response, chain);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        String token = request.getHeader(SecurityConstants.HEADER_STRING);

        if(token != null){
            token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

            /*
            After we used the key to decrypt the token we will find the token information:
            {
                "sub": "alan.sosa@gmail.com",
                "exp": 1614918010
            }
            We need to extract the "subject" because that contains the real user value.
            * */
            String user = Jwts.parser()
                    .setSigningKey(SecurityConstants.getTokenSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            //If user exists
            if(user != null){
                //Return the user.
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            //No user found
            return null;
        }
        //No token found
        return null;
    }
}
