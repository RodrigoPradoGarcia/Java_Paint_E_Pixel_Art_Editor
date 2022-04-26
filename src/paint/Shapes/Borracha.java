package paint.Shapes;

import java.awt.*;
import java.util.*;

public class Borracha extends Drawable
{
    public static Color corFundo = new Color(255,255,255);
    private final ArrayList<Point> pontos;

    public Borracha(boolean isFilled, boolean isGradient, boolean isDashedLine, int lineWidth, int dashLength, Color primaryColor, Color secondaryColor) {
        super(isFilled, isGradient, isDashedLine, lineWidth, dashLength, primaryColor, secondaryColor);
        this.pontos = new ArrayList();
    } 
    
    @Override
    public void draw(Graphics g) {
        
       Graphics2D gg = (Graphics2D)g;
       int[] pointsX = new int[this.pontos.size()];
       int[] pointsY = new int[this.pontos.size()];
       int cont = 0; 
       
       for(Point p : this.pontos)
       {
           pointsX[cont] = (int)p.getX();
           pointsY[cont] = (int)p.getY();
           cont++;
       }
       
       g.setColor(Borracha.corFundo);
       gg.setStroke(new BasicStroke(getLineWidth()));
       g.drawPolyline(pointsX, pointsY, cont);
    }

    @Override
    public void clicar(Point ponto) {
        
    }

    @Override
    public void arrastar(Point ponto) {
        this.pontos.add(ponto);
    }

    @Override
    public void moverMouse(Point ponto) {
        
    }

    @Override
    public void cliqueDireito(Point ponto) {
        
    }

    @Override
    public void soltarMouse(Point ponto) {
        this.finish();
    }
    
}
