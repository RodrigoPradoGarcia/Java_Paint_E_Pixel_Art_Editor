package paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import paint.Painel.Painel;
import paint.Shapes.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class Desenho {

    private static class Tela extends JFrame
    {
        private static String[] opcoes = {"Desenho livre","Retângulo","Oval","Linha","Poli-linha","Estrela","Triângulo"};
        private static final String PATH = "D:\\projs\\Paint\\src\\paint";
        
        private final JButton btnUndo;
        private final JButton btnClear;
        private final JComboBox comboShape;
        private final JCheckBox checkFilled;
        private final JCheckBox checkGradient;
        private final JTextField txtLineWidth;
        private final JTextField txtDashLength;
        private final JCheckBox checkDashed;
        private Painel desenho;
        private JLabel labelMouseCoords;
        private JButton selectedFirstColor;
        private JButton selectedSecondColor;
        private JButton selectedBackgroundColor;
        private JButton salvar;
        private JButton btnOpen;
        private JButton contaGotas;
        private JTextField numerolados;
        
        private ButtonShape[] botoes =
        {
            new ButtonShape(new JButton("Desenho livre"/*new ImageIcon("assets/Pincelicone.png")*/),0,"Desenho livre"),
            new ButtonShape(new JButton("Retângulo"/*new ImageIcon("assets/Quadradoicone.png")*/),1,"Retângulo"),
            new ButtonShape(new JButton("Círculo"/*new ImageIcon("assets/Circuloicone.png")*/),2,"Círculo"),
            new ButtonShape(new JButton("Linha"/*new ImageIcon("assets/Linhaicone.png")*/),3,"Linha"),
            new ButtonShape(new JButton("PolyLinha"/*new ImageIcon("assets/Polylinhaicone.png")*/),4,"Poly Linha"),
            new ButtonShape(new JButton("Estrela"/*new ImageIcon("assets/Estrleaicone.png")*/),5,"Estrela")
        };
        
        private JButton btnEstrela;
        private JButton btnLinha;
        private JButton btnPolylinha;
        private JButton btnRetangulo;
        private JButton btnOval;
        private JButton btnPincel;
        
        private Color firstColor;
        private Color secondColor;
        private Color backgroundColor;
        
        private int selectedShape = 0;
        
        //Lita de cores
        private final LinkedList<JButton> coresRecentes = new LinkedList();
        private final LinkedList<JButton> coresPersonalizadas = new LinkedList();
        private JPanel painelCores;
        private JPanel inferior;
        
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
            int escolha = f.showDialog(Tela.this,"Abrir");
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
                        Tela t = new Tela( (Painel) in.readObject() );
                        t.selectedBackgroundColor.setBackground( t.desenho.getCorFundo() );
                    }
                    catch(ClassNotFoundException | IOException e)
                    {
                        JOptionPane.showMessageDialog(null,"Ocorreu um erro ao abrir o arquivo: " + e.getMessage(),"ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(Tela.this,"Arquivos " + extension + " não são compatíveis com arquivos .paint","ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        private void renderCores()
        {
            if(painelCores instanceof JPanel)
            inferior.remove(painelCores);
            painelCores = new JPanel();
            JLabel labCor = new JLabel("Cores Personalizadas: ");
            labCor.setFont(new Font("serif",Font.PLAIN,16));
            if(this.coresPersonalizadas.isEmpty())
            {
                labCor.setText(labCor.getText() + "    Nenhuma cor Criada");
            }
            painelCores.add(labCor);
            this.coresPersonalizadas.forEach(cor->{
                painelCores.add(cor);
            });
            inferior.add(painelCores,2);
        }
        
        private void addColor(Color cor)
        {
            JButton bot = new JButton();
            bot.setPreferredSize(new Dimension(30,30));
            bot.setBackground(cor);
            bot.setCursor(new Cursor(Cursor.HAND_CURSOR));
            bot.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    Tela.this.firstColor = bot.getBackground();
                    Tela.this.selectedFirstColor.setBackground(bot.getBackground());
                }
            });
            bot.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == 3)
                    {
                        Tela.this.secondColor = bot.getBackground();
                        Tela.this.selectedSecondColor.setBackground(bot.getBackground());
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
            if(this.coresPersonalizadas.size() == 9)
            {
                this.coresPersonalizadas.removeFirst();
            }
            this.coresPersonalizadas.addLast(bot);
            System.out.println("okok");
            this.renderCores();
        }
        
        public Tela(Painel pane)
        {
            super("Paint");
            
            this.desenho = pane;
            
            desenho.setBackground(new Color(255,255,255));
            
            firstColor = new Color(0,0,0);
            secondColor = new Color(128,128,128);
            backgroundColor = new Color(255,255,255);
            
            btnOpen = new JButton("Abrir Desenho");
            btnOpen.setFont(new Font("Serif",Font.PLAIN,14));
            
            btnUndo = new JButton("Desfazer");
            btnUndo.setFont(new Font("Serif",Font.PLAIN,14));
            
            btnClear = new JButton("Limpar tela");
            btnClear.setFont(new Font("Serif",Font.PLAIN,14));
            
            comboShape = new JComboBox(Tela.opcoes);
            comboShape.setMaximumRowCount(5);
            comboShape.setFont(new Font("Serif",Font.PLAIN,16));
            
            JLabel labelNumero = new JLabel("     Número de lados: ");
            labelNumero.setFont(new Font("Serif",Font.PLAIN,16));
            numerolados = new JTextField();
            numerolados.setPreferredSize(new Dimension(100,30));
            numerolados.setFont(new Font("Serif",Font.PLAIN,16));
            
            checkFilled = new JCheckBox("Formas preenchidas     ");
            checkFilled.setFont(new Font("Serif",Font.PLAIN,16));
            
            checkGradient = new JCheckBox("Usar degradê     ");
            checkGradient.setFont(new Font("Serif",Font.PLAIN,16));
            
            txtLineWidth = new JTextField("5");
            txtLineWidth.setPreferredSize(new Dimension(50,30));
            txtLineWidth.setFont(new Font("Serif",Font.PLAIN,16));
            
            txtDashLength = new JTextField("5");
            txtDashLength.setPreferredSize(new Dimension(50,30));
            txtDashLength.setFont(new Font("Serif",Font.PLAIN,16));
            
            checkDashed = new JCheckBox("Linha tracejada     ");
            checkDashed.setFont(new Font("Serif",Font.PLAIN,16));
            
            JLabel labelShape = new JLabel("     Desenhar forma: ");
            labelShape.setFont(new Font("Serif",Font.PLAIN,16));
            
            JLabel labelWidth = new JLabel("     Espessura da linha: ");
            labelWidth.setFont(new Font("Serif",Font.PLAIN,16));
            
            JLabel labelDash = new JLabel("Tamanho do tracejado: ");
            labelDash.setFont(new Font("Serif",Font.PLAIN,16));
            
            inferior = new JPanel();
            inferior.setLayout(new GridLayout(1,4));
            inferior.setBackground(new Color(200,200,200));
            inferior.setMinimumSize(new Dimension(1920,50));
            inferior.setPreferredSize(new Dimension(1919,60));
            
            labelMouseCoords = new JLabel("(0,0)");
            labelMouseCoords.setFont(new Font("Serif",Font.PLAIN,16));
            labelMouseCoords.setHorizontalAlignment(SwingConstants.RIGHT);
            
            JLabel corSelect = new JLabel("Cor primária: ");
            corSelect.setFont(new Font("Serif",Font.PLAIN,16));
            
            JLabel corSelect2 = new JLabel("     Cor secundária: ");
            corSelect2.setFont(new Font("Serif",Font.PLAIN,16));
            
            JLabel corFundo = new JLabel("     Cor de fundo: ");
            corFundo.setFont(new Font("Serif",Font.PLAIN,16));           
            
            selectedFirstColor = new JButton();
            selectedFirstColor.setPreferredSize(new Dimension(30,30));
            selectedFirstColor.setBackground(this.firstColor);
            selectedFirstColor.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            selectedSecondColor = new JButton();
            selectedSecondColor.setPreferredSize(new Dimension(30,30));
            selectedSecondColor.setBackground(this.secondColor);
            selectedSecondColor.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            selectedBackgroundColor = new JButton();
            selectedBackgroundColor.setPreferredSize(new Dimension(30,30));
            selectedBackgroundColor.setBackground(this.backgroundColor);
            selectedBackgroundColor.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            contaGotas = new JButton("Conta Gotas");
            contaGotas.setBackground(new Color(255,255,255));
            contaGotas.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            salvar = new JButton("Salvar Desenho");
            salvar.setFont(new Font("Serif",Font.PLAIN,14));
            
            /*
                criando o painel de cores
            */
            JPanel cores = new JPanel();
            for(int i=0;i<360-36;i+=36)
            {
              BtnCor btnCor = new BtnCor(Color.getHSBColor((float)i/360, 1, 1),new JButton());
              btnCor.getBotao().setPreferredSize(new Dimension(30,30));
              btnCor.getBotao().setBackground(btnCor.getCor());
              btnCor.getBotao().setCursor(new Cursor(Cursor.HAND_CURSOR));
              btnCor.getBotao().addActionListener(new ActionListener(){
                  @Override
                  public void actionPerformed(ActionEvent e)
                  {
                      Tela.this.firstColor = btnCor.getCor();
                      Tela.this.selectedFirstColor.setBackground(btnCor.getCor());
                  }
              });
              btnCor.getBotao().addMouseListener(new MouseListener(){
                  @Override
                  public void mouseClicked(MouseEvent e) {
                    if(e.getButton()==3)
                    {
                        Tela.this.secondColor = btnCor.getCor();
                        Tela.this.selectedSecondColor.setBackground(btnCor.getCor());
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
              coresRecentes.add(btnCor.getBotao());
            }
            
            JLabel lblCores = new JLabel("Cores: ");
            lblCores.setFont(new Font("serif",Font.PLAIN,16));
            cores.add(lblCores);
            for(JButton but : this.coresRecentes)
            {
                cores.add(but);
            }
            
            inferior.add(cores);
            
            JPanel coresPrimariaSecundaria = new JPanel();
            coresPrimariaSecundaria.setLayout(new FlowLayout());
            coresPrimariaSecundaria.add(corSelect);
            coresPrimariaSecundaria.add(this.selectedFirstColor);
            coresPrimariaSecundaria.add(corSelect2);
            coresPrimariaSecundaria.add(this.selectedSecondColor);
            coresPrimariaSecundaria.add(corFundo);
            coresPrimariaSecundaria.add(selectedBackgroundColor);
            
            inferior.add(coresPrimariaSecundaria);
            //inferior.add(this.contaGotas);           
            
            this.renderCores();
            inferior.add(this.painelCores);
            
            JPanel mouseCoords = new JPanel();
            FlowLayout fl = new FlowLayout();
            fl.setAlignment(FlowLayout.RIGHT);
            mouseCoords.setLayout(fl);
            labelMouseCoords.setFont(new Font("serif",Font.BOLD,20));
            mouseCoords.add(labelMouseCoords);
            
            inferior.add(mouseCoords);
            
            Box controles = Box.createVerticalBox();
            
            JPanel arquivo = new JPanel();
            GridLayout gl = new GridLayout(1,2);
            arquivo.setLayout(gl);
            arquivo.setBackground(new Color(153,153,153));
            
            JPanel arq1 = new JPanel();
            fl = new FlowLayout();
            fl.setAlignment(FlowLayout.LEFT);
            arq1.setLayout(fl);
                arq1.add(salvar);
                arq1.add(btnOpen);
            
            JPanel arq2 = new JPanel();
            fl = new FlowLayout();
            fl.setAlignment(FlowLayout.RIGHT);
            arq2.setLayout(fl);
                arq2.add(btnUndo);
                arq2.add(btnClear);
            
            arquivo.add(arq1);
            arquivo.add(arq2);
            
            JPanel undoControls = new JPanel();
            fl = new FlowLayout();
            fl.setAlignment(FlowLayout.LEFT);
            undoControls.setLayout(fl);
           undoControls.setBackground(new Color(200,200,200));
            
            JPanel controls = new JPanel();
            controls.setLayout(new FlowLayout());
            controls.setBackground(new Color(200,200,200));
            
            controls.add(labelShape);
            
            for(ButtonShape botao : this.botoes)
            {
                botao.getBotao().setToolTipText(botao.getTooltipText());
                botao.getBotao().setCursor(new Cursor(Cursor.HAND_CURSOR));
                controls.add(botao.getBotao());
            }
            
            for(int i=0;i<7;i++)
            {
                controls.add(new JLabel());
            }
            
            JButton polygon = new JButton("Polígono");
            polygon.setPreferredSize(new Dimension(100,30));
            controls.add(polygon);
            
            JLabel lblPolygon = new JLabel("Número de lados: ");
            lblPolygon.setFont(new Font("serif",Font.PLAIN,16));
            controls.add(lblPolygon);
            
            JTextField txtSides = new JTextField();
            txtSides.setPreferredSize(new Dimension(100,30));
            txtSides.setFont(new Font("serif",Font.PLAIN,16));
            controls.add(txtSides);
            
            JPanel controlsInferior = new JPanel();
            controlsInferior.setLayout(new GridLayout(1,2));
            controlsInferior.setBackground(new Color(200,200,200));
            
            JPanel painel1 = new JPanel();
            fl = new FlowLayout();
            fl.setAlignment(FlowLayout.LEFT);
            painel1.setLayout(fl);
                painel1.add(this.checkFilled);
                painel1.add(this.checkGradient);
                painel1.add(this.checkDashed);
            
            JPanel painel2 = new JPanel();
            fl = new FlowLayout();
            fl.setAlignment(FlowLayout.RIGHT);
            painel2.setLayout(fl);
                painel2.add(labelWidth);
                painel2.add(this.txtLineWidth);
                painel2.add(labelDash);
                painel2.add(this.txtDashLength);
            
            controlsInferior.add(painel1);
            controlsInferior.add(painel2);
            
            JScrollPane scr = new JScrollPane(arquivo);
            scr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scr.setPreferredSize(new Dimension(10,50));
            
            controles.add(scr);
            scr = new JScrollPane(controls);
            scr.setPreferredSize(new Dimension(10,40));
            controles.add(scr);
            scr = new JScrollPane(controlsInferior);
            scr.setPreferredSize(new Dimension(10,60));
            controles.add(scr);
        
            for(ButtonShape bott : Tela.this.botoes)
            {
                bott.getBotao().setBackground(new Color(255,255,255));
            }
            this.botoes[0].getBotao().setBackground(new Color(172, 222, 242));
            
            for(ButtonShape bot : this.botoes)
            {
                bot.getBotao().addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Tela.this.selectedShape = bot.getIndex();
                        for(ButtonShape bott : Tela.this.botoes)
                        {
                            bott.getBotao().setBackground(new Color(255,255,255));
                        }
                        bot.getBotao().setBackground(new Color(172, 222, 242));
                    }
                });
            }
            
            this.txtDashLength.addKeyListener(new KeyListener(){
                @Override
                public void keyTyped(KeyEvent e) {
                    
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    
                }

                @Override
                public void keyReleased(KeyEvent e) {
                   try
                   {
                        Integer.parseInt(Tela.this.txtDashLength.getText());
                   }
                   catch(NumberFormatException eee)
                   {
                       
                   }
                }
            });
            
            this.txtLineWidth.addKeyListener(new KeyListener(){
                @Override
                public void keyTyped(KeyEvent e) {
                    
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    try
                    {
                        Integer.parseInt(Tela.this.txtLineWidth.getText());
                        Tela.this.txtDashLength.setText(Tela.this.txtLineWidth.getText());
                    }
                    catch(NumberFormatException eee)
                    {
                        
                    }
                    
                   try
                   {
                        Integer.parseInt(Tela.this.txtDashLength.getText());
                   }
                   catch(NumberFormatException eee)
                   {
                       
                   }
                }
                
            });
            
            
            contaGotas.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(Tela.this.contaGotas.getBackground().equals(new Color(146, 208, 214)))
                    {
                        Tela.this.contaGotas.setBackground(new Color(255,255,255));
                    }
                    else
                    {
                        Tela.this.contaGotas.setBackground(new Color(146, 208, 214));
                    }
                }
            });
            
            btnOpen.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    Tela.this.lerArquivo();
                }
            });
            
            selectedBackgroundColor.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    Color corSelecionada = JColorChooser.showDialog(Tela.this,"Escolha uma cor",backgroundColor);
                    if(corSelecionada == null)
                    {
                        corSelecionada = new Color(0,0,0);
                    }
                    Tela.this.addColor(corSelecionada);
                    Tela.this.backgroundColor = corSelecionada;
                    Tela.this.selectedBackgroundColor.setBackground(Tela.this.backgroundColor);
                    Tela.this.desenho.trocarCorFundo(Tela.this.backgroundColor);
                    Borracha.corFundo = Tela.this.backgroundColor;
                }          
            });
            
            salvar.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    try
                    {
                        Tela.this.gravarArquivo();
                    }
                    catch(IOException erro)
                    {
                        JOptionPane.showMessageDialog(null,"Ocorreu um erro ao salvar o arquivo: "+erro.getMessage(),"ERRO",JOptionPane.ERROR_MESSAGE);
                    }    
                    
                }
            });
            
            desenho.addMouseMotionListener(new MouseMotionListener(){
                @Override
                public void mouseDragged(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    Tela.this.labelMouseCoords.setText("("+x+","+y+")");
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    Tela.this.labelMouseCoords.setText("("+x+","+y+")");
                    Tela.this.desenho.moverMouse(e.getPoint());
                }    
            });
            
            desenho.addMouseListener(new MouseListener() {
                @Override
                public void mousePressed(MouseEvent e) {
                   
                  if(e.getButton() == 3)
                  {
                    int lineWidth = Integer.parseInt(Tela.this.txtLineWidth.getText());
                    Tela.this.desenho.cliqueDireito(new Borracha( Tela.this.checkFilled.isSelected(),Tela.this.checkGradient.isSelected(),Tela.this.checkGradient.isSelected(),Integer.parseInt(Tela.this.txtLineWidth.getText()),Integer.parseInt(Tela.this.txtDashLength.getText()),Tela.this.firstColor,Tela.this.secondColor ),e.getPoint());
                    return;
                  }
                  
                  if(Tela.this.contaGotas.getBackground().equals(new Color(146, 208, 214)))
                  {
                      try
                      {
                        Robot r = new Robot();
                        Color cor = r.getPixelColor((int)e.getPoint().getX(),(int)e.getPoint().getY());
                        Tela.this.firstColor = cor;
                        Tela.this.selectedFirstColor.setBackground(cor);
                        Tela.this.contaGotas.setBackground(new Color(255,255,255));
                        return;
                      }
                      catch(AWTException ex)
                      {
                          JOptionPane.showMessageDialog(null,"Erro ao usar o conta gotas","ERRO",JOptionPane.ERROR_MESSAGE);
                      }
                  }
                    
                  try
                  {              
                    boolean filled = Tela.this.checkFilled.isSelected();
                    GradientPaint pente;
                    if(Tela.this.checkGradient.isSelected())
                    {
                        pente = new GradientPaint(new Point(0,0),Tela.this.firstColor,new Point(50,50),Tela.this.secondColor,true);
                    }
                    else
                    {
                        pente = new GradientPaint(new Point(0,0),Tela.this.firstColor,new Point(50,50),Tela.this.firstColor,true);
                    }
                    Stroke str;
                    if(Tela.this.checkDashed.isSelected())
                    {
                        int lineWidth = Integer.parseInt(Tela.this.txtLineWidth.getText());
                        int dashLength = Integer.parseInt(Tela.this.txtDashLength.getText());
                        float[] dashes = {dashLength};
                        str = new BasicStroke(lineWidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND, 0, dashes,0);
                    }
                    else
                    {
                        int lineWidth = Integer.parseInt(Tela.this.txtLineWidth.getText());
                        str = new BasicStroke(lineWidth);
                    }

                    switch(Tela.this.selectedShape)
                    {
                         case 0:
                            
                            int espessura = Integer.parseInt(Tela.this.txtLineWidth.getText());
                            Tela.this.desenho.clicar(new Lapis(Tela.this.checkFilled.isSelected(),Tela.this.checkGradient.isSelected(),Tela.this.checkDashed.isSelected(),Integer.parseInt(Tela.this.txtLineWidth.getText()),Integer.parseInt(Tela.this.txtDashLength.getText()),Tela.this.firstColor,Tela.this.secondColor),e.getPoint());
                            
                            break;
                             
                         case 1:

                            Tela.this.desenho.clicar(new paint.Shapes.Rectangle( Tela.this.checkFilled.isSelected() , Tela.this.checkGradient.isSelected() , Tela.this.checkDashed.isSelected() , Integer.parseInt(Tela.this.txtLineWidth.getText()) , Integer.parseInt(Tela.this.txtDashLength.getText()) , Tela.this.firstColor , Tela.this.secondColor,e.getPoint(), e.getPoint() ) , e.getPoint());

                            break;

                         case 2:

                            Tela.this.desenho.clicar( new paint.Shapes.Oval( Tela.this.checkFilled.isSelected() , Tela.this.checkGradient.isSelected() , Tela.this.checkDashed.isSelected() , Integer.parseInt(Tela.this.txtLineWidth.getText()) , Integer.parseInt(Tela.this.txtDashLength.getText()) , Tela.this.firstColor , Tela.this.secondColor,e.getPoint(), e.getPoint() ) , e.getPoint() ); 

                            break;
                             
                         case 3:
                             
                            Tela.this.desenho.clicar( new paint.Shapes.Line( Tela.this.checkFilled.isSelected() , Tela.this.checkGradient.isSelected() , Tela.this.checkDashed.isSelected() , Integer.parseInt(Tela.this.txtLineWidth.getText()) , Integer.parseInt(Tela.this.txtDashLength.getText()) , Tela.this.firstColor , Tela.this.secondColor , e.getPoint() , e.getPoint() ) , e.getPoint() ); 
                             
                            break;
                        
                         case 4:
                             
                             Tela.this.desenho.clicar(new paint.Shapes.Polylinha( Tela.this.checkFilled.isSelected() , Tela.this.checkGradient.isSelected() , Tela.this.checkDashed.isSelected() , Integer.parseInt(Tela.this.txtLineWidth.getText()) , Integer.parseInt(Tela.this.txtDashLength.getText()) , Tela.this.firstColor , Tela.this.secondColor ) , e.getPoint());
                             
                             break;
                         case 5:
                             Tela.this.desenho.clicar( new paint.Shapes.Star(Tela.this.checkFilled.isSelected(),Tela.this.checkGradient.isSelected(),Tela.this.checkDashed.isSelected(),Integer.parseInt(Tela.this.txtLineWidth.getText()),Integer.parseInt(Tela.this.txtDashLength.getText()),Tela.this.firstColor,Tela.this.secondColor,e.getPoint(),e.getPoint()) ,e.getPoint());
                             break;
                         case 6:
                             Tela.this.desenho.clicar(new paint.Shapes.Triangulo(3,Tela.this.checkFilled.isSelected(),Tela.this.checkGradient.isSelected(),Tela.this.checkDashed.isSelected(),Integer.parseInt(Tela.this.txtLineWidth.getText()),Integer.parseInt(Tela.this.txtDashLength.getText()),Tela.this.firstColor,Tela.this.secondColor,e.getPoint(),e.getPoint()) ,e.getPoint());
                             break;
                    }
                  }
                  catch(NumberFormatException erro)
                  {
                      JOptionPane.showMessageDialog(null,"Espessura da linha e Tamanho do tracejado precisam ser numéricos!","ERRO",JOptionPane.ERROR_MESSAGE);
                  }
                    
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    Tela.this.desenho.soltarMouse(e.getPoint());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    
                }
            });
            
            desenho.addMouseMotionListener(new MouseMotionListener(){
                @Override
                public void mouseDragged(MouseEvent e) {
                    Tela.this.desenho.arrastar(new Point(e.getX(),e.getY()));
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                }
                
            });
            
            btnUndo.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    desenho.undo();
                }                
            });
            
            btnClear.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    desenho.clear();
                }
            });
            
            selectedFirstColor.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    Color corSelecionada = JColorChooser.showDialog(Tela.this,"Escolha uma cor", firstColor);
                    if(corSelecionada == null)
                    {
                        corSelecionada = new Color(0,0,0);
                    }
                    Tela.this.addColor(corSelecionada);
                    Tela.this.firstColor = corSelecionada;
                    Tela.this.selectedFirstColor.setBackground(corSelecionada);
                }
            });
            
            selectedSecondColor.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    Color corSelecionada = JColorChooser.showDialog(Tela.this,"Escolha uma cor", firstColor);
                    if(corSelecionada == null)
                    {
                        corSelecionada = new Color(0,0,0);
                    }
                    Tela.this.addColor(corSelecionada);
                    Tela.this.secondColor = corSelecionada;
                    Tela.this.selectedSecondColor.setBackground(corSelecionada);
                }
            });
            
            /////////
            
            add(controles,BorderLayout.NORTH);
            add(new JScrollPane(desenho),BorderLayout.CENTER);
            scr = new JScrollPane(inferior);
            scr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            add(scr,BorderLayout.SOUTH);
            
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
            tela.setSize( new Dimension( (int)tela.getWidth() - 50, (int)tela.getHeight() - 50 ) );
            setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 50,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 50);
            setResizable(true);
            setVisible(true);
        }
    }
    
    public static void main(String[] args) {
        try
        {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Desenho.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Desenho.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Desenho.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Desenho.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        Painel des = new Painel(new Color(255,255,255));
        des.setPreferredSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
        new Tela(des); 
    }
}
