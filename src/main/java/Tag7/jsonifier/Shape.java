package Tag7.jsonifier;

import java.util.Objects;
import com.google.gson.Gson;

public class Shape {
    private String type;
    private String color;
    private String fillColor;
    private double lineWidth;
    private double positionX;
    private double positionY;
    private double scaleX;
    private double scaleY;
    private double rotation;
    private Shape content;

    public Shape(String type, String color, String fillColor, double lineWidth,
                 double positionX, double positionY, double scaleX, double scaleY,
                 double rotation, Shape content) {
        this.type = type;
        this.color = color;
        this.fillColor = fillColor;
        this.lineWidth = lineWidth;
        this.positionX = positionX;
        this.positionY = positionY;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rotation = rotation;
        this.content = content;
    }

    // Getters and setters

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return Double.compare(shape.lineWidth, lineWidth) == 0 &&
                Double.compare(shape.positionX, positionX) == 0 &&
                Double.compare(shape.positionY, positionY) == 0 &&
                Double.compare(shape.scaleX, scaleX) == 0 &&
                Double.compare(shape.scaleY, scaleY) == 0 &&
                Double.compare(shape.rotation, rotation) == 0 &&
                Objects.equals(type, shape.type) &&
                Objects.equals(color, shape.color) &&
                Objects.equals(fillColor, shape.fillColor) &&
                Objects.equals(content, shape.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color, fillColor, lineWidth, positionX, positionY,
                scaleX, scaleY, rotation, content);
    }

    @Override
    public String toString() {
        return "Shape{" +
                "type='" + type + '\'' +
                ", color='" + color + '\'' +
                ", fillColor='" + fillColor + '\'' +
                ", lineWidth=" + lineWidth +
                ", positionX=" + positionX +
                ", positionY=" + positionY +
                ", scaleX=" + scaleX +
                ", scaleY=" + scaleY +
                ", rotation=" + rotation +
                ", content=" + content +
                '}';
    }

    public Shape setContent(Shape content) {
        this.content = content;
        return null;
}
public Shape getContent() {
        return content;
}

public Shape setRotation(double rotation) {
        this.rotation = rotation;
        return null;
}
public double getRotation() {
        return rotation;
}
public String toJson() {
    Gson gson = new Gson();
    return gson.toJson(this);
    }
}

