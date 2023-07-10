/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */
package de.unistuttgart.iste.sopraws20.api.furniture;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "furniture")
public class Furniture {
		@Id
    	@GeneratedValue(strategy = GenerationType.AUTO)
	    private long id;
		private String name;
		private long itestraId;
		private String imgURL;

		private String color;
	    private String material;
	    /*
	     * ????? What else, depends to be seen
	     */
	    
	    // empty default constructor is necessary for JPA
	    public Furniture() {}
	    
	    public Furniture(String name, String color, String material) {
	    	this.name=name;
	    	this.color=color;
	      	this.material=material;

	    }
	    public Furniture(long itestraId) {
	    	this.itestraId=itestraId;
	    }

		public void setId(long id) {
			this.id=id;
			
		}
		public void setItestraId(long id) {
			this.itestraId=id;
		}
		public long getItestraId() {
			return itestraId;
		}
		   public String getColor() {
				return color;
			}

			public void setColor(String color) {
				this.color = color;
			}

			public String getMaterial() {
				return material;
			}

			public void setMaterial(String material) {
				this.material = material;
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

			public String getImgURL() {
				return imgURL;
			}

			public void setImgURL(String imgURL) {
				this.imgURL = imgURL;
			}

}
