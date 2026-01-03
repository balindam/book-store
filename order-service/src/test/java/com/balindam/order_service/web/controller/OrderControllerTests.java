package com.balindam.order_service.web.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import com.balindam.order_service.AbstractIT;
import com.balindam.order_service.testdata.TestDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class OrderControllerTests extends AbstractIT {

    @Nested
    class CreateOrderTests {
        @Test
        void shouldCreateOrderSuccessfully() {
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
                                   "name": "The Starless Sea",
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
}
