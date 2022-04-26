package paint.Shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class Triangulo extends Forma
{
    private int NUMERO_LADOS;
    private double angulo;
    private String texto;
    
    public Triangulo(int numeroLados,boolean isFilled, boolean isGradient, boolean isDashedLine, int lineWidth, int dashLength, Color primaryColor, Color secondaryColor, Point p1, Point p2) {
        super(isFilled, isGradient, isDashedLine, lineWidth, dashLength, primaryColor, secondaryColor, p1, p2);
        this.NUMERO_LADOS = numeroLados;
        this.angulo = 0;
        this.texto = "";
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
        
        Point[] pontos = new Point[NUMERO_LADOS];
        
        for(int i=0; i<NUMERO_LADOS; i++)
        {
            double angulo = this.angulo + 2 * Math.PI / NUMERO_LADOS * ( i + 1 );
            pontos[i] = new Point( (int)(xc + r1 * Math.cos(angulo)) + (int)this.getP1().getX() , (int)(yc + r2 * Math.sin(angulo)) + (int)this.getP1().getY() );
        }
        
        int[] x = new int[NUMERO_LADOS];
        int[] y = new int[NUMERO_LADOS];
        
        for(int i=0;i<pontos.length;i++)
        {
            x[i] = (int)pontos[i].getX();
            y[i] = (int)pontos[i].getY();
        }
        
        if(this.isIsFilled())
        {
            g.fillPolygon(x,y,NUMERO_LADOS);
        }
        else
        {
            g.drawPolygon(x,y,NUMERO_LADOS);
        }
        
        if(!this.texto.equals(""))
        {
            gg.setPaint(new Color(0,0,0));
            int minX = (int)Math.min(this.getP1().getX(),this.getP2().getX());
            int minY = (int)Math.min(this.getP1().getY(),this.getP2().getY());
            g.setFont(new Font("serif",Font.PLAIN,20));
            g.drawString(this.texto,minX,minY - 20);
        }
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
        this.texto = "";
    }

    @Override
    public void soltarMouse(Point ponto) {
        this.texto = "";
        this.finish();
    }
    
    @Override
    public void rodaMouse(int rotacao)
    {
        this.angulo += 2 * Math.PI * (double)rotacao / 180;
    }
}
