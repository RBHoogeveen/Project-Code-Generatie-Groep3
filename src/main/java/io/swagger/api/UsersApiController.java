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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-21T11:24:34.915Z[GMT]")
@RestController
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<List<User>> getUserById(@Parameter(in = ParameterIn.PATH, description = "The id", required=true, schema=@Schema()) @PathVariable("id") String id) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<User>>(objectMapper.readValue("[ {\n  \"isCustomer\" : true,\n  \"firstname\" : \"John\",\n  \"password\" : \"MySecretPassword\",\n  \"isEmployee\" : true,\n  \"dayLimit\" : 5.962133916683182,\n  \"phonenumber\" : \"06-1234567\",\n  \"id\" : 1,\n  \"transactionLimit\" : 5.637376656633329,\n  \"isActive\" : true,\n  \"email\" : \"JohnDoe@example.com\",\n  \"username\" : \"JohnDoe123\",\n  \"lastname\" : \"Doe\"\n}, {\n  \"isCustomer\" : true,\n  \"firstname\" : \"John\",\n  \"password\" : \"MySecretPassword\",\n  \"isEmployee\" : true,\n  \"dayLimit\" : 5.962133916683182,\n  \"phonenumber\" : \"06-1234567\",\n  \"id\" : 1,\n  \"transactionLimit\" : 5.637376656633329,\n  \"isActive\" : true,\n  \"email\" : \"JohnDoe@example.com\",\n  \"username\" : \"JohnDoe123\",\n  \"lastname\" : \"Doe\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<User>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<User>> getUserByUsername(@NotNull @Parameter(in = ParameterIn.QUERY, description = "the searchterm" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "searchTerm", required = true) String searchTerm) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<User>>(objectMapper.readValue("[ {\n  \"isCustomer\" : true,\n  \"firstname\" : \"John\",\n  \"password\" : \"MySecretPassword\",\n  \"isEmployee\" : true,\n  \"dayLimit\" : 5.962133916683182,\n  \"phonenumber\" : \"06-1234567\",\n  \"id\" : 1,\n  \"transactionLimit\" : 5.637376656633329,\n  \"isActive\" : true,\n  \"email\" : \"JohnDoe@example.com\",\n  \"username\" : \"JohnDoe123\",\n  \"lastname\" : \"Doe\"\n}, {\n  \"isCustomer\" : true,\n  \"firstname\" : \"John\",\n  \"password\" : \"MySecretPassword\",\n  \"isEmployee\" : true,\n  \"dayLimit\" : 5.962133916683182,\n  \"phonenumber\" : \"06-1234567\",\n  \"id\" : 1,\n  \"transactionLimit\" : 5.637376656633329,\n  \"isActive\" : true,\n  \"email\" : \"JohnDoe@example.com\",\n  \"username\" : \"JohnDoe123\",\n  \"lastname\" : \"Doe\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<User>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<User>> getUsers(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "number of records to skip for pagination" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "skip", required = false) Integer skip,@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, maximum="50"
, defaultValue="50")) @Valid @RequestParam(value = "limit", required = false, defaultValue="50") Integer limit) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<User>>(objectMapper.readValue("[ {\n  \"isCustomer\" : true,\n  \"firstname\" : \"John\",\n  \"password\" : \"MySecretPassword\",\n  \"isEmployee\" : true,\n  \"dayLimit\" : 5.962133916683182,\n  \"phonenumber\" : \"06-1234567\",\n  \"id\" : 1,\n  \"transactionLimit\" : 5.637376656633329,\n  \"isActive\" : true,\n  \"email\" : \"JohnDoe@example.com\",\n  \"username\" : \"JohnDoe123\",\n  \"lastname\" : \"Doe\"\n}, {\n  \"isCustomer\" : true,\n  \"firstname\" : \"John\",\n  \"password\" : \"MySecretPassword\",\n  \"isEmployee\" : true,\n  \"dayLimit\" : 5.962133916683182,\n  \"phonenumber\" : \"06-1234567\",\n  \"id\" : 1,\n  \"transactionLimit\" : 5.637376656633329,\n  \"isActive\" : true,\n  \"email\" : \"JohnDoe@example.com\",\n  \"username\" : \"JohnDoe123\",\n  \"lastname\" : \"Doe\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<User>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<User>> registerUser(@Parameter(in = ParameterIn.DEFAULT, description = "Register", schema=@Schema()) @Valid @RequestBody User body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<User>>(objectMapper.readValue("[ {\n  \"isCustomer\" : true,\n  \"firstname\" : \"John\",\n  \"password\" : \"MySecretPassword\",\n  \"isEmployee\" : true,\n  \"dayLimit\" : 5.962133916683182,\n  \"phonenumber\" : \"06-1234567\",\n  \"id\" : 1,\n  \"transactionLimit\" : 5.637376656633329,\n  \"isActive\" : true,\n  \"email\" : \"JohnDoe@example.com\",\n  \"username\" : \"JohnDoe123\",\n  \"lastname\" : \"Doe\"\n}, {\n  \"isCustomer\" : true,\n  \"firstname\" : \"John\",\n  \"password\" : \"MySecretPassword\",\n  \"isEmployee\" : true,\n  \"dayLimit\" : 5.962133916683182,\n  \"phonenumber\" : \"06-1234567\",\n  \"id\" : 1,\n  \"transactionLimit\" : 5.637376656633329,\n  \"isActive\" : true,\n  \"email\" : \"JohnDoe@example.com\",\n  \"username\" : \"JohnDoe123\",\n  \"lastname\" : \"Doe\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<User>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> updateUser(@Parameter(in = ParameterIn.PATH, description = "name that need to be updated", required=true, schema=@Schema()) @PathVariable("id") String id,@Parameter(in = ParameterIn.DEFAULT, description = "User to update", schema=@Schema()) @Valid @RequestBody User body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

}
