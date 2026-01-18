package com.balindam.order_service.web.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.balindam.order_service.AbstractIT;
import com.balindam.order_service.domain.models.OrderSummary;
import com.balindam.order_service.testdata.TestDataFactory;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-orders.sql")
class OrderControllerTests extends AbstractIT {

    @Nested
    class CreateOrderTests {
        @Test
        void shouldCreateOrderSuccessfully() {
            mockGetProductByCode("P200", "The Night Circus", new BigDecimal(25.99));
            var payload =
                    """
                       {
                           "customer" : {
                               "name": "Balindam",
                               "email": "balindam@google.com",
                               "phone": "999999999"
                           },
                           "deliveryAddress" : {
                               "addressLine1": "HNO 167B",
                               "addressLine2": "ShaktiNagar",
                               "city": "Lucknow",
                               "state": "Uttar Pradesh",
                               "zipCode": "226016",
                               "country": "India"
                           },
                           "items": [
                               {
                                   "code": "P200",
                                   "name": "The Night Circus",
                                   "price": 25.99,
                                   "quantity": 1
                               }
                           ]
                       }
                    """;
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());
        }

        @Test
        void shouldReturnBadRequestWhenMandatoryDataIsMissing() {
            var payload = TestDataFactory.createOrderRequestWithInvalidCustomer();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class GetOrderTests {
        @Test
        void shouldReturnNotFoundForNonExistingOrder() {
            String nonExistingOrderNumber = "ORD-99999";
            given().when()
                    .get("/api/orders/{orderNumber}", nonExistingOrderNumber)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        void shouldGetOrdersSuccessfully() {
            List<OrderSummary> orderSummaries = given().when()
                    .get("/api/orders")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(new TypeRef<>() {});

            assertThat(orderSummaries).hasSize(2);
        }
    }

    @Nested
    class GetOrderByOrderNumberTests {
        String orderNumber = "order-123";

        @Test
        void shouldGetOrderSuccessfully() {
            given().when()
                    .get("/api/orders/{orderNumber}", orderNumber)
                    .then()
                    .statusCode(200)
                    .body("orderNumber", is(orderNumber))
                    .body("items.size()", is("2"));
        }
    }
}
