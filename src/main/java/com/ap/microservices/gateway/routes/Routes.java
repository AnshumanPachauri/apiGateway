package com.ap.microservices.gateway.routes;

import java.net.URI;

import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> productServiceRouter() {

        return GatewayRouterFunctions.route("product_service")
                .route(
                    // Changed from "/api/product/" to match the base path and all sub-paths
                    RequestPredicates.path("/api/product/**"), 
                    HandlerFunctions.http()
                )
                .before(BeforeFilterFunctions.uri("http://localhost:8080"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("product_service_circuit_breaker",
                		URI.create("forward:/fallBackRoute")))
                .build();
    }
    
    @Bean
    public RouterFunction<ServerResponse> orderServiceRouter() {

        return GatewayRouterFunctions.route("order_service")
                .route(
                    // Changed from "/api/order/" to match the base path and all sub-paths
                    RequestPredicates.path("/api/order/**"), 
                    HandlerFunctions.http()
                )
                .before(BeforeFilterFunctions.uri("http://localhost:8081"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("order_service_circuit_breaker",
                		URI.create("forward:/fallBackRoute")))
                .build();
    }
    
    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRouter() {

        return GatewayRouterFunctions.route("inventory_service")
                .route(
                    // Changed from "/api/inventory/" to match the base path and all sub-paths
                    RequestPredicates.path("/api/inventory/**"), 
                    HandlerFunctions.http()
                )
                .before(BeforeFilterFunctions.uri("http://localhost:8082"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventory_service_circuit_breaker",
                		URI.create("forward:/fallBackRoute")))
                .build();
    }
    
    @Bean
    public RouterFunction<ServerResponse> productServiceSwaggerRouter() {

        return GatewayRouterFunctions.route("product_service_swagger")
                .route(
                    // Changed from "/api/product/" to match the base path and all sub-paths
                    RequestPredicates.path("/aggregate/product-service/v3/api-docs"), 
                    HandlerFunctions.http()
                )
                .before(BeforeFilterFunctions.uri("http://localhost:8080")).before(BeforeFilterFunctions.setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("product_service_swagger_circuit_breaker",
                		URI.create("forward:/fallBackRoute")))
                .build();
    }
    
    @Bean
    public RouterFunction<ServerResponse> orderServiceSwaggerRouter() {

        return GatewayRouterFunctions.route("order_service_swagger")
                .route(
                    // Changed from "/api/order/" to match the base path and all sub-paths
                    RequestPredicates.path("/aggregate/order-service/v3/api-docs"), 
                    HandlerFunctions.http()
                )
                .before(BeforeFilterFunctions.uri("http://localhost:8081")).before(BeforeFilterFunctions.setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("order_service_swagger_circuit_breaker",
                		URI.create("forward:/fallBackRoute")))
                .build();
    }
    
    @Bean
    public RouterFunction<ServerResponse> inventoryServiceSwaggerRouter() {

        return GatewayRouterFunctions.route("inventory_service_swagger")
                .route(
                    // Changed from "/api/order/" to match the base path and all sub-paths
                    RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"), 
                    HandlerFunctions.http()
                )
                .before(BeforeFilterFunctions.uri("http://localhost:8082")).before(BeforeFilterFunctions.setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventory_service_swagger_circuit_breaker",
                		URI.create("forward:/fallBackRoute")))
                .build();
    }
    
    public RouterFunction<ServerResponse> fallBackRoute(){
    	return GatewayRouterFunctions.route("fallbackRoute")
    			.GET(".fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
    					.body("Service Unavailable Please Try Again Later!!")).build();
    }
    
}
