/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */
package de.unistuttgart.iste.sopraws20.api.furniture;


import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.unistuttgart.iste.sopraws20.api.authentications.Authentication;
import de.unistuttgart.iste.sopraws20.api.rooms.Room;
import de.unistuttgart.iste.sopraws20.api.rooms.RoomRepository;
import de.unistuttgart.iste.sopraws20.api.users.User;
import de.unistuttgart.iste.sopraws20.api.users.UserRepository;

@RestController
public class FurnitureController {
		@Autowired
	    private FurnitureRepository furnitureRepository;
		@Autowired
		private UserRepository userRepository;
		@Autowired
		private RoomRepository roomRepository;


		// executed after start-up and dependency injection
	    @PostConstruct
	    public void init() {
	    	
	            
	        
	    }
	    // create a itestraFurniture
	    @PostMapping("/itestraFurnitures")
	    @ResponseStatus(HttpStatus.CREATED)
	    public Furniture createItestraFurniture(@Valid @RequestBody IterstraFurnitureDTO requestBody) {
	    	User user= userRepository.findById(requestBody.getUserId());
	        if (user != null) {
	        	if(user.getAuthentication().equals(requestBody.getAuthentication())) {
	        		List<Room> rooms =user.getRooms();
	        		for (Room room: rooms) {
	        			if (room.getId()==requestBody.getRoomId()) {
	        				List<Furniture> items=room.getFurnitureItems();
	        				Furniture newItem= new Furniture (requestBody.getItestraId());
	        				Furniture savedItem = furnitureRepository.save(newItem);
	        				items.add(savedItem);
	        				room.setFurnitureItems(items);
	        				roomRepository.save(room);
	        				user.setRooms(rooms);
	        				userRepository.save(user);
	        				return savedItem;
	        			}
	        			
	        		}
	        		throw new ResponseStatusException(HttpStatus.NOT_FOUND,
			                String.format("Room with ID %s not found!", requestBody.getRoomId()));
	        	}
	        	throw new ResponseStatusException(HttpStatus.FORBIDDEN,
		                String.format("Access Denied!"));
	        	
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", requestBody.getUserId()));
	        
	    }
	 // delete a itestra Furniture
	    @DeleteMapping("/itestraFurnitures/{id}")
	    public void deleteItestraFurniture(@PathVariable("id") long id, @RequestHeader("name") String name, @RequestHeader("password") String password, @RequestHeader("id") long userId) {
	    	User user= userRepository.findById(userId);
	        if (user != null) {
	        	Authentication sentData= new Authentication();
		    	sentData.setPassword(password);
		    	sentData.setUserName(name);
	        	if(user.getAuthentication().equals(sentData)) {
	        		List<Room> rooms =user.getRooms();
	        		for (Room room: rooms) {
	        				List<Furniture> items=room.getFurnitureItems();
	        				for (Furniture item :items) {
	        					if(item.getItestraId()==id) {
	        						long itemId=item.getId();
	        						items.remove(item);
	        						room.setFurnitureItems(items);
	        						user.setRooms(rooms);
			        				userRepository.save(user);
			        				roomRepository.save(room);
		        					furnitureRepository.deleteById(itemId);
			        				return;
	        					}
	        					
	        				}
	        				
	        				
	        			}
	        		throw new ResponseStatusException(HttpStatus.NOT_FOUND,
			                String.format("Furniture with ID %s not found!", id));
	        	}
	        	throw new ResponseStatusException(HttpStatus.FORBIDDEN,
		                String.format("Access Denied!"));
	        	
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", userId));
	    }

	    // create a Furniture
	    @PostMapping("/furnitures")
	    @ResponseStatus(HttpStatus.CREATED)
	    public Furniture createFurniture(@Valid @RequestBody FurnitureCreationDTO requestBody) {
	    	User user= userRepository.findById(requestBody.getUserId());
	        if (user != null) {
	        	if(user.getAuthentication().equals(requestBody.getAuthentication())) {
	        		List<Room> rooms =user.getRooms();
	        		for (Room room: rooms) {
	        			if (room.getId()==requestBody.getRoomId()) {
	        				List<Furniture> items=room.getFurnitureItems();
	        				Furniture newItem= new Furniture (requestBody.getName(), requestBody.getColor(), requestBody.getMaterial());

	        					newItem.setImgURL(requestBody.getImgURL());
	        				
	        				Furniture savedItem = furnitureRepository.save(newItem);
	        				items.add(savedItem);
	        				room.setFurnitureItems(items);
	        				roomRepository.save(room);
	        				user.setRooms(rooms);
	        				userRepository.save(user);
	        				return savedItem;
	        			}
	        			
	        		}
	        		throw new ResponseStatusException(HttpStatus.NOT_FOUND,
			                String.format("Room with ID %s not found!", requestBody.getRoomId()));
	        	}
	        	throw new ResponseStatusException(HttpStatus.FORBIDDEN,
		                String.format("Access Denied!"));
	        	
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", requestBody.getUserId()));
	        
	    }

	    // update a Furniture
	    @PutMapping("/furnitures/{id}")
	    public Furniture updateFurniture(@PathVariable("id") long id, @Valid @RequestBody FurnitureCreationDTO requestBody) {
	    	User user= userRepository.findById(requestBody.getUserId());
	        if (user != null) {
	        	if(user.getAuthentication().equals(requestBody.getAuthentication())) {
	        		List<Room> rooms =user.getRooms();
	        		for (Room room: rooms) {
	        			if (room.getId()==requestBody.getRoomId()) {
	        				List<Furniture> items=room.getFurnitureItems();
	        				for (Furniture item :items) {
	        					if(item.getId()==id) {
	        						item.setName(requestBody.getName());
		        					item.setColor(requestBody.getColor());
		        					item.setMaterial(requestBody.getMaterial());

			        				item.setImgURL(requestBody.getImgURL());
			        				
		        					Furniture savedItem = furnitureRepository.save(item);
		        					items.add(savedItem);
			        				room.setFurnitureItems(items);
			        				roomRepository.save(room);
			        				user.setRooms(rooms);
			        				userRepository.save(user);
			        				return savedItem;
	        					}
	        					
	        				}
	        				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	    			                String.format("Furniture with ID %s not found!", id));
	        				
	        			}
	        			
	        		}
	        		throw new ResponseStatusException(HttpStatus.NOT_FOUND,
			                String.format("Room with ID %s not found!", requestBody.getRoomId()));
	        	}
	        	throw new ResponseStatusException(HttpStatus.FORBIDDEN,
		                String.format("Access Denied!"));
	        	
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", requestBody.getUserId()));
	    }

	    // delete a Furniture
	    @DeleteMapping("/furnitures/{id}")
	    public void deleteFurniture(@PathVariable("id") long id, @RequestHeader("name") String name, @RequestHeader("password") String password, @RequestHeader("id") long userId) {
	    	User user= userRepository.findById(userId);
	        if (user != null) {
	        	Authentication sentData= new Authentication();
		    	sentData.setPassword(password);
		    	sentData.setUserName(name);
	        	if(user.getAuthentication().equals(sentData)) {
	        		List<Room> rooms =user.getRooms();
	        		for (Room room: rooms) {
	        				List<Furniture> items=room.getFurnitureItems();
	        				for (Furniture item :items) {
	        					if(item.getId()==id) {
	        						items.remove(item);
	        						room.setFurnitureItems(items);
	        						user.setRooms(rooms);
			        				userRepository.save(user);
			        				roomRepository.save(room);
		        					furnitureRepository.deleteById(id);
			        				return;
	        					}
	        					
	        				}
	        				
	        				
	        			}
	        		throw new ResponseStatusException(HttpStatus.NOT_FOUND,
			                String.format("Furniture with ID %s not found!", id));
	        	}
	        	throw new ResponseStatusException(HttpStatus.FORBIDDEN,
		                String.format("Access Denied!"));
	        	
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", userId));
	    }
}
