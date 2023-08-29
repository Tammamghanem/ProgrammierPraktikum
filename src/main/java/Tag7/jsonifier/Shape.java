package Tag7.jsonifier;

public class Shape {
	Type type;
	String color;
	String fillColor;
	Integer lineWidth;
	Integer positionX;
	Integer positionY;
	Integer scaleX;
	Integer scaleY;
	Integer rotation;
	Shape content;

	public Shape(Type type,
	String color,
	String fillColor,
	Integer lineWidth,
	Integer positionX,
	Integer positionY,
	Integer scaleX,
	Integer scaleY, Integer rotation){
		this.type = type;
		this.content = null;
		this.color = color;
		this. fillColor = fillColor;
		this.lineWidth = lineWidth;
		this. positionX = positionX;
		this.positionY = positionY;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.rotation = rotation;
	}

	Shape getContent(){
		return this.content;
	}

	void setContent(Shape content){
		this.content = content;
	}

	String toJson(){
		String contentJson = this.content == null ? null : this.content.toJson();
		return "{"+"\"type\":\""+this.type.name().toLowerCase()+"\",\"color\":\""+this.color+"\", \"fillColor\":\""+this.fillColor+"\", \"lineWidth\":"+this.lineWidth+", \"positionX\":"+this.positionX+", \"positionY\":"+this.positionY+", \"scaleX\":"+this.scaleX+",\"scaleY\":"+this.scaleY+", \"rotation\":"+this.rotation+", \"content\":"+contentJson+"}";
	}
}
