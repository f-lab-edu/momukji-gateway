package kr.flab.momukji.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import kr.flab.momukji.gateway.filter.CustomAuthFilter;

@EnableEurekaClient
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder, CustomAuthFilter customAuthFilter) {

		return builder.routes()
			.route(p -> p
				.path("/main/**")
				.filters(f -> f.rewritePath("/main", "/api")
					.filter(customAuthFilter.apply(new CustomAuthFilter.Config()))
				)
				.uri("lb://main"))
			.route(p -> p
				.path("/auth/**")
				.filters(f -> f.rewritePath("/auth/", "/api/"))
				.uri("lb://auth"))
			.route(p -> p
				.path("/user/**")
				.filters(f -> f.rewritePath("/user", "/api"))
				.uri("lb://user")
			)
			.build();
	}

}
