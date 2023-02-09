package br.com.felipemenezesdm.integration.authserver.service;

import br.com.felipemenezesdm.integration.authserver.props.OAuthClientProps;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.UUID;
import static br.com.felipemenezesdm.infrastructure.constant.General.*;

@Service
public class AssetTokenRequestService {
    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @Autowired
    OAuthClientProps oAuthClientProps;

    RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        restTemplate = restTemplateBuilder.build();
    }

    public void validate(String token, String correlationId, String[] scopes) {
        if(!oAuthClientProps.getEnabled()) {
            return;
        }

        String uri = String.valueOf(oAuthClientProps.getAssetUri()).concat( "?scopes=").concat(String.join(",", scopes));
        HttpHeaders headers = new HttpHeaders();
        headers.add(STR_AUTHORIZATION, token);
        headers.add(STR_CORRELATION_ID, Optional.ofNullable(correlationId).orElse(UUID.randomUUID().toString()));

        HttpEntity<?> httpRequest =  new HttpEntity<>(null, headers);
        restTemplate.exchange(uri, HttpMethod.GET, httpRequest, JSONObject.class);
    }
}
