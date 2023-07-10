/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */
package de.unistuttgart.iste.sopraws20.api.users;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import de.unistuttgart.iste.sopraws20.api.authentications.Authentication;
import de.unistuttgart.iste.sopraws20.api.rooms.Room;
import de.unistuttgart.iste.sopraws20.api.searchQuerys.SearchQuery;

@Entity
@Table(name = "users")
public class User {
		@Id
    	@GeneratedValue(strategy = GenerationType.AUTO)
	    private long id;
		@OneToOne(targetEntity=de.unistuttgart.iste.sopraws20.api.authentications.Authentication.class)
	    private Authentication authentication;
	    
		@OneToMany(targetEntity=de.unistuttgart.iste.sopraws20.api.searchQuerys.SearchQuery.class, cascade = CascadeType.ALL)
		@LazyCollection(LazyCollectionOption.FALSE)   //To avoid fetch problems in classes with two collections
		private List<SearchQuery> searchQuerys;
	    @OneToMany(targetEntity=de.unistuttgart.iste.sopraws20.api.rooms.Room.class)
	    @LazyCollection(LazyCollectionOption.FALSE)
		private List<Room> rooms;

	    
	    // empty default constructor is necessary for JPA
	    public User() {
	    	this.searchQuerys =new ArrayList<SearchQuery>();
	    	this.rooms= new ArrayList<Room>();
	    }
	    
	    public User(Authentication authentication, List<SearchQuery> query, List<Room> rooms) {
	    	this.authentication=authentication;
	    	this.searchQuerys=query;
	    	this.rooms=rooms;
	    }
	    
	    public User(Authentication authentication) {
	    	this.authentication=authentication;
	    	this.rooms=new ArrayList<Room>();
	    	this.searchQuerys= new ArrayList<SearchQuery>();
	    }
	    


		public List<SearchQuery> getSearchQuerys() {
			return searchQuerys;
		}

		public void setSearchQuerys(ArrayList<SearchQuery> searchQuerys) {
			this.searchQuerys = searchQuerys;
		}

		public List<Room> getRooms() {
			return rooms;
		}

		public void setRooms(ArrayList<Room> rooms) {
			this.rooms = rooms;
		}

		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id=id;
		}
		public Authentication getAuthentication() {
			return authentication;
		}

		public void setAuthentication(Authentication authentication) {
			this.authentication = authentication;
		}

		public void setSearchQuerys(List<SearchQuery> searchQuerys) {
			this.searchQuerys = searchQuerys;
		}

		public void setRooms(List<Room> rooms) {
			this.rooms = rooms;
		}
	  
}