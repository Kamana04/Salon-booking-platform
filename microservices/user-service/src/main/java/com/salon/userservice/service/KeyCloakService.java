package com.salon.userservice.service;

import com.salon.userservice.payload.dto.*;
import com.salon.userservice.payload.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyCloakService {

    private static final String KEYCLOAK_BASE_URL = "http://localhost:8080";
    private static final String KEYCLOAK_ADMIN_API = KEYCLOAK_BASE_URL + "/admin/realms/master/users";

    private static final String TOKEN_URL = KEYCLOAK_BASE_URL + "/realms/master/protocol/openid-connect/token";

    private static final String CLIENT_ID = "salon-booking-client";
    private static final String CLIENT_SECRET = "VmxmbChHuWxco6I2CFBpV71gOFfluRzH";
    private static final String GRANT_TYPE = "password";
    private static final String SCOPE = "openid profile email";
    private static final String USERNAME = "zosh";
    private static final String PASSWORD = "admin";
    private static final String clientId = "12311fde-4ce4-4e18-af3e-6474b4235178";

    private final RestTemplate restTemplate; // to fetch data from api

    public void createUser(SignupDTO signupDTO) throws Exception {
        String ACCESS_TOKEN = getAdminAccessToken(USERNAME, PASSWORD, GRANT_TYPE, null).getAccessToken();
        //create credentials
        //requestbody
        Credential credential = new Credential();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(signupDTO.getPassword());

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(signupDTO.getUsername());
        userRequest.setLastName(signupDTO.getLastName());
        userRequest.setEmail(signupDTO.getEmail());
        userRequest.setEnabled(true);
        userRequest.setFirstName(signupDTO.getFirstName());

        //invoke api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        HttpEntity<UserRequest> requestHttpEntity = new HttpEntity<>(userRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                KEYCLOAK_ADMIN_API,
                HttpMethod.POST,
                requestHttpEntity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED) {
            System.out.println("User created successfully");

            KeyCloakUserDTO user = fetchFirstUserByUsername(signupDTO.getUsername(), ACCESS_TOKEN);
            KeyCloakRole role = getRoleByName(clientId, ACCESS_TOKEN, signupDTO.getRole().toString());

            List<KeyCloakRole> roles = new ArrayList<>();
            roles.add(role);
            //after user creation assign role
            assignRoleToUser(user.getId(), clientId, roles, ACCESS_TOKEN);
        } else {
            System.out.println("User creation failed");
            throw new Exception(response.getBody());
        }

    }

    //get admin access token
    public TokenResponse getAdminAccessToken(String username, String password, String grantType, String refreshToken) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("username", username);
        requestBody.add("password", password);
        requestBody.add("grant_type", grantType);
        requestBody.add("client_id", CLIENT_ID);
        requestBody.add("client_secret", CLIENT_SECRET);
        requestBody.add("scope", SCOPE);
        requestBody.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> requestHttpEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<TokenResponse> response = restTemplate.exchange(
                TOKEN_URL,
                HttpMethod.GET,
                requestHttpEntity,
                TokenResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new Exception("Failed to get access token");
        }
    }

    //get role by name
    //to assign role to the user
    public KeyCloakRole getRoleByName(String clientId, String token, String role) throws Exception {

        String url = KEYCLOAK_BASE_URL + "/admin/realms/master/clients/"+clientId+"/roles/"+role;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Void> requestHttpEntity = new HttpEntity<> (headers);
        ResponseEntity<KeyCloakRole> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestHttpEntity,
                KeyCloakRole.class
        );

            return response.getBody();
    }

    //fetch user by username
    //why?: inside rolemapping we need userid
    public KeyCloakUserDTO fetchFirstUserByUsername(String username, String token) throws Exception {


        String url = KEYCLOAK_BASE_URL + "/admin/realms/master/users?username=" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> requestHttpEntity = new HttpEntity<> (headers);
        ResponseEntity<KeyCloakUserDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestHttpEntity,
                KeyCloakUserDTO[].class
        );
        KeyCloakUserDTO[] users = response.getBody();
        if (users!=null && users.length > 0) {
            return users[0];

        }
        throw new Exception("User not found");

    }

    //for assigning role
    public void assignRoleToUser(String userId, String clientId, List<KeyCloakRole> roles, String token) throws Exception {

        String url = KEYCLOAK_BASE_URL+"/admin/realms/master/users/" + userId +
                "/role-mappings/clients/" + clientId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<List<KeyCloakRole>> entity = new HttpEntity<>(roles, headers);

        try {

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            response.getStatusCode();

        } catch (Exception e) {

            throw new Exception("Failed to assign roles: " + e.getMessage());
        }
    }
}
