package in.niraj.spring.reactivespringbootmongo.repository;

import in.niraj.spring.reactivespringbootmongo.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * created by niraj on Sep, 2018
 */
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
