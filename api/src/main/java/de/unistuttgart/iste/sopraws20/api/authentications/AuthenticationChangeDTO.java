/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */
package de.unistuttgart.iste.sopraws20.api.authentications;

import javax.validation.constraints.NotNull;

public class AuthenticationChangeDTO {
	@NotNull
	private Authentication oldAuthentication;
	@NotNull
	private Authentication newAuthentication;
	
	
	public AuthenticationChangeDTO( Authentication oldAuthentication, Authentication newAuthentication) {

		this.oldAuthentication=oldAuthentication;
		this.newAuthentication=newAuthentication;
	}

	public Authentication getOldAuthentication() {
		return oldAuthentication;
	}
	public void setOldAuthentication(Authentication oldAuthentication) {
		this.oldAuthentication = oldAuthentication;
	}
	public Authentication getNewAuthentication() {
		return newAuthentication;
	}
	public void setNewAuthentication(Authentication newAuthentication) {
		this.newAuthentication = newAuthentication;
	}


}
