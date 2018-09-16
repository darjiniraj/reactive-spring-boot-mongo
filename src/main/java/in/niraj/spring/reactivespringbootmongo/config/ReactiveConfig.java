package in.niraj.spring.reactivespringbootmongo.config;

import in.niraj.spring.reactivespringbootmongo.handler.RouterHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * created by niraj on Sep, 2018
 */
@Configuration
public class ReactiveConfig {

    @Bean
    RouterFunction routerFunction(RouterHandler routerHandler) {

        return RouterFunctions.route(RequestPredicates.GET("/api/reactive/product"), routerHandler::getAll)
                .andRoute(RequestPredicates.GET("/api/reactive/product/{id}"), routerHandler::getProductByID)
                .andRoute(RequestPredicates.POST("/api/reactive/product"), routerHandler::saveProduct)
                .andRoute(RequestPredicates.GET("/api/reactive/{id}/event"), routerHandler::streamEvent);


    }
}
