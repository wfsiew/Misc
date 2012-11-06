package app.bean;

public class ProductSetBean {
	private String code;
	private String type;
	private String priceType;
	private String itemQuantity;
	private String childCode;
	private String productGrouping;
	private String childQuantity;
	private String childRetailPrice;
	private String childPricePerUnit;
	private String childEVPerUnit;
	private String totalChildPrice;
	private String totalChildEV;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code == null ? code : code.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? type : type.trim();
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		if (priceType != null && "promo".equalsIgnoreCase(priceType))
			this.priceType = "P";
		
		else
			this.priceType = priceType.trim();
	}

	public String getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(String itemQuantity) {
		this.itemQuantity = itemQuantity == null ? itemQuantity : itemQuantity.trim();
	}

	public String getChildCode() {
		return childCode;
	}

	public void setChildCode(String childCode) {
		this.childCode = childCode == null ? childCode : childCode.trim();
	}

	public String getProductGrouping() {
		return productGrouping;
	}

	public void setProductGrouping(String productGrouping) {
		this.productGrouping = productGrouping == null ? productGrouping : productGrouping.trim();
	}

	public String getChildQuantity() {
		return childQuantity;
	}

	public void setChildQuantity(String childQuantity) {
		this.childQuantity = childQuantity == null ? childQuantity : childQuantity.trim();
	}

	public String getChildRetailPrice() {
		return childRetailPrice;
	}

	public void setChildRetailPrice(String childRetailPrice) {
		this.childRetailPrice = childRetailPrice == null ? childRetailPrice : childRetailPrice.trim();
	}

	public String getChildPricePerUnit() {
		return childPricePerUnit;
	}

	public void setChildPricePerUnit(String childPricePerUnit) {
		this.childPricePerUnit = childPricePerUnit == null ? childPricePerUnit : childPricePerUnit.trim();
	}

	public String getChildEVPerUnit() {
		return childEVPerUnit;
	}

	public void setChildEVPerUnit(String childEVPerUnit) {
		this.childEVPerUnit = childEVPerUnit == null ? childEVPerUnit : childEVPerUnit.trim();
	}

	public String getTotalChildPrice() {
		return totalChildPrice;
	}

	public void setTotalChildPrice(String totalChildPrice) {
		this.totalChildPrice = totalChildPrice == null ? totalChildPrice : totalChildPrice.trim();
	}

	public String getTotalChildEV() {
		return totalChildEV;
	}

	public void setTotalChildEV(String totalChildEV) {
		this.totalChildEV = totalChildEV == null ? totalChildEV : totalChildEV.trim();
	}
}