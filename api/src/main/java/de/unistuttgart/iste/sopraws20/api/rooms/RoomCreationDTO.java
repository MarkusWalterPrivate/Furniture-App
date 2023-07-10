/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */
package de.unistuttgart.iste.sopraws20.api.rooms;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import de.unistuttgart.iste.sopraws20.api.authentications.Authentication;
import de.unistuttgart.iste.sopraws20.api.furniture.Furniture;


public class RoomCreationDTO {


		@NotEmpty
	    private String name;
	    private List<Furniture> furnitureItems;
	    @NotNull
	    @Min(value =0)
	    private long userId;
	    @NotNull
	    private Authentication authentication;
	    @NotNull
	    private String imgURL;


		 


		// empty default constructor is necessary for JPA
	    public RoomCreationDTO() {
	    	this.furnitureItems=new ArrayList<Furniture>();
	    	this.authentication=new Authentication();
	    }
	    
	    public RoomCreationDTO(String name, List<Furniture> furnitureItems) {
	        this.name = name;
	        this.furnitureItems = furnitureItems;
	    }
	    public RoomCreationDTO (String name) {
	    	this.name=name;
	    	this.furnitureItems=new ArrayList<Furniture>();
	    }
	    
	    public long getUserId() {
			return userId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setUserId(long id) {
			this.userId=id;
			
		}
		
		public Authentication getAuthentication() {
			return authentication;
		}

		public void setAuthentication(Authentication authentication) {
			this.authentication = authentication;
		}
		public List<Furniture> getFurnitureItems() {
			return furnitureItems;
		}

		public void setFurnitureItems(List<Furniture> furnitureItems) {
			this.furnitureItems = furnitureItems;
		}

		public String getImgURL() {
			return imgURL;
		}

		public void setImgURL(String imgURL) {
			this.imgURL = imgURL;
		}

}
