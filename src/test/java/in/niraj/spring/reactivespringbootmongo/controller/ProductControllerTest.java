package in.niraj.spring.reactivespringbootmongo.controller;

import in.niraj.spring.reactivespringbootmongo.model.Product;
import in.niraj.spring.reactivespringbootmongo.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

/**
 * created by niraj on Sep, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveProduct() {


        webTestClient.post().uri("/api/product")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(createProduct()), Product.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("lenovo");
    }


    @Test
    public void testGetAllProducts() {
        webTestClient.get().uri("/api/product")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Product.class);
    }

    @Test
    public void testGetProductById() {

        Product savedProduct = productRepository.save(createProduct()).block();
        webTestClient.get()
                .uri("/api/product/{id}", Collections.singletonMap("id", savedProduct.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        assertThat(response.getResponseBody()).isNotNull());
    }


    @Test
    public void testUpdateProduct() {
        Product savedProduct = productRepository.save(createProduct()).block();

        Product newProduct = new Product("lenovo","updated description");

        webTestClient.put()
                .uri("/api/product/{id}", Collections.singletonMap("id", savedProduct.getId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newProduct), Product.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.description").isEqualTo("updated description")
                .jsonPath("$.name").isEqualTo("lenovo");
    }


    @Test
    public void testDeleteProductById() {
        Product savedProduct = productRepository.save(createProduct()).block();

        webTestClient.delete()
                .uri("/api/product/{id}", Collections.singletonMap("id",  savedProduct.getId()))
                .exchange()
                .expectStatus().isOk();
    }



    private Product createProduct() {
        return new Product("lenovo", "laptop with cool features");
    }

}