/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */
package de.unistuttgart.iste.sopraws20.api.authentications;




import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.Column;



@Entity
@Table(name = "authentication")
public class Authentication {
		@Id
    	@GeneratedValue(strategy = GenerationType.AUTO)
	    private long id;
		@Size(min = 3)
		@NotNull
		@Column(unique=true)
	    private String userName;
		@Size(min = 10)
		@NotNull
	    private String password;

	    


		// empty default constructor is necessary for JPA
	    public Authentication() {}
	    
	    public Authentication(String userName, String password) {
	        this.userName = userName;
	        this.password = password;
	    }

	    public long getId() {
	        return id;
	    }
	    public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public void setId(long id) {
			this.id=id;
			
		}
	    
		public boolean equals(Object obj) {
			Authentication auth = (Authentication) obj;
				if (auth.getUserName().equals(this.userName)&&auth.getPassword().equals(this.password)) {
					return true;
				}
			
			return false;
			
		}



}