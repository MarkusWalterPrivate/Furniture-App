/*
 *  Authored by Albert Walker and Markus Walter aka Team 17 at 2021/22 Sopra at University of Stuttgart
 */
package de.unistuttgart.iste.sopraws20.api.rooms;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    Room findById(long id);
}
