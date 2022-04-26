package paint.Shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

public abstract class Drawable implements Serializable
{
    private boolean done;
    private boolean isFilled;
    private boolean isGradient;
    private boolean isDashedLine;
    private int lineWidth;
    private int dashLength;
    private Color primaryColor;
    private Color secondaryColor;

    public Drawable(boolean isFilled, boolean isGradient, boolean isDashedLine, int lineWidth, int dashLength, Color primaryColor, Color secondaryColor) {
        this.done = false;
        this.isFilled = isFilled;
        this.isGradient = isGradient;
        this.isDashedLine = isDashedLine;
        this.lineWidth = lineWidth;
        this.dashLength = dashLength;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    } 
    
    public boolean isIsFilled() {
        return isFilled;
    }

    public void setIsFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    public boolean isIsGradient() {
        return isGradient;
    }

    public void setIsGradient(boolean isGradient) {
        this.isGradient = isGradient;
    }

    public boolean isIsDashedLine() {
        return isDashedLine;
    }

    public void setIsDashedLine(boolean isDashedLine) {
        this.isDashedLine = isDashedLine;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getDashLength() {
        return dashLength;
    }

    public void setDashLength(int dashLength) {
        this.dashLength = dashLength;
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(Color primaryColor) {
        this.primaryColor = primaryColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(Color secondaryColor) {
        this.secondaryColor = secondaryColor;
    }  
    
    public boolean isDone()
    {
        return this.done;
    }
    
    public void finish()
    {
        this.done = true;
    }
    
    public abstract void draw(Graphics g);
    public abstract void clicar(Point ponto);
    public abstract void arrastar(Point ponto);
    public abstract void moverMouse(Point ponto);
    public abstract void cliqueDireito(Point ponto);
    public abstract void soltarMouse(Point ponto);
    public void rodaMouse(int rotacao){};
}
