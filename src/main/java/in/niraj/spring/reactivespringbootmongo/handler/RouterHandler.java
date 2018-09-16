package in.niraj.spring.reactivespringbootmongo.handler;

import in.niraj.spring.reactivespringbootmongo.model.Product;
import in.niraj.spring.reactivespringbootmongo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.*;

/**
 * created by niraj on Sep, 2018
 */
@Component
public class RouterHandler {

    @Autowired
    private ProductRepository productRepository;

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .body(productRepository.findAll(), Product.class);

    }

    public Mono<ServerResponse> getProductByID(ServerRequest serverRequest) {

        return ServerResponse.ok().body(productRepository.findById(serverRequest.pathVariable("id")), Product.class);


    }

    public Mono<ServerResponse> saveProduct(ServerRequest serverRequest) {

        Mono<Product> newProduct = serverRequest.bodyToMono(Product.class);
        return ServerResponse.ok().body(newProduct.map(product -> new Product(product.getName(), product.getDescription())).
                flatMap(productRepository::save), Product.class);

    }

    public Mono<ServerResponse> streamEvent(ServerRequest serverRequest) {

        String productId = serverRequest.pathVariable("id");
        return ServerResponse
                .ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(
                        productRepository.findById(productId)
                                .flatMapMany(product -> {
                                    Flux<Long> timeInterval = Flux.interval(Duration.ofSeconds(3));
                                    Flux<Product> productFlux = Flux.fromStream(Stream.generate(() -> product));
                                    return Flux.zip(timeInterval, productFlux).map(Tuple2::getT2);
                                }), Product.class);
    }

}
