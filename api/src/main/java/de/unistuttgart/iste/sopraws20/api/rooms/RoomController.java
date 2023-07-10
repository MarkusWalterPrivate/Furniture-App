/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */
package de.unistuttgart.iste.sopraws20.api.rooms;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.unistuttgart.iste.sopraws20.api.authentications.Authentication;
import de.unistuttgart.iste.sopraws20.api.users.User;
import de.unistuttgart.iste.sopraws20.api.users.UserRepository;


@RestController
public class RoomController {
		@Autowired
		private RoomRepository roomRepository;
		@Autowired
		private UserRepository userRepository;


		// executed after start-up and dependency injection
	    @PostConstruct
	    public void init() {
	    	
	       
	        
	    }

	    // get all Room
	    @GetMapping("/rooms")
	    public List<Room> getRooms(@RequestHeader("name") String name, @RequestHeader("password") String password, @RequestHeader("id") long userId) {
	    	User user =userRepository.findById(userId);
	    	if (user != null) {
	    		Authentication sentData= new Authentication();
		    	sentData.setPassword(password);
		    	sentData.setUserName(name);
	    		if(user.getAuthentication().equals(sentData)) {
		    		return user.getRooms();
		    	}
		    	throw new ResponseStatusException(HttpStatus.FORBIDDEN,
		                String.format("Access Denied!"));
	    	}
	    	throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", userId));
	    	
	    }

	    // get a single Room
	    @GetMapping("/rooms/{id}")
	    public Room getRoom(@PathVariable("id") long id,@RequestHeader("name") String name, @RequestHeader("password") String password, @RequestHeader("id") long userId) {

	        User user =userRepository.findById(userId);
	        if (user != null) {
	        	Authentication sentData= new Authentication();
	        	sentData.setPassword(password);
		    	sentData.setUserName(name);
	    		if(user.getAuthentication().equals(sentData)) {
		        	List<Room> rooms =user.getRooms();
		    		for (Room room:rooms) {
		    			if (room.getId()== id) {
		    				return room;
		    			}
		    		}
		    		throw new ResponseStatusException(HttpStatus.NOT_FOUND,
			                String.format("Room with ID %s not found!", id));
		    	}
		    	throw new ResponseStatusException(HttpStatus.FORBIDDEN,
		                String.format("Access Denied!"));
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", userId));

	        
	    }

	    // create a Room
	    @PostMapping("/rooms")
	    @ResponseStatus(HttpStatus.CREATED)
	    public List<Room> createRoom(@Valid @RequestBody RoomCreationDTO requestBody) {
	        
	        User user= userRepository.findById(requestBody.getUserId());
	        if (user != null) {
	        	if(user.getAuthentication().equals(requestBody.getAuthentication())) {
	        		Room room = new Room(requestBody.getName(), requestBody.getFurnitureItems());
	        		room.setImgURL(requestBody.getImgURL());
		        	Room savedRoom = roomRepository.save(room);
		        	List<Room> rooms =user.getRooms();
		        	rooms.add(savedRoom);
		        	user.setRooms(rooms);
		        	userRepository.save(user);
			        return rooms;
		    		
		    	}
		    	throw new ResponseStatusException(HttpStatus.FORBIDDEN,
		                String.format("Access Denied!"));
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", requestBody.getUserId()));
	        
	    }

	    // update a Room
	    @PutMapping("/rooms/{id}")
	    public List<Room> updateRoom(@PathVariable("id") long id, @Valid @RequestBody RoomCreationDTO requestBody) {
	    	
	        User user= userRepository.findById(requestBody.getUserId());
	        if (user != null) {
	        	if(user.getAuthentication().equals(requestBody.getAuthentication())) {
		        	List<Room> rooms =user.getRooms();
		        	for(Room room:rooms) {
		        		if (room.getId()==id){
		        			if(!requestBody.getFurnitureItems().equals(null)) {
		        				room.setFurnitureItems(requestBody.getFurnitureItems());
		        			}
		        			
		        			room.setName(requestBody.getName());
		        			room.setImgURL(requestBody.getImgURL());
		        			user.setRooms(rooms);
		        			roomRepository.save(room);
		        			userRepository.save(user);
		    		        return rooms;
		        		}
		        	}
		        	throw new ResponseStatusException(HttpStatus.NOT_FOUND,
			                String.format("Room with ID %s not found!", id));	
		    	}
		    	throw new ResponseStatusException(HttpStatus.FORBIDDEN,
		                String.format("Access Denied!"));
	        } 
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", id));
	    }

	    // delete a Room
	    @DeleteMapping("/rooms/{id}")
	    public List<Room> deleteRoom(@PathVariable("id") long id, @RequestHeader("name") String name, @RequestHeader("password") String password, @RequestHeader("id") long userId) {
	    	User user= userRepository.findById(userId);
	    	if (user != null) {
	    		Authentication sentData= new Authentication();
	        	sentData.setPassword(password);
		    	sentData.setUserName(name);
	    		if(user.getAuthentication().equals(sentData)) {
		        	List<Room> rooms =user.getRooms();
		        	for(Room room:rooms) {
		        		if (room.getId()==id){
		        			rooms.remove(room);
		        			userRepository.save(user);
		        			roomRepository.deleteById(id);
		        			return rooms;
		        		}
		        	}
		        	throw new ResponseStatusException(HttpStatus.NOT_FOUND,
			                String.format("Room with ID %s not found!", id));	
		    	}
		    	throw new ResponseStatusException(HttpStatus.FORBIDDEN,
		                String.format("Access Denied!"));
	    	}
	    	throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", id));
	        
	    }
}

