package com.movies.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Movie {

	private final String title;
	@NotNull
	private final TypeAndBonus movietype;
	@NotNull
	private final PriceType pricetype;

	@JsonCreator
	public Movie(@JsonProperty("title") String pTitle, @JsonProperty("movietype") TypeAndBonus fType,
			@JsonProperty("pricetype") PriceType pType) {
		title = pTitle;
		movietype = fType;
		pricetype = pType;
		MyValidator.validate(this);
	}

	@Size(min = 2, max = 60, message = "The title '${validatedValue}' must be between {min} and {max} characters long")
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	@JsonProperty("pricetype")
	public PriceType getPricetype() {
		return pricetype;
	}

	@JsonProperty("movietype")
	public TypeAndBonus getmovietype() {
		return movietype;
	}

	@JsonProperty("price")
	public int getPrice() {
		return pricetype.getPrice();
	}

	@JsonProperty("bonus")
	public int getBonus() {
		return movietype.getBonus();
	}

	public enum PriceType {
		PREMIUM(40), BASIC(20);

		public final int price;

		PriceType(int pPrice) {
			price = pPrice;
		}

		public int getPrice() {
			return price;
		}
	}

	public enum TypeAndBonus implements Serializable {
		NEW(2), REGULAR(1), OLD(1);

		private int bonus;

		TypeAndBonus(int pBonus) {
			bonus = pBonus;
		}

		public int getBonus() {
			return bonus;
		}
	}

}