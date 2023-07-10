/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */

package de.unistuttgart.iste.sopraws20.api.rooms;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import de.unistuttgart.iste.sopraws20.api.furniture.Furniture;

@Entity
@Table(name = "rooms")
public class Room {
		@Id
    	@GeneratedValue(strategy = GenerationType.AUTO)
	    private long id;
		@Size(min = 1)
	    private String name;
	    @ManyToMany(targetEntity=de.unistuttgart.iste.sopraws20.api.furniture.Furniture.class)
	    @LazyCollection(LazyCollectionOption.FALSE)
	    private List<Furniture> furnitureItems;
	    private String imgURL;
	    


		// empty default constructor is necessary for JPA
	    public Room() {
	    	this.furnitureItems=new ArrayList<Furniture>();
	    }
	    
	    public Room(String name, List<Furniture> furnitureItems) {
	        this.name = name;
	        this.furnitureItems = furnitureItems;
	    }
	    public Room (String name) {
	    	this.name=name;
	    	this.furnitureItems=new ArrayList<Furniture>();
	    }
	    
	    public long getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}


		public List<Furniture> getFurnitureItems() {
			return furnitureItems;
		}

		public void setFurnitureItems(List<Furniture> list) {
			this.furnitureItems = list;
		}

		public void setId(long id) {
			this.id=id;
			
		}

		public String getImgURL() {
			return imgURL;
		}

		public void setImgURL(String imgURL) {
			this.imgURL = imgURL;
		}


}
