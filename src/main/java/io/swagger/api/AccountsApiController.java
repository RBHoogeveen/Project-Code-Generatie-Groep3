package io.swagger.api;

import io.swagger.model.Account;
import io.swagger.model.Deposit;
import io.swagger.model.Transaction;
import io.swagger.model.Withdrawal;
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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-21T10:25:28.299Z[GMT]")
@RestController
public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> deposit(@Parameter(in = ParameterIn.DEFAULT, description = "deposit to create", schema=@Schema()) @Valid @RequestBody Deposit body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Deposit>> getDeposits(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "number of records to skip for pagination" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "skip", required = false) Integer skip,@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, maximum="50"
, defaultValue="50")) @Valid @RequestParam(value = "limit", required = false, defaultValue="50") Integer limit) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Deposit>>(objectMapper.readValue("[ {\n  \"date\" : \"14-05-2021\",\n  \"amount\" : 6.027456183070403,\n  \"userPerforming\" : {\n    \"isCustomer\" : true,\n    \"firstname\" : \"John\",\n    \"password\" : \"MySecretPassword\",\n    \"isEmployee\" : true,\n    \"dayLimit\" : 5.962133916683182,\n    \"phonenumber\" : \"06-1234567\",\n    \"id\" : 1,\n    \"transactionLimit\" : 5.637376656633329,\n    \"isActive\" : true,\n    \"email\" : \"JohnDoe@example.com\",\n    \"username\" : \"JohnDoe123\",\n    \"lastname\" : \"Doe\"\n  },\n  \"sender\" : {\n    \"balance\" : 0.8008281904610115,\n    \"absoluteLimit\" : 6.027456183070403,\n    \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n    \"type\" : true,\n    \"isActive\" : true,\n    \"userID\" : \"3fa85f64-5717-4562-b3fc-2c963f66afa6\"\n  },\n  \"id\" : 0\n}, {\n  \"date\" : \"14-05-2021\",\n  \"amount\" : 6.027456183070403,\n  \"userPerforming\" : {\n    \"isCustomer\" : true,\n    \"firstname\" : \"John\",\n    \"password\" : \"MySecretPassword\",\n    \"isEmployee\" : true,\n    \"dayLimit\" : 5.962133916683182,\n    \"phonenumber\" : \"06-1234567\",\n    \"id\" : 1,\n    \"transactionLimit\" : 5.637376656633329,\n    \"isActive\" : true,\n    \"email\" : \"JohnDoe@example.com\",\n    \"username\" : \"JohnDoe123\",\n    \"lastname\" : \"Doe\"\n  },\n  \"sender\" : {\n    \"balance\" : 0.8008281904610115,\n    \"absoluteLimit\" : 6.027456183070403,\n    \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n    \"type\" : true,\n    \"isActive\" : true,\n    \"userID\" : \"3fa85f64-5717-4562-b3fc-2c963f66afa6\"\n  },\n  \"id\" : 0\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Deposit>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Deposit>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Transaction>> getTransactions(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "number of records to skip for pagination" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "skip", required = false) Integer skip,@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, maximum="50"
, defaultValue="50")) @Valid @RequestParam(value = "limit", required = false, defaultValue="50") Integer limit) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Transaction>>(objectMapper.readValue("[ {\n  \"date\" : \"14-05-2021\",\n  \"amount\" : 6.027456183070403,\n  \"userPerforming\" : {\n    \"isCustomer\" : true,\n    \"firstname\" : \"John\",\n    \"password\" : \"MySecretPassword\",\n    \"isEmployee\" : true,\n    \"dayLimit\" : 5.962133916683182,\n    \"phonenumber\" : \"06-1234567\",\n    \"id\" : 1,\n    \"transactionLimit\" : 5.637376656633329,\n    \"isActive\" : true,\n    \"email\" : \"JohnDoe@example.com\",\n    \"username\" : \"JohnDoe123\",\n    \"lastname\" : \"Doe\"\n  },\n  \"fromAccount\" : {\n    \"balance\" : 0.8008281904610115,\n    \"absoluteLimit\" : 6.027456183070403,\n    \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n    \"type\" : true,\n    \"isActive\" : true,\n    \"userID\" : \"3fa85f64-5717-4562-b3fc-2c963f66afa6\"\n  },\n  \"id\" : 0\n}, {\n  \"date\" : \"14-05-2021\",\n  \"amount\" : 6.027456183070403,\n  \"userPerforming\" : {\n    \"isCustomer\" : true,\n    \"firstname\" : \"John\",\n    \"password\" : \"MySecretPassword\",\n    \"isEmployee\" : true,\n    \"dayLimit\" : 5.962133916683182,\n    \"phonenumber\" : \"06-1234567\",\n    \"id\" : 1,\n    \"transactionLimit\" : 5.637376656633329,\n    \"isActive\" : true,\n    \"email\" : \"JohnDoe@example.com\",\n    \"username\" : \"JohnDoe123\",\n    \"lastname\" : \"Doe\"\n  },\n  \"fromAccount\" : {\n    \"balance\" : 0.8008281904610115,\n    \"absoluteLimit\" : 6.027456183070403,\n    \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n    \"type\" : true,\n    \"isActive\" : true,\n    \"userID\" : \"3fa85f64-5717-4562-b3fc-2c963f66afa6\"\n  },\n  \"id\" : 0\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Account>> getUserAccount(@NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "username", required = true) String username,@NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "include inactive", required = true) Boolean includeInactive) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Account>>(objectMapper.readValue("[ {\n  \"balance\" : 0.8008281904610115,\n  \"absoluteLimit\" : 6.027456183070403,\n  \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n  \"type\" : true,\n  \"isActive\" : true,\n  \"userID\" : \"3fa85f64-5717-4562-b3fc-2c963f66afa6\"\n}, {\n  \"balance\" : 0.8008281904610115,\n  \"absoluteLimit\" : 6.027456183070403,\n  \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n  \"type\" : true,\n  \"isActive\" : true,\n  \"userID\" : \"3fa85f64-5717-4562-b3fc-2c963f66afa6\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Account>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Account>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Transaction>> getWithdrawals(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "number of records to skip for pagination" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "skip", required = false) Integer skip,@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, maximum="50"
, defaultValue="50")) @Valid @RequestParam(value = "limit", required = false, defaultValue="50") Integer limit) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Transaction>>(objectMapper.readValue("[ {\n  \"date\" : \"14-05-2021\",\n  \"amount\" : 6.027456183070403,\n  \"userPerforming\" : {\n    \"isCustomer\" : true,\n    \"firstname\" : \"John\",\n    \"password\" : \"MySecretPassword\",\n    \"isEmployee\" : true,\n    \"dayLimit\" : 5.962133916683182,\n    \"phonenumber\" : \"06-1234567\",\n    \"id\" : 1,\n    \"transactionLimit\" : 5.637376656633329,\n    \"isActive\" : true,\n    \"email\" : \"JohnDoe@example.com\",\n    \"username\" : \"JohnDoe123\",\n    \"lastname\" : \"Doe\"\n  },\n  \"fromAccount\" : {\n    \"balance\" : 0.8008281904610115,\n    \"absoluteLimit\" : 6.027456183070403,\n    \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n    \"type\" : true,\n    \"isActive\" : true,\n    \"userID\" : \"3fa85f64-5717-4562-b3fc-2c963f66afa6\"\n  },\n  \"id\" : 0\n}, {\n  \"date\" : \"14-05-2021\",\n  \"amount\" : 6.027456183070403,\n  \"userPerforming\" : {\n    \"isCustomer\" : true,\n    \"firstname\" : \"John\",\n    \"password\" : \"MySecretPassword\",\n    \"isEmployee\" : true,\n    \"dayLimit\" : 5.962133916683182,\n    \"phonenumber\" : \"06-1234567\",\n    \"id\" : 1,\n    \"transactionLimit\" : 5.637376656633329,\n    \"isActive\" : true,\n    \"email\" : \"JohnDoe@example.com\",\n    \"username\" : \"JohnDoe123\",\n    \"lastname\" : \"Doe\"\n  },\n  \"fromAccount\" : {\n    \"balance\" : 0.8008281904610115,\n    \"absoluteLimit\" : 6.027456183070403,\n    \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n    \"type\" : true,\n    \"isActive\" : true,\n    \"userID\" : \"3fa85f64-5717-4562-b3fc-2c963f66afa6\"\n  },\n  \"id\" : 0\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> transaction(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Transaction body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> updateAccount(@NotNull @Parameter(in = ParameterIn.QUERY, description = "IBAN of account that needs udating." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "iban", required = true) String iban,@Parameter(in = ParameterIn.DEFAULT, description = "Account to update", schema=@Schema()) @Valid @RequestBody Account body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> withdrawal(@Parameter(in = ParameterIn.DEFAULT, description = "deposit to create", schema=@Schema()) @Valid @RequestBody Withdrawal body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

}
