import java.io.Serializable;
import java.util.Objects;

public class MMPoint implements Serializable {
    private int x;
    private int y;

    public MMPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MMPoint mmPoint = (MMPoint) object;
        return x == mmPoint.x && y == mmPoint.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}