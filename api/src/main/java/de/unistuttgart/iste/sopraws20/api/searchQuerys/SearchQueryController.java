/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */
package de.unistuttgart.iste.sopraws20.api.searchQuerys;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
import de.unistuttgart.iste.sopraws20.api.users.UserRequestDTO;


@RestController
public class SearchQueryController {
		@Autowired
	    private SearchQueryRepository searchQueryRepository;
		@Autowired
		private UserRepository userRepository;



		// executed after start-up and dependency injection
	    @PostConstruct
	    public void init() {
	    	
	        
	    }

	    // get all SearchQuery
	    @GetMapping("/searchquerys")
	    public List<SearchQuery> getSearchQuerys(@RequestHeader("name") String name, @RequestHeader("password") String password, @RequestHeader("id") long id) {
	    	User user =userRepository.findById(id);
	    	if (user != null) {
		    	Authentication sentData= new Authentication();
		    	sentData.setPassword(password);
		    	sentData.setUserName(name);
	    		if(user.getAuthentication().equals(sentData)) {
		    		return user.getSearchQuerys();
		    	}
		    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
		                String.format("Access Denied!"));
	    	}
	    	throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", id));
	    }

	    // get a single SearchQuery
	    @GetMapping("/searchquerys/{id}")
	    public SearchQuery getSearchQuery(@PathVariable("id") long id, @RequestHeader("name") String name, @RequestHeader("password") String password, @RequestHeader("id") long userId) {

	    	User user =userRepository.findById(userId);
	        if (user != null) {
	        	Authentication sentData= new Authentication();
		    	sentData.setPassword(password);
		    	sentData.setUserName(name);
	        	if(user.getAuthentication().equals(sentData)) {
		        	List<SearchQuery> searches =user.getSearchQuerys();
		    		for (SearchQuery search: searches) {
		    			if (search.getId()== id) {
		    				return search;
		    			}
		    		}
		    		throw new ResponseStatusException(HttpStatus.NOT_FOUND,
			                String.format("SearchQuerry with ID %s not found!", id));
		    	}
		    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
		                String.format("Access Denied!"));
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", userId));

	    }

	    // create a SearchQuery
	    @PostMapping("/searchquerys")
	    @ResponseStatus(HttpStatus.CREATED)
	    public SearchQuery createSearchQuery(@Valid @RequestBody SearchQueryCreationDTO requestBody) {
	    	User user= userRepository.findById(requestBody.getUserId());
	        if (user != null) {
	        	if(user.getAuthentication().equals(requestBody.getAuthentication())) {
	        		SearchQuery searchQuery = new SearchQuery(requestBody.getSearchTerm());
	        		SearchQuery savedSearchQuery = searchQueryRepository.save(searchQuery);
		        	List<SearchQuery> searches =user.getSearchQuerys();
		        	searches.add(savedSearchQuery);
		        	user.setSearchQuerys(searches);
		        	userRepository.save(user);
			        return savedSearchQuery;
		    		
		    	}
		    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
		                String.format("Access Denied!"));
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", requestBody.getUserId()));
	        
	    }
	    
	    @PutMapping("/searchquerys/{id}")
	    @ResponseStatus(HttpStatus.CREATED)
	    public SearchQuery createSearchQuery(@PathVariable("id") long id, @Valid @RequestBody SearchQueryCreationDTO requestBody) {
	    	User user= userRepository.findById(requestBody.getUserId());
	        if (user != null) {
	        	if(user.getAuthentication().equals(requestBody.getAuthentication())) {
		        	List<SearchQuery> searches =user.getSearchQuerys();
		        	for(SearchQuery search:searches) {
		        		if (search.getId()==id){
		        			search.setSearchTerms(requestBody.getSearchTerm());
		        			searchQueryRepository.save(search);
		        			user.setSearchQuerys(searches);
		        			userRepository.save(user);

		        			return search;
		        		}
		        	}
		        	throw new ResponseStatusException(HttpStatus.NOT_FOUND,
			                String.format("SearchQuery with ID %s not found!", id));
		    		
		    	}
		    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
		                String.format("Access Denied!"));
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", requestBody.getUserId()));
	        
	    }
	       

	    // delete a SearchQuery
	    @DeleteMapping("/searchquerys/{id}")
	    public void deleteSearchQuery(@PathVariable("id") long id, @RequestHeader("name") String name, @RequestHeader("password") String password, @RequestHeader("id") long userId) {
	    	User user= userRepository.findById(userId);
	    	if (user != null) {
	    		Authentication sentData= new Authentication();
		    	sentData.setPassword(password);
		    	sentData.setUserName(name);
	    		if(user.getAuthentication().equals(sentData)) {
		        	List<SearchQuery> searches =user.getSearchQuerys();
		        	for(SearchQuery search:searches) {
		        		if (search.getId()==id){
		        			searches.remove(search);
		        			userRepository.save(user);
		        			searchQueryRepository.deleteById(id);
		        			return;
		        		}
		        	}
		        	throw new ResponseStatusException(HttpStatus.NOT_FOUND,
			                String.format("SearchQuery with ID %s not found!", id));	
		    	}
		    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
		                String.format("Access Denied!"));
	    	}
	    	throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", userId));
	    	
	    }
	    
	    @DeleteMapping("/searchquerys/")
	    public void deleteAllSearchQuery(@RequestHeader("name") String name, @RequestHeader("password") String password, @RequestHeader("id") long userId) {
	    	User user= userRepository.findById(userId);
	    	if (user != null) {
	    		Authentication sentData= new Authentication();
		    	sentData.setPassword(password);
		    	sentData.setUserName(name);
	    		if(user.getAuthentication().equals(sentData)) {
		        	List<SearchQuery> searches =user.getSearchQuerys();
		        	List<Long>searchesToDelete=new ArrayList<Long>();
		        	for(SearchQuery search:searches) {
		        		searchesToDelete.add(search.getId());
		        	}
		        	searches.clear();
		        	user.setSearchQuerys(searches);
		        	userRepository.save(user);
		        	for(Long search:searchesToDelete) {
		        		searchQueryRepository.deleteById(search);
		        	}
		        		
		        			
		        	
		        	return;
		    	}
		    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
		                String.format("Access Denied!"));
	    	}
	    	throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("User with ID %s not found!", userId));
	    	
	    }
}

