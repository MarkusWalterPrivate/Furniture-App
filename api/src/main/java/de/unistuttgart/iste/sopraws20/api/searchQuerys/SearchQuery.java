/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */
package de.unistuttgart.iste.sopraws20.api.searchQuerys;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "searchQuerys")
public class SearchQuery {
		@Id
    	@GeneratedValue(strategy = GenerationType.AUTO)
	    private long id;

		@Column(columnDefinition = "TEXT")
		private String searchTerm;
	    
	    // empty default constructor is necessary for JPA
	    public SearchQuery() {
	    }
	    
	    public SearchQuery(String searchTerm) {
	        this.searchTerm = searchTerm;
	        
	    }

	    public long getId() {
	        return id;
	    }
	    public void setId(long id) {
			this.id=id;
		}

		public String getSearchTerms() {
			return searchTerm;
		}

		public void setSearchTerms(String list) {
			this.searchTerm = list;
		}




}