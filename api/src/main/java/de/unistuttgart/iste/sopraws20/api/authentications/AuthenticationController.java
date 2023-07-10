/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */
package de.unistuttgart.iste.sopraws20.api.authentications;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.unistuttgart.iste.sopraws20.api.users.User;
import de.unistuttgart.iste.sopraws20.api.users.UserRepository;

@RestController
public class AuthenticationController {
		@Autowired
	    private AuthenticationRepository authenticationRepository;
		@Autowired
		private UserRepository userRepository;
		


		// executed after start-up and dependency injection
	    @PostConstruct
	    public void init() {
	    	
	    }


	    // login with a Authentication
	    @GetMapping("/authentications")
	    public User createAuthentication( @RequestHeader("name") String name, @RequestHeader("password") String password) {
	    	Authentication sentData= new Authentication();
	    	sentData.setPassword(password);
	    	sentData.setUserName(name);
	        for(User user : userRepository.findAll()) {
	        	if (user.getAuthentication().equals(sentData)) {
	        		return user;
	        	}
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("Username or password incorrect!"));
	    }

	    // update a Authentication
	    @PutMapping("/authentications/{id}")
	    public Authentication updateAuthentication(@PathVariable("id") long id, @Valid @RequestBody AuthenticationChangeDTO requestBody) {
	        User user =userRepository.findById(id);
	        if (user != null) {
	    		if(user.getAuthentication().equals(requestBody.getOldAuthentication())) {
	    			Authentication auth =authenticationRepository.findById(user.getAuthentication().getId());
	    			//Only password can be changed
	    			if(!(auth.getUserName().equals(requestBody.getNewAuthentication().getUserName()))) {
	    				throw new ResponseStatusException(HttpStatus.CONFLICT,
	    		                String.format("Username cannot be changed!"));
	    			}
	    			auth.setPassword(requestBody.getNewAuthentication().getPassword());
	    			authenticationRepository.save(auth);
	    			user.setAuthentication(auth);
	    			userRepository.save(user);
	    			return user.getAuthentication();
	    			
		    		
		    	}
		    	throw new ResponseStatusException(HttpStatus.FORBIDDEN,
		                String.format("Access Denied!"));
	    	}
	    	throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("Username or Password incorrect"));
	       
	    }
}
