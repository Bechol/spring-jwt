package ru.bechol.jwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.*;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "profiles")
@NoArgsConstructor
public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	long id;
	@Column(name = "first_name")
	String firstName;
	@Column(name = "last_name")
	String lastName;
	LocalDate dob;
	String about;
	@Column(name = "delete_waiting")
	boolean deleteWaiting; //todo scheduler: block profile and user after today date is blockDate
	@Column(name = "block_time")
	LocalDateTime blockTime;
	@Column(name = "photo_link")
	String photoLink;
	@OneToOne(mappedBy = "profile")
	User user;

}
