package fr.ulille.iut;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Product {
    private int id;
	private int priority;
	private int amont;
    private String color;
    private String description;

    public Product(int id,int priority, int amont, String color, String description) {
        this.id = id;
        this.priority = priority;
        this.amont = amont;
        this.color = color;
        this.description = description;
    }
    
    public int getColumnCount() {
        return getClass().getDeclaredFields().length;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getAmont() {
		return amont;
	}

	public void setAmont(int amont) {
		this.amont = amont;
	}

	public boolean equals(Object u) {
        return id==((Product) u).id || this.amont == ((Product) u).amont || this.priority == ((Product) u).priority || color.equals(((Product) u).color) || description.equals(((Product) u).description);
    }

    public String toString() {
        return "(Object " + id + "): " + amont + description + ", color : " + color + ". Priority : " + priority;
    }
    
    public String[] toArray(){
    	String[] result = new String[getColumnCount()];
    	result[0] = ""+getId();
    	result[1] = ""+getPriority();
    	result[2] = ""+getAmont();
    	result[3] = getColor();
    	result[4] = getDescription();
    	return result;
    }
}
