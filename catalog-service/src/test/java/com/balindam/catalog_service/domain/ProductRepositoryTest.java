package com.balindam.catalog_service.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
        properties = {
            "spring.test.database.replace=none",
            "spring.datasource.url=jdbc:tc:postgresql:16-alpine:////db",
        })
@Sql("/test-data.sql")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldGetAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        assertThat(products).hasSize(15);
    }

    @Test
    void shouldGetProductByCode() {
        ProductEntity product = productRepository.findByCode("P200").orElseThrow();
        assertThat(product).isNotNull();
        assertThat(product.getCode()).isEqualTo("P200");
        assertThat(product.getName()).isEqualTo("The Night Circus");
        assertThat(product.getDescription())
                .isEqualTo("The Circus arrives without warning… but it will leave you changed forever.");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("26.99"));
    }

    @Test
    void shouldReturnEmptyWhenProductCodeNotExists() {
        assertThat(productRepository.findByCode("invalid-product-code")).isEmpty();
    }
}
