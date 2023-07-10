/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */
package de.unistuttgart.iste.sopraws20.api.searchQuerys;


import javax.validation.constraints.NotNull;
import de.unistuttgart.iste.sopraws20.api.authentications.Authentication;


public class SearchQueryCreationDTO {
	@NotNull
	private String searchTerm;
	@NotNull
	private long userId;
	@NotNull
	private Authentication authentication;
	
	public SearchQueryCreationDTO(){
		this.searchTerm= new String();
		this.authentication=new Authentication();
	}
	
	public SearchQueryCreationDTO(long userId, Authentication authentication, String searchTerm) {
		this.userId=userId;
		this.authentication=authentication;
		this.searchTerm=searchTerm;
	}
	
	public String getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(String searchTerms) {
		this.searchTerm = searchTerms;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Authentication getAuthentication() {
		return authentication;
	}
	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}
	
}
