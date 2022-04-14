package com.example.springboot;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            System.out.println("granted Authority: " + grantedAuthority.getAuthority());
        }
        Map<String, Object> attributes = principal.getAttributes();
        for (String key : attributes.keySet()) {
            System.out.println("Key: " + key + ", Value: " + attributes.get(key));
        }

        return Collections.singletonMap("name", principal.getAttribute("name"));

    }

    @GetMapping("/restricted")
    public Map<String, Object> restricted() {
        printUserDetails();
        return Collections.singletonMap("key", "secretValue");
    }

    private void printUserDetails() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            System.out.println("Authentication was Null, use default user");
            return;

        }

        final Object principal = auth.getPrincipal();
        System.out.println("Principal: " + principal);
        final Object credentials = auth.getCredentials();
        System.out.println("Credentials: " + credentials);
        final Object details = auth.getDetails();
        System.out.println("Details: " + details);

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            System.out.println("granted Authority: " + grantedAuthority.getAuthority());
        }
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

}
