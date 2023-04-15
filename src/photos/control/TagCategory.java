package photos.control;

import java.io.Serializable;

/**
 * Represents a category of tags that can be associated with an object.
 * 
 * @author Huzaif Mansuri, Pavitra Patel
 */
public class TagCategory implements Serializable {
	private static final long serialVersionUID = 6L;
	private String categoryName;
	private boolean singular;

	/**
	 * Represents a single tag value within the tag category.
	 */
	public class Tag implements Serializable {
		private static final long serialVersionUID = 5L;
		private String tagValue;

		/**
		 * Constructs a new Tag object with the specified value.
		 *
		 * @param value the value of the tag
		 */
		public Tag(String value) {
			this.tagValue = value;
		}

		/**
		 * Returns the value of the tag.
		 *
		 * @return the tag value
		 */
		public String getTagValue() {
			return tagValue;
		}

		/**
		 * Sets the value of the tag.
		 *
		 * @param tagValue the new tag value
		 */
		public void setTagValue(String tagValue) {
			this.tagValue = tagValue;
		}

		/**
		 * Returns a string representation of the Tag object.
		 *
		 * @return a string representation of the Tag object
		 */
		public String toString() {
			return TagCategory.this.toString() + " : " + this.tagValue;
		}
	}

	/**
	 * Constructs a new TagCategory object with the specified name.
	 *
	 * @param name the name of the tag category
	 */
	public TagCategory(String name) {
		this.setCategoryName(name);
	}

	/**
	 * Constructs a new TagCategory object with the specified name and singularity.
	 *
	 * @param name the name of the tag category
	 * @param singular whether the tag category is singular or plural
	 */
	public TagCategory(String name, Boolean singular) {
		this.setCategoryName(name);
		this.singular = singular;
	}

	/**
	 * Returns the name of the tag category.
	 *
	 * @return the name of the tag category
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * Sets the name of the tag category.
	 *
	 * @param categoryName the new name of the tag category
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * Returns a string representation of the TagCategory object.
	 *
	 * @return a string representation of the TagCategory object
	 */
	public String toString() {
		return this.categoryName;
	}

	/**
	 * Returns whether the tag category is singular or plural.
	 *
	 * @return true if the tag category is singular, false if it is plural
	 */
	public boolean isSinglular() {
		return singular;
	}

	/**
	 * Sets whether the tag category is singular or plural.
	 *
	 * @param singular true if the tag category is singular, false if it is plural
	 */
	public void setSinglular(boolean singular) {
		this.singular = singular;
	}

}
