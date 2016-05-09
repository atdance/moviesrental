package com.movies.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.movies.model.error.ApiException;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Movie {
	private final String title;
	private final TypeAndBonus movietype;
	private final PriceType pricetype;

	@JsonCreator
	public Movie(@JsonProperty("title") String pTitle, @JsonProperty("movietype") TypeAndBonus fType,
			@JsonProperty("pricetype") PriceType pType) {

		if (pTitle == null || pTitle.length() < Limits.EMPTY_TITLE.getLimit()) {
			throw new ApiException(Limits.EMPTY_TITLE);
		}
		title = pTitle;
		movietype = fType;
		pricetype = pType;
	}

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

		// @JsonCreator
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
