package paint.Shapes;

import java.awt.*;

public abstract class Forma extends Drawable
{
    private Point p1;
    private Point p2;

    public Forma(boolean isFilled, boolean isGradient, boolean isDashedLine, int lineWidth, int dashLength, Color primaryColor, Color secondaryColor,Point p1, Point p2) {
        super(isFilled, isGradient, isDashedLine, lineWidth, dashLength, primaryColor, secondaryColor);
        this.p1 = p1;
        this.p2 = p2;
    }   

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }
    
    @Override
    public abstract void draw(Graphics g);
}
