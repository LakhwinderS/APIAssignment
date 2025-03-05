package steps;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import io.restassured.response.Response;


public class bookingSteps {


        private Response response;
    private String bookingJson;
    private String bookingId;  // Variable to store the booking ID for GET request

    // Positive Test - valid booking payload
    @Given("I have a valid booking payload")
    public void iHaveAValidBookingPayload() {
        bookingJson = "{\n" +
                "  \"firstname\" : \"testFirstName\",\n" +
                "  \"lastname\" : \"lastName\",\n" +
                "  \"totalprice\" : 10.11,\n" +
                "  \"depositpaid\" : true,\n" +
                "  \"bookingdates\" : {\n" +
                "    \"checkin\" : \"2022-01-01\",\n" +
                "    \"checkout\" : \"2024-01-01\"\n" +
                "  },\n" +
                "  \"additionalneeds\" : \"testAdd\"\n" +
                "}";
    }

    @When("I send the POST request to create the booking")
    public void iSendThePostRequestToCreateTheBooking() {
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(bookingJson)
                .when()
                .post("https://restful-booker.herokuapp.com/booking");
    }

    @Then("the response status code should be 200")
    public void theResponseStatusCodeShouldBe200() {
        assertThat(response.getStatusCode(), equalTo(200));
    }

    @Then("the response should contain the created booking details")
    public void theResponseShouldContainTheCreatedBookingDetails() {
        // Save the booking ID for future GET request
        bookingId = response.jsonPath().getString("bookingid");
        assertThat(response.jsonPath().getString("firstname"), equalTo("testFirstName"));
        assertThat(response.jsonPath().getString("lastname"), equalTo("lastName"));
        assertThat(response.jsonPath().getFloat("totalprice"), equalTo(10.11f));
        assertThat(response.jsonPath().getBoolean("depositpaid"), equalTo(true));
        assertThat(response.jsonPath().getString("bookingdates.checkin"), equalTo("2022-01-01"));
        assertThat(response.jsonPath().getString("bookingdates.checkout"), equalTo("2024-01-01"));
        assertThat(response.jsonPath().getString("additionalneeds"), equalTo("testAdd"));
    }

    // Negative Test - missing first name
    @Given("I have a booking payload with missing first name")
    public void iHaveABookingPayloadWithMissingFirstName() {
        bookingJson = "{\n" +
                "  \"lastname\" : \"lastName\",\n" +
                "  \"totalprice\" : 10.11,\n" +
                "  \"depositpaid\" : true,\n" +
                "  \"bookingdates\" : {\n" +
                "    \"checkin\" : \"2022-01-01\",\n" +
                "    \"checkout\" : \"2024-01-01\"\n" +
                "  },\n" +
                "  \"additionalneeds\" : \"testAdd\"\n" +
                "}";
    }

    @Then("the response status code should be 400")
    public void theResponseStatusCodeShouldBe400() {
        assertThat(response.getStatusCode(), equalTo(400));
    }

    @Then("the response should contain an error message")
    public void theResponseShouldContainAnErrorMessage() {
        String errorMessage = response.jsonPath().getString("message");
        assertThat(errorMessage, containsString("Invalid input"));
    }

    // Negative Test - incorrect data type for price
    @Given("I have a booking payload with incorrect data type for price")
    public void iHaveABookingPayloadWithIncorrectDataTypeForPrice() {
        bookingJson = "{\n" +
                "  \"firstname\" : \"testFirstName\",\n" +
                "  \"lastname\" : \"lastName\",\n" +
                "  \"totalprice\" : \"invalidPrice\",\n" +
                "  \"depositpaid\" : true,\n" +
                "  \"bookingdates\" : {\n" +
                "    \"checkin\" : \"2022-01-01\",\n" +
                "    \"checkout\" : \"2024-01-01\"\n" +
                "  },\n" +
                "  \"additionalneeds\" : \"testAdd\"\n" +
                "}";
    }

    // Negative Test - invalid dates
    @Given("I have a booking payload with invalid check-in and check-out dates")
    public void iHaveABookingPayloadWithInvalidDates() {
        bookingJson = "{\n" +
                "  \"firstname\" : \"testFirstName\",\n" +
                "  \"lastname\" : \"lastName\",\n" +
                "  \"totalprice\" : 10.11,\n" +
                "  \"depositpaid\" : true,\n" +
                "  \"bookingdates\" : {\n" +
                "    \"checkin\" : \"2024-01-01\",\n" +
                "    \"checkout\" : \"2022-01-01\"\n" +
                "  },\n" +
                "  \"additionalneeds\" : \"testAdd\"\n" +
                "}";
    }

    // Negative Test - handle invalid dates
    @Then("the response should contain an error message for invalid dates")
    public void theResponseShouldContainAnErrorMessageForInvalidDates() {
        String errorMessage = response.jsonPath().getString("message");
        assertThat(errorMessage, containsString("Invalid dates"));
    }

    // GET Test - Retrieve the booking using GET request
    @Given("I have a valid booking ID")
    public void iHaveAValidBookingID() {
        // Assume the booking ID has been set from the previous successful POST response
        assertThat(bookingId, notNullValue());
    }

    @When("I send the GET request to retrieve the booking")
    public void iSendTheGetRequestToRetrieveTheBooking() {
        response = RestAssured.given()
                .when()
                .get("https://restful-booker.herokuapp.com/booking/" + bookingId);
    }

    @Then("the response status code should be 200")
    public void theResponseStatusCodeShouldBe200ForGet() {
        assertThat(response.getStatusCode(), equalTo(200));
    }

    @Then("the response should contain the correct booking details")
    public void theResponseShouldContainTheCorrectBookingDetails() {
        assertThat(response.jsonPath().getString("firstname"), equalTo("testFirstName"));
        assertThat(response.jsonPath().getString("lastname"), equalTo("lastName"));
        assertThat(response.jsonPath().getFloat("totalprice"), equalTo(10.11f));
        assertThat(response.jsonPath().getBoolean("depositpaid"), equalTo(true));
        assertThat(response.jsonPath().getString("bookingdates.checkin"), equalTo("2022-01-01"));
        assertThat(response.jsonPath().getString("bookingdates.checkout"), equalTo("2024-01-01"));
        assertThat(response.jsonPath().getString("additionalneeds"), equalTo("testAdd"));
    }
}
