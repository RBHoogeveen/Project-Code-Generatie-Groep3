package io.swagger.api;

import io.swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-05T13:22:26.114Z[GMT]")
@RestController
public class RegisterApiController implements RegisterApi {

    private static final Logger log = LoggerFactory.getLogger(RegisterApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public RegisterApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<List<User>> registerUser(@Parameter(in = ParameterIn.DEFAULT, description = "Register", schema=@Schema()) @Valid @RequestBody User body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<User>>(objectMapper.readValue("[ {\n  \"isCustomer\" : true,\n  \"firstname\" : \"firstname\",\n  \"password\" : \"MySecretPassword\",\n  \"isEmployee\" : true,\n  \"phonenumber\" : \"phonenumber\",\n  \"id\" : 0,\n  \"accounts\" : [ {\n    \"balance\" : 6.027456183070403,\n    \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n    \"type\" : true\n  }, {\n    \"balance\" : 6.027456183070403,\n    \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n    \"type\" : true\n  } ],\n  \"email\" : \"email\",\n  \"username\" : \"username\",\n  \"lastname\" : \"lastname\"\n}, {\n  \"isCustomer\" : true,\n  \"firstname\" : \"firstname\",\n  \"password\" : \"MySecretPassword\",\n  \"isEmployee\" : true,\n  \"phonenumber\" : \"phonenumber\",\n  \"id\" : 0,\n  \"accounts\" : [ {\n    \"balance\" : 6.027456183070403,\n    \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n    \"type\" : true\n  }, {\n    \"balance\" : 6.027456183070403,\n    \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n    \"type\" : true\n  } ],\n  \"email\" : \"email\",\n  \"username\" : \"username\",\n  \"lastname\" : \"lastname\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<User>>(HttpStatus.NOT_IMPLEMENTED);
    }

}
