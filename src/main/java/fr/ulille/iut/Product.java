package fr.ulille.iut;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Product {
    private int id;
    private String color;
    private String text;
	private int order;
	private int amont;

    public Product(int id,int order, int amont, String color, String text) {
        this.id = id;
        this.order = order;
        this.amont = amont;
        this.color = color;
        this.text = text;
    }

    public Product() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getAmont() {
		return amont;
	}

	public void setAmont(int amont) {
		this.amont = amont;
	}

	public boolean equals(Object u) {
        return id==((Product) u).id || this.amont == ((Product) u).amont || this.order == ((Product) u).order || color.equals(((Product) u).color) || text.equals(((Product) u).text);
    }

    public String toString() {
        return id + " " + order + " " + amont  + " " + color + " " + text;
    }
}
