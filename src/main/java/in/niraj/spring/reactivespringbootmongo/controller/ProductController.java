package in.niraj.spring.reactivespringbootmongo.controller;

import in.niraj.spring.reactivespringbootmongo.model.Product;
import in.niraj.spring.reactivespringbootmongo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import javax.validation.Valid;

import java.time.Duration;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

/**
 * created by niraj on Sep, 2018
 */
@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/product")
    public Mono<Product> saveProduct(@Valid @RequestBody Product product) {
        return productRepository.save(product);
    }

    @GetMapping("/product")
    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/product/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable(value = "id") String productId) {
        return productRepository.findById(productId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PutMapping("/product/{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@Valid @RequestBody Product newProdcuct,
                                                       @PathVariable(value = "id") String productId) {
        return productRepository.findById(productId)
                .flatMap(dbProduct -> {
                    dbProduct.setDescription(newProdcuct.getDescription());
                    return productRepository.save(dbProduct);
                }).map(updatedProduct -> new ResponseEntity<>(updatedProduct, OK))
                .defaultIfEmpty(new ResponseEntity<>(NOT_FOUND));
    }

    @DeleteMapping("/product/{id}")
    public Mono<ResponseEntity<Void>> deleteProductById(@PathVariable(value = "id") String productId) {
        return productRepository.findById(productId)
                .flatMap(dbProduct -> productRepository.delete(dbProduct))
                .then(Mono.just(new ResponseEntity<Void>(OK)))
                .defaultIfEmpty(new ResponseEntity<>(NOT_FOUND));
    }

    @GetMapping(value = "/{id}/event", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> streamEvents(@PathVariable("id") String productId) {

        return productRepository.findById(productId).flatMapMany(product -> {
            Flux<Long> timeInterval = Flux.interval(Duration.ofSeconds(3));
            Flux<Product> productFlux = Flux.fromStream(Stream.generate(() -> product));
            return Flux.zip(timeInterval, productFlux).map(Tuple2::getT2);
        });
    }

}
