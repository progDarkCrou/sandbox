package io.swagger.api;

import io.swagger.model.*;

import io.swagger.model.Error;
import io.swagger.model.Profile;

import io.swagger.annotations.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/me", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/me", description = "the me API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringBootServerCodegen", date = "2016-06-12T10:39:16.322Z")
public class MeApi {

  @ApiOperation(value = "User Profile", notes = "The User Profile endpoint returns information about the Uber user that has authorized with the application.", response = Profile.class)
  @ApiResponses(value = { 
    @ApiResponse(code = 200, message = "Profile information for a user", response = Profile.class),
    @ApiResponse(code = 200, message = "Unexpected error", response = Profile.class) })
  @RequestMapping(value = "",
    produces = { "application/json" }, 
    
    method = RequestMethod.GET)
  public ResponseEntity<Profile> meGet()
      throws NotFoundException {
      // do some magic!
      return new ResponseEntity<Profile>(HttpStatus.OK);
  }

}
