package paint.Shapes;

import java.awt.*;
import java.awt.geom.*;

public class Line extends Forma
{

    public Line(boolean isFilled, boolean isGradient, boolean isDashedLine, int lineWidth, int dashLength, Color primaryColor, Color secondaryColor, Point p1, Point p2) {
        super(isFilled, isGradient, isDashedLine, lineWidth, dashLength, primaryColor, secondaryColor, p1, p2);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D gg = (Graphics2D)g;
        
        if(this.isIsGradient())
        {
            int gwidth = (int)( this.getP1().getX() - this.getP2().getX() );
            int gheight = (int)( this.getP1().getY() - this.getP2().getY() );
            gg.setPaint(new GradientPaint(this.getP1(),this.getPrimaryColor(),new Point((int)this.getP1().getX() + gwidth,(int)this.getP1().getY() + gheight),this.getSecondaryColor(),true));
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
        
        gg.draw(new Line2D.Double(this.getP1(),this.getP2()));
    }

    @Override
    public void clicar(Point ponto) {
        
    }

    @Override
    public void arrastar(Point ponto) {
        this.setP2(ponto);
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
