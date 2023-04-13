package photos.control;

import java.io.Serializable;

public class TagCategory implements Serializable {
	private static final long serialVersionUID = 6L;
	private String categoryName;
	
	public class Tag implements Serializable {
		private static final long serialVersionUID = 5L;
		private String tagValue;
		
		public Tag(String value)
		{
			this.tagValue = value;
		}
		public String getTagValue() {
			return tagValue;
		}

		public void setTagValue(String tagValue) {
			this.tagValue = tagValue;
		}
		
		public String toString() {
			return TagCategory.this.toString() + " : " + this.tagValue;
		}
	}
	
	public TagCategory(String name)
	{
		this.setCategoryName(name);
	}
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String toString() {
		return this.categoryName;
	}
	
}
