package paint.Shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class Star extends Forma
{

    public Star(boolean isFilled, boolean isGradient, boolean isDashedLine, int lineWidth, int dashLength, Color primaryColor, Color secondaryColor, Point p1, Point p2) {
        super(isFilled, isGradient, isDashedLine, lineWidth, dashLength, primaryColor, secondaryColor, p1, p2);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D gg = (Graphics2D) g;
        
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
            gg.setStroke(new BasicStroke(this.getLineWidth(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,dashes,0));
        }
        else
        {
            gg.setStroke(new BasicStroke(this.getLineWidth()));
        }
        
        double r1 = Math.abs( this.getP2().getX() - this.getP1().getX() )/2;
        double r2 = Math.abs( this.getP2().getY() - this.getP1().getY() )/2;
        double xc = ( this.getP2().getX() - this.getP1().getX() )/2;
        double yc = ( this.getP2().getY() - this.getP1().getY() )/2;
        
        Point[] pontos = new Point[5];
        
        for(int i=0; i<5; i++)
        {
            double angulo =  - Math.PI / 10 + 2 * Math.PI / 5 * ( i + 1 );
            pontos[i] = new Point( (int)(xc + r1 * Math.cos(angulo)) + (int)this.getP1().getX() , (int)(yc + r2 * Math.sin(angulo)) + (int)this.getP1().getY() );
        }
        
        int[] x = new int[5];
        int[] y = new int[5];
        
        x[0] = (int)pontos[2].getX();
        x[1] = (int)pontos[0].getX();
        x[2] = (int)pontos[3].getX();
        x[3] = (int)pontos[1].getX();
        x[4] = (int)pontos[4].getX();
        y[0] = (int)pontos[2].getY();
        y[1] = (int)pontos[0].getY();
        y[2] = (int)pontos[3].getY();
        y[3] = (int)pontos[1].getY();
        y[4] = (int)pontos[4].getY();
        
        if(this.isIsFilled())
        {
            g.fillPolygon(x,y,5);
            double ar1 = (pontos[4].getY() - pontos[2].getY()) / (pontos[4].getX() - pontos[2].getX());
            double br1 = pontos[4].getY() - ar1 * pontos[4].getX();
            double ar2 = (pontos[3].getY() - pontos[1].getY()) / (pontos[3].getX() - pontos[1].getX());
            double br2 = pontos[1].getY() - ar2 * pontos[1].getX();
            int xis = (int)( ( br2 - br1 ) / ( ar1 - ar2 ) );
            int why = (int)( ar1 * xis + br1 );      
            int[] xxx = {(int)pontos[3].getX(),(int)pontos[0].getX(),(int)pontos[2].getX(),(int)xis};
            int[] yyy = {(int)pontos[3].getY(),(int)pontos[0].getY(),(int)pontos[2].getY(),(int)why};
            gg.fillPolygon(xxx, yyy, 4);
        }
        else
        {
            g.drawPolygon(x,y,5);
        }           
    }

    @Override
    public void clicar(Point ponto) {
        this.setP1(ponto);
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
    public void soltarMouse(Point ponto) {
        this.finish();
    }
    
}
