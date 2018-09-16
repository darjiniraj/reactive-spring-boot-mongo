package in.niraj.spring.reactivespringbootmongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * created by niraj on Sep, 2018
 */
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @NotNull
    @Size(max = 20)
    private String name;

    private String description;

    @NotNull
    private Date createdAt = new Date();

    public Product(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
