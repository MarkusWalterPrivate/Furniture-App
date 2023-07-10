/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */
package de.unistuttgart.iste.sopraws20.api.users;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import de.unistuttgart.iste.sopraws20.api.authentications.Authentication;
import de.unistuttgart.iste.sopraws20.api.authentications.AuthenticationRepository;
import de.unistuttgart.iste.sopraws20.api.furniture.Furniture;
import de.unistuttgart.iste.sopraws20.api.furniture.FurnitureRepository;
import de.unistuttgart.iste.sopraws20.api.rooms.Room;
import de.unistuttgart.iste.sopraws20.api.rooms.RoomRepository;
import de.unistuttgart.iste.sopraws20.api.searchQuerys.SearchQuery;
import de.unistuttgart.iste.sopraws20.api.searchQuerys.SearchQueryRepository;


@RestController
public class UserController {
		@Autowired
		private UserRepository userRepository;

		@Autowired
		private AuthenticationRepository authenticationRepository;
		@Autowired
		private RoomRepository roomRepository;
		@Autowired 
		private FurnitureRepository furnitureRepository;
		@Autowired 
		private SearchQueryRepository searchQueryRepository;


		// executed after start-up and dependency injection
	    @PostConstruct
	    public void init() {
	    	
	      
	    	
	    }
	    @GetMapping("/status")
	    public void isOnline() {
	    	return;
	    }
	    // create a User, with empty room list and search query
	    @PostMapping("/users")
	    @ResponseStatus(HttpStatus.CREATED)
	    public User createUser(@Valid @RequestBody Authentication requestBody) {
	    	for (User user :userRepository.findAll()) {
	    		if (user.getAuthentication().getUserName().equals(requestBody.getUserName())) {
	    			 throw new ResponseStatusException(HttpStatus.CONFLICT,
	    		                String.format("Username already taken"));
	    		}
	    	}
	    	Authentication auth =new Authentication(requestBody.getUserName(), requestBody.getPassword());
	        User user = new User(auth);
	        authenticationRepository.save(auth);
	        User savedUser = userRepository.save(user);
	        return savedUser;
	        
	    }


	    // delete a User
	    @DeleteMapping("/users/{id}")
	    public void deleteUser(@PathVariable("id") long id,@RequestHeader("name") String name, @RequestHeader("password") String password) {
	    	User user = userRepository.findById(id);
	    	Authentication sendData = new Authentication();
	    	sendData.setPassword(password);
	    	sendData.setUserName(name);
	        if (user != null) {
	        	if(user.getAuthentication().equals(sendData)) {
	        		userRepository.deleteById(id);
	        		
	        		//Delete all user resources. Top down to automatically remove all associations
	        		if (!(user.getRooms().isEmpty())){
	        			
	        			for(Room room : user.getRooms()) {
	        				try {
	        					roomRepository.deleteById(room.getId());
	        				}catch (Exception e) {
	        					System.out.println("Room was already deleted");
	        				}
		        			
		        			if(!room.getFurnitureItems().isEmpty()) {
		        				for (Furniture furniture :room.getFurnitureItems()) {
		        					try {
			        				furnitureRepository.deleteById(furniture.getId());
		        					}catch(Exception e) {
		        						System.out.println("Furniture was already deleted");
		        					}
			        			}
		        			}
		        		}
	        		}
	        		if (!user.getSearchQuerys().isEmpty()) {
	        			for (SearchQuery search: user.getSearchQuerys()) {
	        				try {
		        			searchQueryRepository.deleteById(search.getId());
	        				} catch (Exception e) {
								System.out.println("Search was already deleted");
							}
		        		}
	        		}
	        		//Authentication cannot be null/empty so no check
	        		authenticationRepository.deleteById(user.getAuthentication().getId());
		            return;
	        	}
	        	throw new ResponseStatusException(HttpStatus.FORBIDDEN,
		                String.format("Access Denied!"));  
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", id));
	    }
}
