package paint;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.LineBorder;

import paint.Shapes.*;
import paint.Painel.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

class FinalString
{
    public final String string;
    public final int index;
    private static int cont = 0;

    public FinalString(String str)
    {
        this.string = str;
        this.index = FinalString.cont++;
        
    }
}

public class Paint extends javax.swing.JFrame {

    public Paint(Painel des) {
        initComponents();
        renderColors();
        renderCoresRecentes();
        addSelectShapeEvent();
        this.desenho = des;
        this.panelDesenho.add(desenho);
        this.desenho.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == 1)
                {
                    switch(Paint.this.shape)
                    {
                        case "Desenho Livre" -> {
                            Paint.this.desenho.clicar(new Lapis(Paint.this.isFilled,Paint.this.isGradient,Paint.this.isDashedLine,Paint.this.lineWidth,Paint.this.dashLength,Paint.this.btnCorPrimaria.getBackground(),Paint.this.btnCorSecundaria.getBackground()),e.getPoint());
                        }
                        case "Retângulo" -> {
                            Paint.this.desenho.clicar(new paint.Shapes.Rectangle(Paint.this.isFilled,Paint.this.isGradient,Paint.this.isDashedLine,Paint.this.lineWidth,Paint.this.dashLength,Paint.this.btnCorPrimaria.getBackground(),Paint.this.btnCorSecundaria.getBackground(),e.getPoint(),e.getPoint()),e.getPoint());
                        }
                        case "Círculo" -> {
                            Paint.this.desenho.clicar(new paint.Shapes.Oval(Paint.this.isFilled,Paint.this.isGradient,Paint.this.isDashedLine,Paint.this.lineWidth,Paint.this.dashLength,Paint.this.btnCorPrimaria.getBackground(),Paint.this.btnCorSecundaria.getBackground(),e.getPoint(),e.getPoint()),e.getPoint());
                        }
                        case "Linha" -> {
                            Paint.this.desenho.clicar(new paint.Shapes.Line(Paint.this.isFilled,Paint.this.isGradient,Paint.this.isDashedLine,Paint.this.lineWidth,Paint.this.dashLength,Paint.this.btnCorPrimaria.getBackground(),Paint.this.btnCorSecundaria.getBackground(),e.getPoint(),e.getPoint()),e.getPoint());
                        }
                        case "Polylinha" -> {
                            Paint.this.desenho.clicar(new paint.Shapes.Polylinha(Paint.this.isFilled,Paint.this.isGradient,Paint.this.isDashedLine,Paint.this.lineWidth,Paint.this.dashLength,Paint.this.btnCorPrimaria.getBackground(),Paint.this.btnCorSecundaria.getBackground()),e.getPoint());
                        }
                        case "Estrela" -> {
                            Paint.this.desenho.clicar(new paint.Shapes.Star(Paint.this.isFilled,Paint.this.isGradient,Paint.this.isDashedLine,Paint.this.lineWidth,Paint.this.dashLength,Paint.this.btnCorPrimaria.getBackground(),Paint.this.btnCorSecundaria.getBackground(),e.getPoint(),e.getPoint()),e.getPoint());
                        }
                        case "Polígono" -> {
                            Paint.this.desenho.clicar(new paint.Shapes.Triangulo(Paint.this.numeroLados,Paint.this.isFilled,Paint.this.isGradient,Paint.this.isDashedLine,Paint.this.lineWidth,Paint.this.dashLength,Paint.this.btnCorPrimaria.getBackground(),Paint.this.btnCorSecundaria.getBackground(),e.getPoint(),e.getPoint()),e.getPoint());
                        }
                    }
                }
                else if(e.getButton() == 3)
                {
                    Paint.this.desenho.cliqueDireito(new Borracha(Paint.this.isFilled,Paint.this.isGradient,Paint.this.isDashedLine,Paint.this.lineWidth,Paint.this.dashLength,Paint.this.btnCorPrimaria.getBackground(),Paint.this.btnCorSecundaria.getBackground()),e.getPoint());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Paint.this.desenho.soltarMouse(e.getPoint());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        
        this.desenho.addMouseMotionListener(new MouseMotionListener(){
            @Override
            public void mouseDragged(MouseEvent e) {
                Paint.this.desenho.arrastar(e.getPoint());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Paint.this.lblMouseCoords.setText("("+e.getX()+","+e.getY()+")");
                Paint.this.desenho.moverMouse(e.getPoint());
            }
            
        });
        
        this.desenho.addMouseWheelListener(new MouseWheelListener(){
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                Paint.this.desenho.rodaMouse(e.getWheelRotation());
            }
        });
    }
    
    private void gravarArquivo()throws IOException
        {
            JFileChooser f = new JFileChooser();
            f.setFileSelectionMode(JFileChooser.FILES_ONLY);
            f.setFileFilter(new FileNameExtensionFilter("Arquivo de desenho Paint","paint"));
            int escolha = f.showDialog(this,"Salvar");
            if(escolha == JFileChooser.APPROVE_OPTION)
            {
                Path p = f.getSelectedFile().toPath();
                if(p.toString().substring(p.toString().lastIndexOf("."),p.toString().length()).equals(".paint"))
                {
                    File filess = new File(p.toString());
                    if(filess.exists())
                    {
                       String[] options = {"Sim","Não"};
                       int confirmar = JOptionPane.showOptionDialog(null,"Esse arquivo já existe\nDeseja sobrescrevê-lo?","Salvar",0,JOptionPane.QUESTION_MESSAGE,null,options,0);
                       if(confirmar!=0)
                       {
                           return;
                       }
                    }
                    ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get(p.toString())));
                    out.writeObject(this.desenho);
                    out.close();
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Extensão do arquivo deve ser .paint e não "+p.toString().substring(p.toString().lastIndexOf("."),p.toString().length()),"ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        private void lerArquivo()
        {
            JFileChooser f = new JFileChooser();
            f.setFileSelectionMode(JFileChooser.FILES_ONLY);
            f.setFileFilter(new FileNameExtensionFilter("Arquivo de desenho Paint","paint"));
            int escolha = f.showDialog(Paint.this,"Abrir");
            if(escolha == JFileChooser.APPROVE_OPTION)
            {
                Path p = f.getSelectedFile().toPath();
                String extension = p.toString().substring(p.toString().lastIndexOf("."),p.toString().length());
                if(extension.equals(".paint"))
                {
                    try
                    {
                        ObjectInputStream in = new ObjectInputStream(Files.newInputStream(p));
                        this.dispose();
                        Paint ppp = new Paint( (Painel) in.readObject() );
                        ppp.panelCorFundo.setBackground(ppp.desenho.getBackground());
                        ppp.setSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 50,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 50));
                        ppp.setVisible(true);
                    }
                    catch(ClassNotFoundException | IOException e)
                    {
                        JOptionPane.showMessageDialog(null,"Ocorreu um erro ao abrir o arquivo: " + e.getMessage(),"ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(Paint.this,"Arquivos " + extension + " não são compatíveis com arquivos .paint","ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        panelControls = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panelSelectForma = new javax.swing.JPanel();
        btnDesenholivre = new javax.swing.JButton();
        btnRetangulo = new javax.swing.JButton();
        btnCirculo = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        btnLinha = new javax.swing.JButton();
        btnPolyLinha = new javax.swing.JButton();
        btnEstrela = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtNumeroLados = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        panelOpcoes = new javax.swing.JPanel();
        lblPreenchimento = new javax.swing.JLabel();
        panelSelectPreenchimento = new javax.swing.JPanel();
        btnSemPreenchimento = new javax.swing.JButton();
        btnCorSolida = new javax.swing.JButton();
        btnGradiente = new javax.swing.JButton();
        lblContorno = new javax.swing.JLabel();
        panelSelectContorno = new javax.swing.JPanel();
        btnContornoSolido = new javax.swing.JButton();
        btnContornoTracejado = new javax.swing.JButton();
        lblEspessura = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        lblDashed = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        panelDesenho = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelCores = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        panelSelectCores = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        panelCoresRecentes = new javax.swing.JPanel();
        lblMaisCores = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        panelCoresSelecionadas = new javax.swing.JPanel();
        panelCorPrimaria = new javax.swing.JPanel();
        btnCorPrimaria = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        panelCorSecundaria = new javax.swing.JPanel();
        btnCorSecundaria = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        panelCorFundo = new javax.swing.JPanel();
        panelCoords = new javax.swing.JPanel();
        lblMouseCoords = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jScrollPane3.setMinimumSize(new java.awt.Dimension(1102, 65));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(1102, 65));

        panelControls.setBackground(new java.awt.Color(120, 120, 120));
        panelControls.setMaximumSize(new java.awt.Dimension(32767, 40));
        panelControls.setMinimumSize(new java.awt.Dimension(1100, 40));
        panelControls.setPreferredSize(new java.awt.Dimension(1100, 40));
        panelControls.setLayout(new java.awt.GridLayout(1, 2));

        jPanel5.setBackground(new java.awt.Color(120, 120, 120));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Desenhar forma: ");
        jPanel5.add(jLabel1);

        panelSelectForma.setBackground(new java.awt.Color(120, 120, 120));
        panelSelectForma.setPreferredSize(new java.awt.Dimension(240, 30));

        btnDesenholivre.setBackground(new java.awt.Color(31, 31, 31));
        btnDesenholivre.setToolTipText("Desenho Livre");
        btnDesenholivre.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDesenholivre.setPreferredSize(new java.awt.Dimension(30, 30));
        btnDesenholivre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDesenholivreMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDesenholivreMouseExited(evt);
            }
        });

        btnRetangulo.setBackground(new java.awt.Color(31, 31, 31));
        btnRetangulo.setToolTipText("Retângulo");
        btnRetangulo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRetangulo.setPreferredSize(new java.awt.Dimension(30, 30));
        btnRetangulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRetanguloMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRetanguloMouseExited(evt);
            }
        });

        btnCirculo.setBackground(new java.awt.Color(31, 31, 31));
        btnCirculo.setToolTipText("Círculo");
        btnCirculo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCirculo.setPreferredSize(new java.awt.Dimension(30, 30));
        btnCirculo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCirculoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCirculoMouseExited(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(31, 31, 31));
        jButton4.setToolTipText("Linha");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setPreferredSize(new java.awt.Dimension(30, 30));
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton4MouseExited(evt);
            }
        });

        btnLinha.setBackground(new java.awt.Color(31, 31, 31));
        btnLinha.setToolTipText("Polylinha");
        btnLinha.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLinha.setPreferredSize(new java.awt.Dimension(30, 30));
        btnLinha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLinhaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLinhaMouseExited(evt);
            }
        });
        btnLinha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLinhaActionPerformed(evt);
            }
        });

        btnPolyLinha.setBackground(new java.awt.Color(31, 31, 31));
        btnPolyLinha.setToolTipText("Estrela");
        btnPolyLinha.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPolyLinha.setPreferredSize(new java.awt.Dimension(30, 30));
        btnPolyLinha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPolyLinhaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPolyLinhaMouseExited(evt);
            }
        });

        btnEstrela.setBackground(new java.awt.Color(31, 31, 31));
        btnEstrela.setToolTipText("Polígono");
        btnEstrela.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEstrela.setPreferredSize(new java.awt.Dimension(30, 30));
        btnEstrela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEstrelaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEstrelaMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelSelectFormaLayout = new javax.swing.GroupLayout(panelSelectForma);
        panelSelectForma.setLayout(panelSelectFormaLayout);
        panelSelectFormaLayout.setHorizontalGroup(
            panelSelectFormaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
            .addGroup(panelSelectFormaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelSelectFormaLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btnDesenholivre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(btnRetangulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(btnCirculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(btnLinha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(btnPolyLinha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(btnEstrela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panelSelectFormaLayout.setVerticalGroup(
            panelSelectFormaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
            .addGroup(panelSelectFormaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelSelectFormaLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(panelSelectFormaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnDesenholivre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnRetangulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCirculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLinha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnPolyLinha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEstrela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        btnDesenholivre.setIcon(new ImageIcon("assets/PaintIcons/LapisIcon.png"));
        btnRetangulo.setIcon(new ImageIcon("assets/PaintIcons/RetanguloIcon.png"));
        btnCirculo.setIcon(new ImageIcon("assets/PaintIcons/CirculoIcon.png"));
        jButton4.setIcon(new ImageIcon("assets/Painticons/LinhaIcon.png"));
        btnLinha.setIcon(new ImageIcon("assets/PaintIcons/PolylinhaIcon.png"));
        btnPolyLinha.setIcon(new ImageIcon("assets/PaintIcons/StarIcon.png"));
        btnEstrela.setIcon(new ImageIcon("assets/PaintIcons/TrianguloIcon.png"));

        jPanel5.add(panelSelectForma);

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("     Número de lados:");
        jPanel5.add(jLabel4);

        txtNumeroLados.setBackground(new java.awt.Color(31, 31, 31));
        txtNumeroLados.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtNumeroLados.setForeground(new java.awt.Color(255, 255, 255));
        txtNumeroLados.setPreferredSize(new java.awt.Dimension(50, 25));
        txtNumeroLados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNumeroLadosKeyReleased(evt);
            }
        });
        jPanel5.add(txtNumeroLados);

        panelControls.add(jPanel5);

        jPanel6.setBackground(new java.awt.Color(120, 120, 120));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButton7.setBackground(new java.awt.Color(31, 31, 31));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Desfazer");
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.setPreferredSize(new java.awt.Dimension(100, 30));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton7);

        jButton8.setBackground(new java.awt.Color(31, 31, 31));
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Limpar");
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.setMinimumSize(new java.awt.Dimension(74, 30));
        jButton8.setPreferredSize(new java.awt.Dimension(100, 30));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton8);

        panelControls.add(jPanel6);

        jScrollPane3.setViewportView(panelControls);

        getContentPane().add(jScrollPane3);

        jScrollPane2.setMinimumSize(new java.awt.Dimension(800, 75));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(800, 75));

        panelOpcoes.setBackground(new java.awt.Color(90, 90, 90));
        panelOpcoes.setMinimumSize(new java.awt.Dimension(800, 50));
        panelOpcoes.setPreferredSize(new java.awt.Dimension(800, 50));
        panelOpcoes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblPreenchimento.setForeground(new java.awt.Color(255, 255, 255));
        lblPreenchimento.setText("Preenchimento: ");
        panelOpcoes.add(lblPreenchimento);

        panelSelectPreenchimento.setBackground(new java.awt.Color(90, 90, 90));
        panelSelectPreenchimento.setAlignmentX(0.0F);
        panelSelectPreenchimento.setAlignmentY(0.0F);
        panelSelectPreenchimento.setPreferredSize(new java.awt.Dimension(100, 35));

        btnSemPreenchimento.setBackground(new java.awt.Color(31, 31, 31));
        btnSemPreenchimento.setToolTipText("Sem Preenchimento");
        btnSemPreenchimento.setAlignmentY(0.0F);
        btnSemPreenchimento.setBorder(null);
        btnSemPreenchimento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSemPreenchimento.setIconTextGap(1);
        btnSemPreenchimento.setMargin(null);
        btnSemPreenchimento.setMaximumSize(new java.awt.Dimension(30, 30));
        btnSemPreenchimento.setPreferredSize(new java.awt.Dimension(30, 30));
        btnSemPreenchimento.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnSemPreenchimento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSemPreenchimentoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSemPreenchimentoMouseExited(evt);
            }
        });
        btnSemPreenchimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSemPreenchimentoActionPerformed(evt);
            }
        });

        btnCorSolida.setBackground(new java.awt.Color(31, 31, 31));
        btnCorSolida.setToolTipText("Cor Sólida");
        btnCorSolida.setAlignmentY(0.0F);
        btnCorSolida.setBorder(null);
        btnCorSolida.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCorSolida.setIconTextGap(1);
        btnCorSolida.setMargin(null);
        btnCorSolida.setMaximumSize(new java.awt.Dimension(30, 30));
        btnCorSolida.setPreferredSize(new java.awt.Dimension(30, 30));
        btnCorSolida.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnCorSolida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCorSolidaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCorSolidaMouseExited(evt);
            }
        });
        btnCorSolida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorSolidaActionPerformed(evt);
            }
        });

        btnGradiente.setBackground(new java.awt.Color(31, 31, 31));
        btnGradiente.setToolTipText("Gradiente");
        btnGradiente.setAlignmentY(0.0F);
        btnGradiente.setBorder(null);
        btnGradiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGradiente.setIconTextGap(1);
        btnGradiente.setMargin(null);
        btnGradiente.setMaximumSize(new java.awt.Dimension(30, 30));
        btnGradiente.setPreferredSize(new java.awt.Dimension(30, 30));
        btnGradiente.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnGradiente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGradienteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGradienteMouseExited(evt);
            }
        });
        btnGradiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGradienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSelectPreenchimentoLayout = new javax.swing.GroupLayout(panelSelectPreenchimento);
        panelSelectPreenchimento.setLayout(panelSelectPreenchimentoLayout);
        panelSelectPreenchimentoLayout.setHorizontalGroup(
            panelSelectPreenchimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(panelSelectPreenchimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelSelectPreenchimentoLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btnSemPreenchimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(btnCorSolida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(btnGradiente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panelSelectPreenchimentoLayout.setVerticalGroup(
            panelSelectPreenchimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
            .addGroup(panelSelectPreenchimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelSelectPreenchimentoLayout.createSequentialGroup()
                    .addGap(0, 2, Short.MAX_VALUE)
                    .addGroup(panelSelectPreenchimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnSemPreenchimento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnGradiente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCorSolida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 3, Short.MAX_VALUE)))
        );

        btnSemPreenchimento.setIcon(new ImageIcon("assets/PaintIcons/RetanguloIcon.png"));
        btnCorSolida.setIcon(new ImageIcon("assets/PaintIcons/RetanguloFillIcon.png"));
        btnGradiente.setIcon(new ImageIcon("assets/PaintIcons/RetanguloGradientIcon.png"));

        panelOpcoes.add(panelSelectPreenchimento);

        lblContorno.setForeground(new java.awt.Color(255, 255, 255));
        lblContorno.setText("          Contorno: ");
        panelOpcoes.add(lblContorno);

        panelSelectContorno.setBackground(new java.awt.Color(90, 90, 90));
        panelSelectContorno.setPreferredSize(new java.awt.Dimension(65, 30));

        btnContornoSolido.setBackground(new java.awt.Color(31, 31, 31));
        btnContornoSolido.setToolTipText("Linha Sólida");
        btnContornoSolido.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnContornoSolido.setPreferredSize(new java.awt.Dimension(30, 30));
        btnContornoSolido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnContornoSolidoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnContornoSolidoMouseExited(evt);
            }
        });
        btnContornoSolido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContornoSolidoActionPerformed(evt);
            }
        });

        btnContornoTracejado.setBackground(new java.awt.Color(31, 31, 31));
        btnContornoTracejado.setToolTipText("Linha Tracejada");
        btnContornoTracejado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnContornoTracejado.setPreferredSize(new java.awt.Dimension(30, 30));
        btnContornoTracejado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnContornoTracejadoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnContornoTracejadoMouseExited(evt);
            }
        });
        btnContornoTracejado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContornoTracejadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSelectContornoLayout = new javax.swing.GroupLayout(panelSelectContorno);
        panelSelectContorno.setLayout(panelSelectContornoLayout);
        panelSelectContornoLayout.setHorizontalGroup(
            panelSelectContornoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
            .addGroup(panelSelectContornoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelSelectContornoLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btnContornoSolido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(btnContornoTracejado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panelSelectContornoLayout.setVerticalGroup(
            panelSelectContornoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
            .addGroup(panelSelectContornoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelSelectContornoLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(panelSelectContornoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnContornoSolido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnContornoTracejado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        btnContornoSolido.setIcon(new ImageIcon("assets/PaintIcons/LinhaIcon.png"));
        btnContornoTracejado.setIcon(new ImageIcon("assets/PaintIcons/LinhaDashedIcon.png"));

        panelOpcoes.add(panelSelectContorno);

        lblEspessura.setForeground(new java.awt.Color(255, 255, 255));
        lblEspessura.setText("          Espessura da linha: ");
        panelOpcoes.add(lblEspessura);

        jTextField1.setBackground(new java.awt.Color(31, 31, 31));
        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setPreferredSize(new java.awt.Dimension(40, 25));
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        panelOpcoes.add(jTextField1);

        lblDashed.setForeground(new java.awt.Color(255, 255, 255));
        lblDashed.setText("          Tamanho do tracejado:  ");
        panelOpcoes.add(lblDashed);

        jTextField2.setBackground(new java.awt.Color(31, 31, 31));
        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(255, 255, 255));
        jTextField2.setPreferredSize(new java.awt.Dimension(40, 25));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });
        panelOpcoes.add(jTextField2);

        jScrollPane2.setViewportView(panelOpcoes);

        getContentPane().add(jScrollPane2);

        panelDesenho.setBackground(new java.awt.Color(61, 61, 61));
        panelDesenho.setLayout(new java.awt.CardLayout());
        getContentPane().add(panelDesenho);

        jScrollPane1.setBackground(new java.awt.Color(120, 120, 120));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(1200, 95));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1200, 95));

        panelCores.setBackground(new java.awt.Color(120, 120, 120));
        panelCores.setMaximumSize(new java.awt.Dimension(32767, 70));
        panelCores.setMinimumSize(new java.awt.Dimension(1200, 70));
        panelCores.setPreferredSize(new java.awt.Dimension(1200, 70));
        panelCores.setLayout(new java.awt.GridLayout(1, 2));

        jPanel1.setBackground(new java.awt.Color(120, 120, 120));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Cores: ");
        jPanel1.add(jLabel2);

        panelSelectCores.setBackground(new java.awt.Color(120, 120, 120));
        panelSelectCores.setPreferredSize(new java.awt.Dimension(180, 60));
        panelSelectCores.setLayout(new java.awt.GridLayout(2, 6));
        jPanel1.add(panelSelectCores);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("          Paleta de cores: ");
        jPanel1.add(jLabel3);

        panelCoresRecentes.setBackground(new java.awt.Color(120, 120, 120));
        panelCoresRecentes.setPreferredSize(new java.awt.Dimension(180, 60));
        panelCoresRecentes.setLayout(new java.awt.GridLayout(2, 6));
        jPanel1.add(panelCoresRecentes);

        lblMaisCores.setForeground(new java.awt.Color(255, 255, 255));
        lblMaisCores.setText("         Adicionar à paleta: ");
        jPanel1.add(lblMaisCores);

        jButton1.setBackground(new java.awt.Color(61, 61, 61));
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setPreferredSize(new java.awt.Dimension(60, 60));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setIcon(new ImageIcon("assets/PaintIcons/img.png"));

        panelCoresSelecionadas.setBackground(new java.awt.Color(120, 120, 120));
        panelCoresSelecionadas.setPreferredSize(new java.awt.Dimension(250, 60));
        panelCoresSelecionadas.setLayout(new java.awt.GridLayout(2, 2));

        panelCorPrimaria.setBackground(new java.awt.Color(120, 120, 120));
        panelCorPrimaria.setAlignmentX(0.0F);
        panelCorPrimaria.setAlignmentY(0.0F);
        panelCorPrimaria.setMinimumSize(new java.awt.Dimension(0, 0));
        panelCorPrimaria.setPreferredSize(new java.awt.Dimension(100, 25));

        btnCorPrimaria.setBackground(new java.awt.Color(102, 102, 255));
        btnCorPrimaria.setPreferredSize(new java.awt.Dimension(25, 25));
        panelCorPrimaria.add(btnCorPrimaria);

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Cor primária ");
        jLabel6.setPreferredSize(new java.awt.Dimension(100, 25));
        jLabel6.setRequestFocusEnabled(false);
        panelCorPrimaria.add(jLabel6);

        panelCoresSelecionadas.add(panelCorPrimaria);

        panelCorSecundaria.setBackground(new java.awt.Color(120, 120, 120));

        btnCorSecundaria.setBackground(new java.awt.Color(204, 102, 255));
        btnCorSecundaria.setPreferredSize(new java.awt.Dimension(25, 25));
        panelCorSecundaria.add(btnCorSecundaria);

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Cor secundária");
        jLabel7.setPreferredSize(new java.awt.Dimension(100, 25));
        panelCorSecundaria.add(jLabel7);

        panelCoresSelecionadas.add(panelCorSecundaria);

        jPanel1.add(panelCoresSelecionadas);

        jPanel7.setBackground(new java.awt.Color(120, 120, 120));
        jPanel7.setPreferredSize(new java.awt.Dimension(150, 60));
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Cor de fundo: ");
        jPanel7.add(jLabel9);

        panelCorFundo.setBackground(new java.awt.Color(61, 61, 61));
        panelCorFundo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCorFundo.setPreferredSize(new java.awt.Dimension(60, 60));
        panelCorFundo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelCorFundoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelCorFundoLayout = new javax.swing.GroupLayout(panelCorFundo);
        panelCorFundo.setLayout(panelCorFundoLayout);
        panelCorFundoLayout.setHorizontalGroup(
            panelCorFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        panelCorFundoLayout.setVerticalGroup(
            panelCorFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        jPanel7.add(panelCorFundo);

        jPanel1.add(jPanel7);

        panelCores.add(jPanel1);

        jScrollPane1.setViewportView(panelCores);

        getContentPane().add(jScrollPane1);

        panelCoords.setBackground(new java.awt.Color(31, 31, 31));
        panelCoords.setMaximumSize(new java.awt.Dimension(32767, 30));
        panelCoords.setPreferredSize(new java.awt.Dimension(889, 30));
        panelCoords.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblMouseCoords.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMouseCoords.setForeground(new java.awt.Color(255, 255, 255));
        lblMouseCoords.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMouseCoords.setText("(0,0)");
        lblMouseCoords.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblMouseCoords.setMaximumSize(null);
        lblMouseCoords.setMinimumSize(null);
        lblMouseCoords.setPreferredSize(null);
        panelCoords.add(lblMouseCoords);

        getContentPane().add(panelCoords);

        jMenuBar1.setBackground(new java.awt.Color(31, 31, 31));
        jMenuBar1.setBorder(null);
        jMenuBar1.setForeground(new java.awt.Color(255, 255, 255));

        jMenu1.setForeground(new java.awt.Color(0, 0, 0));
        jMenu1.setText("Salvar");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        jMenu2.setForeground(new java.awt.Color(0, 0, 0));
        jMenu2.setText("Abrir");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu2);

        jMenu3.setBackground(new java.awt.Color(31, 31, 31));
        jMenu3.setForeground(new java.awt.Color(0, 0, 0));
        jMenu3.setText("Exportar Imagem");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu3);

        jMenu4.setBackground(new java.awt.Color(31, 31, 31));
        jMenu4.setForeground(new java.awt.Color(0, 0, 0));
        jMenu4.setText("Sair");
        jMenu4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu4MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        this.desenho.undo();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        String[] opc = {"Sim","Não"};
        int resp = JOptionPane.showOptionDialog(this,"Tem certeza que deseja limpar a tela?","Limpar", 0, JOptionPane.WARNING_MESSAGE, null, opc,0);
        if(resp == 0)
        {
            this.desenho.clear();
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void btnSemPreenchimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSemPreenchimentoActionPerformed
        // TODO add your handling code here:
        this.pretificarPrenchimento();
        this.btnSemPreenchimento.setIcon(new ImageIcon("assets/PaintIcons/RetanguloIcon--selected.png"));
        this.btnSemPreenchimento.setBackground(Color.WHITE);
        this.isFilled = false;
        this.isGradient = false;
        this.isDashedLine = false;
    }//GEN-LAST:event_btnSemPreenchimentoActionPerformed

    private void pretificarPrenchimento()
    {
        final String[] paths = {
            "assets/PaintIcons/RetanguloIcon.png",
            "assets/PaintIcons/RetanguloFillIcon.png",
            "assets/PaintIcons/RetanguloGradientIcon.png"
        };
        this.pretificarFilhos(this.panelSelectPreenchimento,paths);
    }
    
    private void btnCorSolidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorSolidaActionPerformed
        // TODO add your handling code here:
        this.pretificarPrenchimento();
        this.btnCorSolida.setIcon(new ImageIcon("assets/PaintIcons/RetanguloFillIcon--selected.png"));
        this.btnCorSolida.setBackground(Color.WHITE);
        this.isFilled = true;
        this.isGradient = false;
    }//GEN-LAST:event_btnCorSolidaActionPerformed

    private void btnGradienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGradienteActionPerformed
        // TODO add your handling code here:
        this.pretificarPrenchimento();
        this.btnGradiente.setIcon(new ImageIcon("assets/PaintIcons/RetanguloGradientIcon--selected.png"));
        this.btnGradiente.setBackground(Color.WHITE);
        this.isFilled = true;
        this.isGradient = true;
    }//GEN-LAST:event_btnGradienteActionPerformed

       private void pretificarContorno()
       {
            final String[] paths = {
                "assets/PaintIcons/LinhaIcon.png",
                "assets/PaintIcons/LinhaDashedIcon.png"
            };
            this.pretificarFilhos(this.panelSelectContorno, paths);
       }
           
    private void btnContornoTracejadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContornoTracejadoActionPerformed
        // TODO add your handling code here:
        this.pretificarContorno();
        this.btnContornoTracejado.setIcon(new ImageIcon("assets/PaintIcons/LinhaDashedIcon--selected.png"));
        this.btnContornoTracejado.setBackground(Color.WHITE);
        this.isDashedLine = true;
    }//GEN-LAST:event_btnContornoTracejadoActionPerformed

    private void btnContornoSolidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContornoSolidoActionPerformed
        // TODO add your handling code here:
        this.pretificarContorno();
        this.btnContornoSolido.setIcon(new ImageIcon("assets/PaintIcons/LinhaIcon--selected.png"));
        this.btnContornoSolido.setBackground(Color.WHITE);
        this.isDashedLine = false;
    }//GEN-LAST:event_btnContornoSolidoActionPerformed

    private void panelCorFundoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelCorFundoMouseClicked
        // TODO add your handling code here:
        Color corSelecionada = JColorChooser.showDialog(this, "Escolha uma cor", this.panelCorFundo.getBackground());
        if(corSelecionada != null)
        {
            this.desenho.trocarCorFundo(corSelecionada);
            this.panelCorFundo.setBackground(corSelecionada);
        }
    }//GEN-LAST:event_panelCorFundoMouseClicked

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        try
        {
            this.jTextField1.setText(this.jTextField1.getText().replaceAll("[^0-9]",""));
            this.lineWidth = Integer.parseInt(this.jTextField1.getText());
            this.dashLength = Integer.parseInt(this.jTextField1.getText());
            this.jTextField2.setText(String.valueOf(this.dashLength));
        }
        catch(NumberFormatException e)
        {
            this.lineWidth = 5;
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        try
        {
            this.jTextField2.setText(this.jTextField2.getText().replaceAll("[^0-9]",""));
            this.dashLength = Integer.parseInt(this.jTextField2.getText());
        }
        catch(NumberFormatException e)
        {
            this.dashLength = 5;
        }
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void btnDesenholivreMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDesenholivreMouseEntered
        // TODO add your handling code here:
        if(this.btnDesenholivre.getBackground().equals(new Color(255,255,255)))return;
        this.btnDesenholivre.setIcon(new ImageIcon("assets/PaintIcons/LapisIcon--hover.png"));
    }//GEN-LAST:event_btnDesenholivreMouseEntered

    private void btnDesenholivreMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDesenholivreMouseExited
        // TODO add your handling code here:
        if(this.btnDesenholivre.getBackground().equals(new Color(255,255,255)))return;
        this.btnDesenholivre.setIcon(new ImageIcon("assets/PaintIcons/LapisIcon.png"));
    }//GEN-LAST:event_btnDesenholivreMouseExited

    private void btnRetanguloMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRetanguloMouseEntered
        // TODO add your handling code here:
        if(this.btnRetangulo.getBackground().equals(new Color(255,255,255)))return;
        this.btnRetangulo.setIcon(new ImageIcon("assets/PaintIcons/RetanguloIcon--hover.png"));
    }//GEN-LAST:event_btnRetanguloMouseEntered

    private void btnRetanguloMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRetanguloMouseExited
        // TODO add your handling code here:
        if(this.btnRetangulo.getBackground().equals(new Color(255,255,255)))return;
        this.btnRetangulo.setIcon(new ImageIcon("assets/PaintIcons/RetanguloIcon.png"));
    }//GEN-LAST:event_btnRetanguloMouseExited

    private void btnLinhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLinhaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLinhaActionPerformed

    private void btnCirculoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCirculoMouseEntered
        // TODO add your handling code here:
        if(this.btnCirculo.getBackground().equals(new Color(255,255,255)))return;
        this.btnCirculo.setIcon(new ImageIcon("assets/PaintIcons/CirculoIcon--hover.png"));
    }//GEN-LAST:event_btnCirculoMouseEntered

    private void btnCirculoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCirculoMouseExited
        // TODO add your handling code here:
        if(this.btnCirculo.getBackground().equals(new Color(255,255,255)))return;
        this.btnCirculo.setIcon(new ImageIcon("assets/PaintIcons/CirculoIcon.png"));
    }//GEN-LAST:event_btnCirculoMouseExited

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        // TODO add your handling code here:
        if(this.jButton4.getBackground().equals(new Color(255,255,255)))return;
        this.jButton4.setIcon(new ImageIcon("assets/PaintIcons/LinhaIcon--hover.png"));
    }//GEN-LAST:event_jButton4MouseEntered

    private void jButton4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseExited
        // TODO add your handling code here:
        if(this.jButton4.getBackground().equals(new Color(255,255,255)))return;
        this.jButton4.setIcon(new ImageIcon("assets/PaintIcons/LinhaIcon.png"));
    }//GEN-LAST:event_jButton4MouseExited

    private void btnLinhaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLinhaMouseEntered
        // TODO add your handling code here:
        if(this.btnLinha.getBackground().equals(new Color(255,255,255)))return;
        this.btnLinha.setIcon(new ImageIcon("assets/PaintIcons/PolylinhaIcon--hover.png"));
    }//GEN-LAST:event_btnLinhaMouseEntered

    private void btnLinhaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLinhaMouseExited
        // TODO add your handling code here:
        if(this.btnLinha.getBackground().equals(new Color(255,255,255)))return;
        this.btnLinha.setIcon(new ImageIcon("assets/PaintIcons/PolylinhaIcon.png"));
    }//GEN-LAST:event_btnLinhaMouseExited

    private void btnPolyLinhaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPolyLinhaMouseExited
        // TODO add your handling code here:
        if(this.btnPolyLinha.getBackground().equals(new Color(255,255,255)))return;
        this.btnPolyLinha.setIcon(new ImageIcon("assets/PaintIcons/StarIcon.png"));
    }//GEN-LAST:event_btnPolyLinhaMouseExited

    private void btnPolyLinhaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPolyLinhaMouseEntered
        // TODO add your handling code here:
        if(this.btnPolyLinha.getBackground().equals(new Color(255,255,255)))return;
        this.btnPolyLinha.setIcon(new ImageIcon("assets/PaintIcons/StarIcon--hover.png"));
    }//GEN-LAST:event_btnPolyLinhaMouseEntered

    private void btnEstrelaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEstrelaMouseEntered
        // TODO add your handling code here:
        if(this.btnEstrela.getBackground().equals(new Color(255,255,255)))return;
        this.btnEstrela.setIcon(new ImageIcon("assets/PaintIcons/TrianguloIcon--hover.png"));
    }//GEN-LAST:event_btnEstrelaMouseEntered

    private void btnEstrelaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEstrelaMouseExited
        // TODO add your handling code here:
        if(this.btnEstrela.getBackground().equals(new Color(255,255,255)))return;
        this.btnEstrela.setIcon(new ImageIcon("assets/PaintIcons/TrianguloIcon.png"));
    }//GEN-LAST:event_btnEstrelaMouseExited

    private void btnSemPreenchimentoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSemPreenchimentoMouseEntered
        // TODO add your handling code here:
        if(this.btnSemPreenchimento.getBackground().equals(new Color(255,255,255)))return;
        this.btnSemPreenchimento.setIcon(new ImageIcon("assets/PaintIcons/RetanguloIcon--hover.png"));
    }//GEN-LAST:event_btnSemPreenchimentoMouseEntered

    private void btnSemPreenchimentoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSemPreenchimentoMouseExited
        // TODO add your handling code here:
        if(this.btnSemPreenchimento.getBackground().equals(new Color(255,255,255)))return;
        this.btnSemPreenchimento.setIcon(new ImageIcon("assets/PaintIcons/RetanguloIcon.png"));
    }//GEN-LAST:event_btnSemPreenchimentoMouseExited

    private void btnGradienteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGradienteMouseEntered
        // TODO add your handling code here:
        if(this.btnGradiente.getBackground().equals(new Color(255,255,255)))return;
        this.btnGradiente.setIcon(new ImageIcon("assets/PaintIcons/RetanguloGradientIcon--hover.png"));
    }//GEN-LAST:event_btnGradienteMouseEntered

    private void btnGradienteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGradienteMouseExited
        // TODO add your handling code here:
        if(this.btnGradiente.getBackground().equals(new Color(255,255,255)))return;
        this.btnGradiente.setIcon(new ImageIcon("assets/PaintIcons/RetanguloGradientIcon.png"));
    }//GEN-LAST:event_btnGradienteMouseExited

    private void btnCorSolidaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCorSolidaMouseEntered
        // TODO add your handling code here:
        if(this.btnCorSolida.getBackground().equals(new Color(255,255,255)))return;
        this.btnCorSolida.setIcon(new ImageIcon("assets/PaintIcons/RetanguloFillIcon--hover.png"));
    }//GEN-LAST:event_btnCorSolidaMouseEntered

    private void btnCorSolidaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCorSolidaMouseExited
        // TODO add your handling code here:
        if(this.btnCorSolida.getBackground().equals(new Color(255,255,255)))return;
        this.btnCorSolida.setIcon(new ImageIcon("assets/PaintIcons/RetanguloFillIcon.png"));
    }//GEN-LAST:event_btnCorSolidaMouseExited

    private void btnContornoSolidoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnContornoSolidoMouseEntered
        // TODO add your handling code here:
        if(this.btnContornoSolido.getBackground().equals(new Color(255,255,255)))return;
        this.btnContornoSolido.setIcon(new ImageIcon("assets/PaintIcons/LinhaIcon--hover.png"));
    }//GEN-LAST:event_btnContornoSolidoMouseEntered

    private void btnContornoSolidoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnContornoSolidoMouseExited
        // TODO add your handling code here:
        if(this.btnContornoSolido.getBackground().equals(new Color(255,255,255)))return;
        this.btnContornoSolido.setIcon(new ImageIcon("assets/PaintIcons/LinhaIcon.png"));
    }//GEN-LAST:event_btnContornoSolidoMouseExited

    private void btnContornoTracejadoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnContornoTracejadoMouseEntered
        // TODO add your handling code here:
        if(this.btnContornoTracejado.getBackground().equals(new Color(255,255,255)))return;
        this.btnContornoTracejado.setIcon(new ImageIcon("assets/PaintIcons/LinhaDashedIcon--hover.png"));
    }//GEN-LAST:event_btnContornoTracejadoMouseEntered

    private void btnContornoTracejadoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnContornoTracejadoMouseExited
        // TODO add your handling code here:
        if(this.btnContornoTracejado.getBackground().equals(new Color(255,255,255)))return;
        this.btnContornoTracejado.setIcon(new ImageIcon("assets/PaintIcons/LinhaDashedIcon.png"));
    }//GEN-LAST:event_btnContornoTracejadoMouseExited

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        switch(evt.getButton())
        {
            case 1 ->{
                Color corSelecionada = JColorChooser.showDialog(this,"Escolha uma cor",this.btnCorPrimaria.getBackground());
                if(corSelecionada != null)
                {
                    this.addCorRecente(corSelecionada);
                }
            }
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void txtNumeroLadosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroLadosKeyReleased
        // TODO add your handling code here:
        try
        {
            this.txtNumeroLados.setText( this.txtNumeroLados.getText().replaceAll("[^0-9]","") );
            this.numeroLados = Integer.parseInt(this.txtNumeroLados.getText());
        }
        catch(NumberFormatException e)
        {
            this.numeroLados = 3;
            
        }
    }//GEN-LAST:event_txtNumeroLadosKeyReleased

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        // TODO add your handling code here:
        try
        {
            this.gravarArquivo();
            
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null,"Não foi possível salvar: "+e.getMessage(),"ERRO",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenu1MouseClicked

    
    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked
        // TODO add your handling code here:
        this.lerArquivo();  
    }//GEN-LAST:event_jMenu2MouseClicked

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        // TODO add your handling code here:
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);
        f.setFileFilter(new FileNameExtensionFilter("Arquivo de imagem","png","gif","jpg","jpeg"));
        int botao = f.showDialog(this,"Save");
        if(botao == JFileChooser.APPROVE_OPTION)
        {
            String[] allowed = {".gif",".jpg",".jpeg",".png"};
            Path p = f.getSelectedFile().toPath();
            String ps = p.toString();
            boolean tem = false;
            for(String ext : allowed)
            {
                if(ps.substring(ps.lastIndexOf(".")).equals(ext))
                {
                    tem = true;
                }
            }
            if(tem)
            {
                BufferedImage im = new BufferedImage(this.desenho.getWidth(),this.desenho.getHeight(),BufferedImage.TYPE_INT_RGB);
                Graphics2D gg = im.createGraphics();
                gg.setBackground(this.desenho.getBackground());
                gg.setPaint(this.desenho.getBackground());
                gg.fillRect(0,0,this.desenho.getWidth(),this.desenho.getHeight());
                for(Drawable d : this.desenho.getStackShapes())
                {
                    d.draw(gg);
                }
                try
                {
                    ImageIO.write(im, ps.substring(ps.lastIndexOf(".")+1), new File(ps));
                }
                catch(IOException e)
                {
                    JOptionPane.showMessageDialog(this,"Erro ao exportar imagem: "+e.getMessage(),"ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(this,"Extensão "+ps.substring(ps.lastIndexOf("."))+" não é suportada!\nExtensões suportadas: png, jpg, jpeg e gif","ERRO",JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jMenu3MouseClicked

    private void jMenu4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu4MouseClicked
        // TODO add your handling code here:
        this.dispose();
        Programa p = new Programa();
        p.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        p.setVisible(true);
    }//GEN-LAST:event_jMenu4MouseClicked

    private void renderColors()
    {
        for(int i=0;i<360-(360/12*2);i+=360/12)
        {
            JButton btnCor = new JButton();
            btnCor.setBackground(Color.getHSBColor((float)i/360, 1, 1));
            btnCor.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCor.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == 1)
                    {
                        Paint.this.btnCorPrimaria.setBackground(btnCor.getBackground());
                    }
                    else if(e.getButton() == 3)
                    {
                        Paint.this.btnCorSecundaria.setBackground(btnCor.getBackground());
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    
                }
                
            });
            this.panelSelectCores.add(btnCor);        
        }
        final JButton btnCor = new JButton();
        btnCor.setBackground(Color.WHITE);
        btnCor.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCor.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == 1)
                    {
                        Paint.this.btnCorPrimaria.setBackground(btnCor.getBackground());
                    }
                    else if(e.getButton() == 3)
                    {
                        Paint.this.btnCorSecundaria.setBackground(btnCor.getBackground());
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    
                }
                
            });
        this.panelSelectCores.add(btnCor);        
        final JButton btnCor2 = new JButton();
        btnCor2.setBackground(Color.BLACK);
        btnCor2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCor2.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == 1)
                    {
                        Paint.this.btnCorPrimaria.setBackground(btnCor2.getBackground());
                    }
                    else if(e.getButton() == 3)
                    {
                        Paint.this.btnCorSecundaria.setBackground(btnCor2.getBackground());
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    
                }
                
            });
        this.panelSelectCores.add(btnCor2);        
    }
    
    private void addCorRecente(Color cor)
    {
        if(this.coresRecentes.size() == 12)
        {
            this.coresRecentes.removeFirst();
        }
        JButton btn = new JButton();
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(cor);
        btn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Paint.this.btnCorPrimaria.setBackground(btn.getBackground());
            }
        });
        btn.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == 3)
                {
                    Paint.this.btnCorSecundaria.setBackground(btn.getBackground());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
            
        });
        this.coresRecentes.addLast(btn);
        this.renderCoresRecentes();
    }
    
    private void renderCoresRecentes()
    {
        this.panelCoresRecentes.removeAll();
        this.coresRecentes.forEach( btnCor -> {
            this.panelCoresRecentes.add(btnCor);
        });
        for(int i=this.coresRecentes.size();i<12;i++)
        {
            JButton btnNada = new JButton();
            btnNada.setBackground(new Color(200,200,200,0));
            btnNada.setBorder(new LineBorder(new Color(0,0,0),1,true));
            this.panelCoresRecentes.add(btnNada);
        }
        this.revalidate();
        this.repaint();
    }
    
    private void pretificarFilhos(Container cont, String[] paths)
    {
        Component[] comps = cont.getComponents();
        int index = 0;
        for(Component comp : comps)
        {
            final int i = index++;
            comp.setBackground(new Color(31,31,31));
            ((JButton)comp).setIcon(new ImageIcon(paths[i]));
        }
    }
    
    private void addSelectShapeEvent()
    {
        final String[] paths2 =
        {
            "assets/PaintIcons/LapisIcon--selected.png",
            "assets/PaintIcons/RetanguloIcon--selected.png",
            "assets/PaintIcons/CirculoIcon--selected.png",
            "assets/PaintIcons/LinhaIcon--selected.png",
            "assets/PaintIcons/PolylinhaIcon--selected.png",
            "assets/PaintIcons/StarIcon--selected.png",
            "assets/PaintIcons/TrianguloIcon--selected.png"
        };
        int indice = 0;
        Component[] comps = this.panelSelectForma.getComponents();
        for(final Component comp : comps)
        {
            final int i = indice;
            ((JButton)comp).addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    final String[] paths = {
                        "assets/PaintIcons/LapisIcon.png",
                        "assets/PaintIcons/RetanguloIcon.png",
                        "assets/PaintIcons/CirculoIcon.png",
                        "assets/PaintIcons/LinhaIcon.png",
                        "assets/PaintIcons/PolylinhaIcon.png",
                        "assets/PaintIcons/StarIcon.png",
                        "assets/PaintIcons/TrianguloIcon.png"
                    };
                    Paint.this.pretificarFilhos(Paint.this.panelSelectForma,paths);
                    comp.setBackground(new Color(255,255,255));
                    Paint.this.shape = ((JButton)comp).getToolTipText();
                    ((JButton)comp).setIcon(new ImageIcon(paths2[i]));
                }
            });
            indice++;
        }
    }

    private final LinkedList<JButton> coresRecentes = new LinkedList();
    private Color corPrimaria = new Color(0,0,0);
    private Color corSecundaria = new Color(255,255,255);
    
    //configurações
    private String shape = "Desenho Livre";
    private boolean isFilled;
    private boolean isGradient;
    private boolean isDashedLine;
    private int lineWidth = 5;
    private int dashLength = 5;
    
    private Painel desenho;
    private int numeroLados = 3;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCirculo;
    private javax.swing.JButton btnContornoSolido;
    private javax.swing.JButton btnContornoTracejado;
    private javax.swing.JButton btnCorPrimaria;
    private javax.swing.JButton btnCorSecundaria;
    private javax.swing.JButton btnCorSolida;
    private javax.swing.JButton btnDesenholivre;
    private javax.swing.JButton btnEstrela;
    private javax.swing.JButton btnGradiente;
    private javax.swing.JButton btnLinha;
    private javax.swing.JButton btnPolyLinha;
    private javax.swing.JButton btnRetangulo;
    private javax.swing.JButton btnSemPreenchimento;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel lblContorno;
    private javax.swing.JLabel lblDashed;
    private javax.swing.JLabel lblEspessura;
    private javax.swing.JLabel lblMaisCores;
    private javax.swing.JLabel lblMouseCoords;
    private javax.swing.JLabel lblPreenchimento;
    private javax.swing.JPanel panelControls;
    private javax.swing.JPanel panelCoords;
    private javax.swing.JPanel panelCorFundo;
    private javax.swing.JPanel panelCorPrimaria;
    private javax.swing.JPanel panelCorSecundaria;
    private javax.swing.JPanel panelCores;
    private javax.swing.JPanel panelCoresRecentes;
    private javax.swing.JPanel panelCoresSelecionadas;
    private javax.swing.JPanel panelDesenho;
    private javax.swing.JPanel panelOpcoes;
    private javax.swing.JPanel panelSelectContorno;
    private javax.swing.JPanel panelSelectCores;
    private javax.swing.JPanel panelSelectForma;
    private javax.swing.JPanel panelSelectPreenchimento;
    private javax.swing.JTextField txtNumeroLados;
    // End of variables declaration//GEN-END:variables
}
