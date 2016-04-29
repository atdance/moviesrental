package com.movies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.movies.model.error.ApiException;

public class Movie {
	private final String title;
	private final TypeAndBonus movietype;
	private final PriceType pricetype;

	@JsonCreator
	public Movie(@JsonProperty("title") String pTitle, @JsonProperty("movietype") TypeAndBonus fType,
			@JsonProperty("pricetype") PriceType pType) {

		if (pTitle == null || pTitle.length() < Limits.EMPTY_TITLE.getCode()) {
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

	@JsonProperty("price")
	public int getPrice() {
		return pricetype.getPrice();
	}

	@JsonProperty("bonus")
	public int getBonus() {
		return movietype.getBonus();
	}

	@JsonProperty("pricetype")
	public PriceType getPricetype() {
		return pricetype;
	}

	@JsonProperty("movietype")
	public TypeAndBonus getmovietype() {
		return movietype;
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

	public enum TypeAndBonus {
		NEW(2), REGULAR(1), OLD(1);

		public int bonus;

		TypeAndBonus(int pBonus) {
			bonus = pBonus;
		}

		public int getBonus() {
			return bonus;
		}
	}

	@Override
	public String toString() {
		return "Movie [title=" + title + ", movietype=" + movietype + ", pricetype=" + pricetype + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((movietype == null) ? 0 : movietype.hashCode());
		result = prime * result + ((pricetype == null) ? 0 : pricetype.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Movie other = (Movie) obj;
		if (movietype != other.movietype) {
			return false;
		}
		if (pricetype != other.pricetype) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		return true;
	}

}