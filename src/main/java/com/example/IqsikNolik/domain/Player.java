package com.example.IqsikNolik.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@Entity
public class Player {

	@GeneratedValue(strategy = IDENTITY)
	@Id
	private Long id;

	private String name;

	private String hashedPassword;

	@OneToMany(fetch = LAZY, mappedBy = "playerX")
	private List<Game> gamesAsX = new ArrayList<>();

	@OneToMany(fetch = LAZY, mappedBy = "playerO")
	private List<Game> gamesAsO = new ArrayList<>();
}
