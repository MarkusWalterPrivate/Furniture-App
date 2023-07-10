/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */
package de.unistuttgart.iste.sopraws20.api.users;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import de.unistuttgart.iste.sopraws20.api.authentications.Authentication;


public class UserRequestDTO {
	@NotNull
	@Min(value=0)
	private long userId;
	@NotNull
	private Authentication authentication;
	
	
	public UserRequestDTO(long userId, Authentication authentication) {
		this.userId=userId;
		this.authentication=authentication;
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
