package de.unistuttgart.iste.sopraws20.api.furniture;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import de.unistuttgart.iste.sopraws20.api.authentications.Authentication;

public class FurnitureCreationDTO {
		@NotEmpty
		private String name;
		@NotEmpty
		private String color;
		@NotEmpty
		private String material;
		@NotNull
		private long userId;
		@NotNull
		private long roomId;
		@NotNull
		private Authentication authentication;
		@NotNull
		private String imgURL;
		
		public FurnitureCreationDTO() {	
		}
		
		public FurnitureCreationDTO(String name, String color, String material,  long userId, long roomId, Authentication authentication) {
			this.name=name;
			this.color=color;
			this.material=material;
			this.userId=userId;
			this.roomId=roomId;
			this.authentication=authentication;

		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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


		public long getUserId() {
			return userId;
		}

		public void setUserId(long userId) {
			this.userId = userId;
		}

		public long getRoomId() {
			return roomId;
		}

		public void setRoomId(long roomId) {
			this.roomId = roomId;
		}

		public Authentication getAuthentication() {
			return authentication;
		}

		public void setAuthentication(Authentication authentication) {
			this.authentication = authentication;
		}

		public String getImgURL() {
			return imgURL;
		}

		public void setImgURL(String imgURL) {
			this.imgURL = imgURL;
		}


		
		
}
