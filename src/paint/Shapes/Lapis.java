package paint.Shapes;

import java.awt.*;
import java.util.*;

public class Lapis extends Drawable
{
    private final ArrayList<Point> pontos;

    public Lapis(boolean isFilled, boolean isGradient, boolean isDashedLine, int lineWidth, int dashLength, Color primaryColor, Color secondaryColor)
    {
        super(isFilled, isGradient, isDashedLine, lineWidth, dashLength, primaryColor, secondaryColor);
        this.pontos = new ArrayList();
    }
    
    public void addPoint(Point p)
    {
        this.pontos.add(p);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D gg = (Graphics2D) g;
        
        int[] pontosX = new int[this.pontos.size()];
        int[] pontosY = new int[this.pontos.size()];
        
        int cont = 0;
        
        for(Point p : this.pontos)
        {
            pontosX[cont] = (int)p.getX();
            pontosY[cont] = (int)p.getY();
            cont++;
        }
        
        if(this.isIsGradient())
        {
            gg.setPaint(new GradientPaint(new Point(0,0),this.getPrimaryColor(),new Point(100,100),this.getSecondaryColor(),true));
        }
        else
        {
            gg.setPaint(this.getPrimaryColor());
        }
        
        if(this.isIsDashedLine())
        {
            float[] dashes = {this.getDashLength()};
            gg.setStroke(new BasicStroke(this.getLineWidth(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,dashes,dashes[0]));
        }
        else
        {
            gg.setStroke(new BasicStroke(this.getLineWidth()));
        }
        
        gg.drawPolyline(pontosX, pontosY, this.pontos.size());
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
    public void soltarMouse(Point ponto)
    {
        this.finish();
    }
}
