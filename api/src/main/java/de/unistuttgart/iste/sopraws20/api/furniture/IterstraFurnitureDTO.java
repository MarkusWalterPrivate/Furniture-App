package de.unistuttgart.iste.sopraws20.api.furniture;

import javax.validation.constraints.NotNull;
import de.unistuttgart.iste.sopraws20.api.authentications.Authentication;

public class IterstraFurnitureDTO {
		@NotNull
		private long itestraId;
		@NotNull
		private long userId;
		@NotNull
		private long roomId;
		@NotNull
		private Authentication authentication;
		
		public IterstraFurnitureDTO() {	
		}
		
		public IterstraFurnitureDTO (long userId, long roomId, Authentication authentication, long itestraId) {

			this.userId=userId;
			this.roomId=roomId;
			this.authentication=authentication;
			this.itestraId=itestraId;
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

		public long getItestraId() {
			return itestraId;
		}

		public void setItestraId(long itestraId) {
			this.itestraId = itestraId;
		}

		
		
}
