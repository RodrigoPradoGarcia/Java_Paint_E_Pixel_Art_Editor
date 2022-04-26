package paint.Painel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import paint.Shapes.*;

public final class Painel extends JPanel implements Serializable
{    
    private Stack<Drawable> formas;
    private Color fundo;
    
    public Painel(Color fundo)
    {
        this.formas = new Stack<>();
        this.fundo = fundo;
        this.setBackground(this.fundo);
    }
    
    public Stack<Drawable> getStackShapes()
    {
        return this.formas;
    }
    
    public void trocarCorFundo(Color cor)
    {
        this.fundo = cor;
        this.setBackground(this.fundo);
    }
    
    public Color getCorFundo()
    {
        return this.fundo;
    }
    
    public void clicar(Drawable shape,Point clique)
    {
        if(this.formas.isEmpty() || this.formas.peek().isDone())
        {
            this.addShape(shape);
            this.formas.peek().clicar(clique);
        }
        else
        {
            this.formas.peek().clicar(clique);
        }
        this.repaint();
    }
    
    public void moverMouse(Point clique)
    {
        if(!this.formas.isEmpty() && !this.formas.peek().isDone())
        {
            this.formas.peek().moverMouse(clique);
        }
        this.repaint();
    }
    
    public void arrastar(Point p)
    {
        if(!this.formas.isEmpty() && !this.formas.peek().isDone())
        {
            this.formas.peek().arrastar(p);
        }
        this.repaint();
    }
    
    public void cliqueDireito(Drawable shape,Point p)
    {
        if(!this.formas.isEmpty() && !this.formas.peek().isDone())
        {
            this.formas.peek().cliqueDireito(p);
        }
        else
        {
            this.addShape(shape);
        }
        this.repaint();
    }
    
    public void soltarMouse(Point ponto)
    {
        if(!this.formas.isEmpty() && !this.formas.peek().isDone())
        {
            this.formas.peek().soltarMouse(ponto);
            this.repaint();
        }
    }
    
    public void rodaMouse(int rotacao)
    {
        if(!this.formas.isEmpty() && !this.formas.peek().isDone())
        {
            this.formas.peek().rodaMouse(rotacao);
            this.repaint();
        }
    }
    
    public void addShape(Drawable shape)
    {
        this.formas.push(shape);
        this.repaint();
    }
    
    public void undo()
    {
        if(!this.formas.isEmpty())
        {
            this.formas.pop();
            this.repaint();
        }
    }
    
    public void clear()
    {
        this.formas = new Stack<>();
        this.repaint();
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        this.setBackground(this.fundo);
        Borracha.corFundo = this.fundo;
        
        for(Drawable forma : formas)
        {
            forma.draw(g);
        }
    }
}
