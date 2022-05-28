package com.infybuzz.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    Logger logger = LoggerFactory.getLogger(AuthorizationHeaderFilter.class);

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    public static class Config {
        private String roles;

        public String getRoles() {
            return roles;
        }

        public void setRoles(String roles) {
            this.roles = roles;
        }
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("roles");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            try {
                var routingRoles = getRouteAuthorizationRoles(config.getRoles());
                for (String role : routingRoles) {
                    logger.info("Role " + role);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            logger.info("AuthorizationHeaderFilter apply Method");
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");

            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT is not valid", HttpStatus.UNAUTHORIZED);
            }

            var response = chain.filter(exchange);
            return response;
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean isJwtValid(String jwt) {

        logger.info("Jwt Valid Check Method");
        boolean isJwtValid = true;
        return isJwtValid;

    }

    private String[] getRouteAuthorizationRoles(String roles) throws JsonProcessingException {
        logger.info("getAuthRoles Method");
        return roles.split("-");
    }

}
