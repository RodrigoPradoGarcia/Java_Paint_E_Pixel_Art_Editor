package paint;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

class BtnColor
{
    private final JPanel button;
    private final Color cor;

    public BtnColor(JPanel button, Color cor) {
        this.button = button;
        this.cor = cor;
    }

    public JPanel getButton() {
        return button;
    }

    public Color getCor() {
        return cor;
    }
}

public class PixelArt extends javax.swing.JFrame {
    
    public PixelArt() {
        super("Pixel Art Editor");
        initComponents();
        renderPanel(30,30);
        renderPanelColors();
    }
    
    private void abrirWorkspace()
    {
        JFileChooser f = new JFileChooser();
        f.setFileFilter(new FileNameExtensionFilter("Arte de Pixel Art Editor","pix"));
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int botao = f.showDialog(this, "Save Workspace");
        if(botao == JFileChooser.APPROVE_OPTION)
        {
            Path p = f.getSelectedFile().toPath();
            String ps = p.toString();
            if(ps.substring(ps.lastIndexOf(".")).equals(".pix"))
            {
                try
                {
                    ObjectInputStream inp = new ObjectInputStream(Files.newInputStream(p));
                    this.desenhoContainer.removeAll();
                    this.revalidate();
                    this.repaint();
                    this.painelDesenho = (JPanel)inp.readObject();
                    this.desenhoContainer.add(this.painelDesenho);
                    Component[] comps = this.painelDesenho.getComponents();
                    for(Component comp : comps)
                    {
                        this.addEventosDeClique((JPanel)comp);
                    }
                    this.getPixelMatrix();
                    this.revalidate();
                    this.repaint();
                }
                catch(IOException e)
                {
                    JOptionPane.showMessageDialog(null,"Erro ao salvar a workspace: "+e.getMessage(),"ERRO",JOptionPane.ERROR_MESSAGE);
                }
                catch(ClassNotFoundException e)
                {
                    JOptionPane.showMessageDialog(null,"Erro ao salvar a workspace: "+e.getMessage(),"ERRO",JOptionPane.ERROR_MESSAGE);
                }
                    
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Arquivo incompatível","ERRO",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void salvarWorkspace()
    {
        JFileChooser f = new JFileChooser();
        f.setFileFilter(new FileNameExtensionFilter("Arte de Pixel Art Editor","pix"));
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int botao = f.showDialog(this, "Save Workspace");
        if(botao == JFileChooser.APPROVE_OPTION)
        {
            Path p = f.getSelectedFile().toPath();
            String ps = p.toString();
            if(ps.substring(ps.lastIndexOf(".")).equals(".pix"))
            {
                try
                {
                    ObjectOutputStream str = new ObjectOutputStream(Files.newOutputStream(p));
                    str.writeObject(this.painelDesenho);
                }
                catch(IOException e)
                {
                    JOptionPane.showMessageDialog(null,"Erro ao salvar a workspace: "+e.getMessage(),"ERRO",JOptionPane.ERROR_MESSAGE);
                }
                    
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Arquivo incompatível","ERRO",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void salvar()
    {
        BufferedImage img = new BufferedImage(linhas*this.tamanhoImagem,colunas*this.tamanhoImagem,BufferedImage.TYPE_INT_RGB);
        Component[] arr = this.painelDesenho.getComponents();
        int l = 0;
        int col = 0;
        Graphics2D gg = img.createGraphics();
        for(var com : arr)
        {
            if(col%this.colunas==0)
            {
                col = 0;
                l++;
                if(l%this.linhas == 0)
                {
                    l = 0;
                }
            }
            gg.setColor(com.getBackground());
            if(l==0)
            {
                gg.fillRect(col*this.tamanhoImagem,(linhas-1)*this.tamanhoImagem,this.tamanhoImagem,this.tamanhoImagem);
            }
            else
            {
                gg.fillRect(col*this.tamanhoImagem,(l-1)*this.tamanhoImagem,this.tamanhoImagem,this.tamanhoImagem);
            }
            col++;
        }
        
        try {
            String[] extensoes = {".gif",".png",".jpeg",".jpg"};
            JFileChooser f = new JFileChooser();
            f.setFileFilter(new FileNameExtensionFilter("Arquivo de imagem","png","jpeg","jpg","gif"));
            int bt = f.showDialog(this,"Save");
            if(bt == JFileChooser.APPROVE_OPTION)
            {
                Path p = f.getSelectedFile().toPath();
                boolean contains = false;
                for(String ext : extensoes)
                {
                    if(p.toString().substring(p.toString().lastIndexOf(".")).equals(ext))
                    {
                        contains = true;
                    }
                }
                if(contains)
                {
                    ImageIO.write(img,p.toString().substring(p.toString().lastIndexOf(".")+1), new File(p.toString()));
                }
                else
                {
                    JOptionPane.showMessageDialog(this,"Formato "+p.toString().substring(p.toString().lastIndexOf("."))+" não é suportado\nFormatos suportados: .png, .jpg, .jpeg e .gif","ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,"Não foi possível salvar: "+ex.getMessage(),"ERRO",JOptionPane.ERROR_MESSAGE);
        }
        catch(StringIndexOutOfBoundsException e)
        {
            JOptionPane.showMessageDialog(this,"Extensão do arquivo não foi especificada","ERRO",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void preview()
    {
        JPanel des = new JPanel(){
            @Override
            public void paintComponent(Graphics gg)
            {
                Component[] arr = PixelArt.this.painelDesenho.getComponents();
                int l = 0;
                int col = 0;
                for(var com : arr)
                {
                    if(col%PixelArt.this.colunas==0)
                    {
                        col = 0;
                        l++;
                        if(l%PixelArt.this.linhas == 0)
                        {
                            l = 0;
                        }
                    }
                    gg.setColor(com.getBackground());
                    if(l==0)
                    {
                        gg.fillRect(col*PixelArt.this.tamanhoImagem,(linhas-1)*PixelArt.this.tamanhoImagem,PixelArt.this.tamanhoImagem,PixelArt.this.tamanhoImagem);
                    }
                    else
                    {
                        gg.fillRect(col*PixelArt.this.tamanhoImagem,(l-1)*PixelArt.this.tamanhoImagem,PixelArt.this.tamanhoImagem,PixelArt.this.tamanhoImagem);
                    }
                    col++;
                }
            }
        };
        
        des.setPreferredSize(new Dimension(this.tamanhoImagem * colunas , this.tamanhoImagem * linhas));
        
        JOptionPane.showMessageDialog(null,des,"Visualização",JOptionPane.PLAIN_MESSAGE);
    }
    
    private void pickColor(JButton botao)
    {
        PixelArt.this.selectedButton.setBackground(botao.getBackground());
        this.selectedButton.setBorder(null);
        PixelArt.this.selectedButton = null;
    }
    
    private void renderPanelColors()
    {
        for(int i=0;i<360-36;i+=36)
        {
            JButton btnCor = new JButton();
            btnCor.setPreferredSize(new Dimension(30,30));
            btnCor.setBackground(Color.getHSBColor((float)i/360, 1, 1));
            btnCor.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCor.setOpaque(false);
            btnCor.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    
                    if(PixelArt.this.selectedButton != null)
                    {
                        pickColor(btnCor);
                        return;
                    }
                    
                    if(e.getButton() == 1)
                    {
                        PixelArt.this.btnCorSelecionada.setBackground(btnCor.getBackground());
                    }
                    else if(e.getButton() == 3)
                    {
                        PixelArt.this.btnCorSecundaria.setBackground(btnCor.getBackground());
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
            this.panelColors.add(btnCor);
        }
            final JButton btnCor = new JButton();
            btnCor.setPreferredSize(new Dimension(30,30));
            btnCor.setBackground(new Color(0,0,0));
            btnCor.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCor.setOpaque(false);
            btnCor.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    
                    if(PixelArt.this.selectedButton != null)
                    {
                        pickColor(btnCor);
                        return;
                    }
                    
                    if(e.getButton() == 1)
                    {
                        PixelArt.this.btnCorSelecionada.setBackground(btnCor.getBackground());
                    }
                    else if(e.getButton() == 3)
                    {
                        PixelArt.this.btnCorSecundaria.setBackground(btnCor.getBackground());
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
            this.panelColors.add(btnCor);
        
            JButton btnCor3 = new JButton();
            btnCor3.setPreferredSize(new Dimension(20,20));
            btnCor3.setBackground(new Color(255,255,255));
            btnCor3.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCor3.setOpaque(false);
            btnCor3.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    
                    if(PixelArt.this.selectedButton != null)
                    {
                        pickColor(btnCor3);
                        return;
                    }
                    
                    if(e.getButton() == 1)
                    {
                        PixelArt.this.btnCorSelecionada.setBackground(btnCor3.getBackground());
                    }
                    else if(e.getButton() == 3)
                    {
                        PixelArt.this.btnCorSecundaria.setBackground(btnCor3.getBackground());
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
        this.panelColors.add(btnCor3);
        this.panelColors.revalidate();
        this.renderRecentColors();
    }
    
    private void renderRecentColors()
    {
        for(int i=0;i<12 - this.coresRecentes.size();i++)
        {
            JPanel p = new JPanel();
            p.setPreferredSize(new Dimension(20,20));
            p.setBackground(new Color(0,0,0,0));
            p.setBorder(new LineBorder(new Color(0,0,0),1,true));
            this.panelRecentColors.add(p);
        }
    }
    
    private void getPixelMatrix()
    {
        Component[] comps = this.painelDesenho.getComponents();
        int lines = 0;
        int cols = 0;
        this.matrixPanel = new Component[colunas][linhas];
        for(Component comp : comps)
        {
            matrixPanel[cols][lines] = comp;
            cols++;
            if(cols % colunas == 0)
            {
                cols = 0;
                lines++;
            }
        }
    }
    
    private void addEventosDeClique(JPanel novo)
    {
        novo.addMouseListener(new MouseListener(){
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                
                            }

                            @Override
                            public void mousePressed(MouseEvent e) {
                                if(PixelArt.this.selectedButton != null)
                                {
                                    JButton btn = new JButton();
                                    btn.setBackground(novo.getBackground());
                                    pickColor(btn);
                                    return;
                                }       
                                
                                if(PixelArt.this.btnBucket.getBackground().equals(new Color(255,255,255)))
                                {
                                    if(e.getButton() == 1)
                                    {
                                        if(e.isControlDown())
                                        {
                                            PixelArt.this.btnCorSelecionada.setBackground(novo.getBackground());
                                            return;
                                        }
                                        else
                                        {
                                            if(PixelArt.this.undo.size() == 16)
                                            {
                                                PixelArt.this.undo.removeLast();
                                            }
                                            PixelArt.this.undo.addFirst(new LinkedList());
                                            PixelArt.this.recursivePaint(novo.getBackground(), PixelArt.this.btnCorSelecionada.getBackground(), PixelArt.this.getPixelCoord(novo));
                                            return;
                                        }
                                    }
                                    else if(e.getButton() == 3)
                                    {
                                        if(e.isControlDown())
                                        {
                                            PixelArt.this.btnCorSecundaria.setBackground(novo.getBackground());
                                            return;
                                        }
                                        else
                                        {
                                            if(PixelArt.this.undo.size() == 16)
                                            {
                                                PixelArt.this.undo.removeLast();
                                            }
                                            PixelArt.this.undo.addFirst(new LinkedList());
                                            PixelArt.this.recursivePaint(novo.getBackground(), PixelArt.this.btnCorSecundaria.getBackground(), PixelArt.this.getPixelCoord(novo));
                                            return;
                                        }
                                    }
                                }
                                
                                PixelArt.this.isDrawing = true;
                                if(PixelArt.this.undo.size() == 16)
                                {
                                    PixelArt.this.undo.removeLast();
                                }
                                PixelArt.this.undo.addFirst(new LinkedList());
                                PixelArt.this.undo.getFirst().addFirst(new BtnColor(novo,novo.getBackground()));
                                if(e.getButton() == 1)
                                {
                                    if(e.isControlDown())
                                    {
                                        PixelArt.this.btnCorSelecionada.setBackground(novo.getBackground());
                                        return;
                                    }
                                    else
                                    {
                                        PixelArt.this.botaoMouse = e.getButton();
                                        novo.setBackground(PixelArt.this.btnCorSelecionada.getBackground());
                                    }
                                }
                                else if(e.getButton() == 3)
                                {
                                    if(e.isControlDown())
                                    {
                                        PixelArt.this.btnCorSecundaria.setBackground(novo.getBackground());
                                        return;   
                                    }
                                    else
                                    {
                                        PixelArt.this.botaoMouse = e.getButton();
                                        novo.setBackground(PixelArt.this.btnCorSecundaria.getBackground());
                                    }
                                }
                                float[] hsb = new float[3];
                                if(Color.RGBtoHSB(novo.getBackground().getRed(),novo.getBackground().getGreen(),novo.getBackground().getBlue(), hsb)[2] < 0.5)
                                {
                                    novo.setBorder(new LineBorder(new Color(255,255,255,128),1,false));
                                }
                                else
                                {
                                    novo.setBorder(new LineBorder(new Color(0,0,0,128),1,false));
                                }
                            }

                            @Override
                            public void mouseReleased(MouseEvent e) {
                                PixelArt.this.isDrawing = false;
                            }

                            @Override
                            public void mouseEntered(MouseEvent e) {
                                if(PixelArt.this.isDrawing)
                                {
                                    PixelArt.this.undo.getFirst().addFirst(new BtnColor(novo,novo.getBackground()));
                                    if(PixelArt.this.botaoMouse == 1)
                                    {
                                        novo.setBackground(PixelArt.this.btnCorSelecionada.getBackground());
                                    }
                                    else
                                    {
                                        novo.setBackground(PixelArt.this.btnCorSecundaria.getBackground());
                                    }
                                    float[] hsb = new float[3];
                                if(Color.RGBtoHSB(novo.getBackground().getRed(),novo.getBackground().getGreen(),novo.getBackground().getBlue(), hsb)[2] < 0.5)
                                {
                                    novo.setBorder(new LineBorder(new Color(255,255,255,128),1,false));
                                }
                                else
                                {
                                    novo.setBorder(new LineBorder(new Color(0,0,0,128),1,false));
                                }
                                }
                            }

                            @Override
                            public void mouseExited(MouseEvent e) {
                                
                            }
                            
                        });
    }
    
    private void renderPanel(int linhas,int colunas)
    {
                this.desenhoContainer.removeAll();
                this.painelDesenho = new JPanel();
                this.painelDesenho.setLayout(new GridLayout(linhas,colunas));
                this.painelDesenho.setBackground(new Color(120,120,120));
                painelDesenho.setPreferredSize(new Dimension(colunas*20,linhas*20));
                
                for(int i=0;i<linhas;i++)
                {
                    for(int j=0;j<colunas;j++)
                    {
                        JPanel novo = new JPanel();
                        novo.setPreferredSize(new Dimension(20,20));
                        novo.setBackground(new Color(255,255,255));
                        novo.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        novo.setBorder(new LineBorder(new Color(0, 0, 0, 128),1,false));
                        novo.addMouseWheelListener(new MouseWheelListener(){
                            @Override
                            public void mouseWheelMoved(MouseWheelEvent e) {
                                PixelArt.this.desenhoContainerMouseWheelMoved(e);
                            }
                        });
                        this.addEventosDeClique(novo);
                        this.painelDesenho.add(novo);
                    }
                }
                
                this.desenhoContainer.add(new JScrollPane(painelDesenho));
                this.revalidate();
                this.repaint();
                
                /*
                    criando a matriz
                */
                
                this.getPixelMatrix();
      }
   
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton7 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtTamanhoImagem = new javax.swing.JTextField();
        txtTamanho = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jSlider1 = new javax.swing.JSlider();
        jButton2 = new javax.swing.JButton();
        textZoom = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        desenhoContainer = new javax.swing.JPanel();
        painelDesenho = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        panelCores = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        panelColors = new javax.swing.JPanel();
        painelCoresRecentes = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        panelRecentColors = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        selectedColors = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnCorSelecionada = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnCorSecundaria = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        panelSelectMode = new javax.swing.JPanel();
        btnDesenhoLivre = new javax.swing.JButton();
        btnBucket = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        btnSubsSrc = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnSubsTarget = new javax.swing.JButton();
        btnSubstituir = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu5 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenu8 = new javax.swing.JMenu();

        jButton7.setText("jButton7");

        jPanel4.setBackground(new java.awt.Color(120, 120, 120));
        jPanel4.setPreferredSize(new java.awt.Dimension(300, 300));

        jPanel5.setBackground(new java.awt.Color(80, 80, 80));
        jPanel5.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 260, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 230, Short.MAX_VALUE))
        );

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jScrollPane3.setMaximumSize(new java.awt.Dimension(32767, 35));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(202, 60));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(202, 60));

        jPanel1.setBackground(new java.awt.Color(120, 120, 120));
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel1.setMinimumSize(new java.awt.Dimension(1000, 32));
        jPanel1.setPreferredSize(new java.awt.Dimension(980, 35));
        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        jPanel2.setBackground(new java.awt.Color(120, 120, 120));
        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 35));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButton1.setBackground(new java.awt.Color(31, 31, 31));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Desfazer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        jButton4.setBackground(new java.awt.Color(31, 31, 31));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Limpar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("          Escala da imagem:");
        jPanel2.add(jLabel4);

        txtTamanhoImagem.setBackground(new java.awt.Color(69, 73, 75));
        txtTamanhoImagem.setForeground(new java.awt.Color(255, 255, 255));
        txtTamanhoImagem.setPreferredSize(new java.awt.Dimension(100, 25));
        txtTamanhoImagem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTamanhoImagemKeyReleased(evt);
            }
        });
        jPanel2.add(txtTamanhoImagem);

        txtTamanho.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTamanho.setForeground(new java.awt.Color(255, 255, 255));
        txtTamanho.setText("30 x 30");
        jPanel2.add(txtTamanho);

        jPanel1.add(jPanel2);

        jPanel6.setBackground(new java.awt.Color(120, 120, 120));
        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel6.setPreferredSize(new java.awt.Dimension(100, 35));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jSlider1.setForeground(new java.awt.Color(31, 31, 31));
        jSlider1.setValue(11);
        jSlider1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jSlider1MouseReleased(evt);
            }
        });
        jPanel6.add(jSlider1);

        jButton2.setBackground(new java.awt.Color(31, 31, 31));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("-");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton2);

        textZoom.setBackground(new java.awt.Color(255, 255, 255));
        textZoom.setForeground(new java.awt.Color(255, 255, 255));
        textZoom.setText("20%");
        jPanel6.add(textZoom);

        jButton3.setBackground(new java.awt.Color(31, 31, 31));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("+");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton3);

        jPanel1.add(jPanel6);

        jScrollPane3.setViewportView(jPanel1);

        getContentPane().add(jScrollPane3);

        jScrollPane1.setMaximumSize(new java.awt.Dimension(32767, 32000));

        desenhoContainer.setBackground(new java.awt.Color(61, 61, 61));
        desenhoContainer.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                desenhoContainerMouseWheelMoved(evt);
            }
        });

        painelDesenho.setBackground(new java.awt.Color(255, 255, 255));
        painelDesenho.setOpaque(false);
        painelDesenho.setPreferredSize(new java.awt.Dimension(300, 300));
        painelDesenho.setLayout(new java.awt.GridLayout(1, 10));
        desenhoContainer.add(painelDesenho);

        jScrollPane1.setViewportView(desenhoContainer);

        getContentPane().add(jScrollPane1);

        jScrollPane2.setMaximumSize(new java.awt.Dimension(32767, 50));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(1002, 95));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(1002, 95));

        jPanel3.setBackground(new java.awt.Color(120, 120, 120));
        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel3.setMinimumSize(new java.awt.Dimension(700, 70));
        jPanel3.setPreferredSize(new java.awt.Dimension(700, 70));
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        panelCores.setBackground(new java.awt.Color(120, 120, 120));
        panelCores.setPreferredSize(new java.awt.Dimension(202, 60));
        panelCores.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Cores: ");
        panelCores.add(jLabel2);

        panelColors.setBackground(new java.awt.Color(120, 120, 120));
        panelColors.setPreferredSize(new java.awt.Dimension(145, 45));
        panelColors.setLayout(new java.awt.GridLayout(2, 6));
        panelCores.add(panelColors);

        jPanel3.add(panelCores);

        painelCoresRecentes.setBackground(new java.awt.Color(120, 120, 120));
        painelCoresRecentes.setPreferredSize(new java.awt.Dimension(253, 60));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Paleta: ");
        painelCoresRecentes.add(jLabel5);

        panelRecentColors.setBackground(new java.awt.Color(120, 120, 120));
        panelRecentColors.setPreferredSize(new java.awt.Dimension(145, 45));
        panelRecentColors.setLayout(new java.awt.GridLayout(2, 6));
        painelCoresRecentes.add(panelRecentColors);

        jPanel3.add(painelCoresRecentes);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Adicionar à paleta: ");
        jPanel3.add(jLabel6);

        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setMinimumSize(new java.awt.Dimension(60, 60));
        jButton5.setPreferredSize(new java.awt.Dimension(60, 60));
        jButton5.setIcon(new ImageIcon("assets/PaintIcons/img.png"));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton5);

        selectedColors.setBackground(new java.awt.Color(120, 120, 120));
        selectedColors.setAlignmentX(1.0F);
        selectedColors.setAlignmentY(1.0F);
        selectedColors.setPreferredSize(new java.awt.Dimension(180, 60));
        selectedColors.setLayout(new java.awt.GridLayout(2, 1));

        jPanel10.setBackground(new java.awt.Color(120, 120, 120));
        jPanel10.setPreferredSize(new java.awt.Dimension(50, 30));
        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("          Cor primária: ");
        jLabel1.setPreferredSize(new java.awt.Dimension(130, 20));
        jPanel10.add(jLabel1);

        btnCorSelecionada.setBackground(new java.awt.Color(0, 0, 0));
        btnCorSelecionada.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnCorSelecionada.setMaximumSize(new java.awt.Dimension(20, 20));
        btnCorSelecionada.setMinimumSize(null);
        btnCorSelecionada.setPreferredSize(new java.awt.Dimension(25, 25));
        btnCorSelecionada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorSelecionadaActionPerformed(evt);
            }
        });
        jPanel10.add(btnCorSelecionada);

        selectedColors.add(jPanel10);

        jPanel11.setBackground(new java.awt.Color(120, 120, 120));
        jPanel11.setPreferredSize(new java.awt.Dimension(50, 30));
        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("          Cor secundária: ");
        jLabel3.setPreferredSize(new java.awt.Dimension(130, 20));
        jPanel11.add(jLabel3);

        btnCorSecundaria.setBackground(new java.awt.Color(255, 255, 255));
        btnCorSecundaria.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnCorSecundaria.setMaximumSize(new java.awt.Dimension(20, 20));
        btnCorSecundaria.setMinimumSize(null);
        btnCorSecundaria.setPreferredSize(new java.awt.Dimension(25, 25));
        btnCorSecundaria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorSecundariaActionPerformed(evt);
            }
        });
        jPanel11.add(btnCorSecundaria);

        selectedColors.add(jPanel11);

        jPanel3.add(selectedColors);

        jScrollPane2.setViewportView(jPanel3);

        getContentPane().add(jScrollPane2);

        jPanel7.setBackground(new java.awt.Color(90, 90, 90));
        jPanel7.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel7.setPreferredSize(new java.awt.Dimension(50, 60));
        jPanel7.setLayout(new java.awt.GridLayout(1, 2));

        jScrollPane4.setMinimumSize(new java.awt.Dimension(622, 60));
        jScrollPane4.setPreferredSize(new java.awt.Dimension(622, 60));

        jPanel8.setBackground(new java.awt.Color(90, 90, 90));
        jPanel8.setMinimumSize(new java.awt.Dimension(366, 60));
        jPanel8.setPreferredSize(new java.awt.Dimension(620, 60));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        panelSelectMode.setBackground(new java.awt.Color(90, 90, 90));
        panelSelectMode.setPreferredSize(new java.awt.Dimension(300, 40));

        btnDesenhoLivre.setBackground(new java.awt.Color(31, 31, 31));
        btnDesenhoLivre.setForeground(new java.awt.Color(255, 255, 255));
        btnDesenhoLivre.setText("Desenho Livre");
        btnDesenhoLivre.setPreferredSize(new java.awt.Dimension(150, 30));
        btnDesenhoLivre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesenhoLivreActionPerformed(evt);
            }
        });
        panelSelectMode.add(btnDesenhoLivre);

        btnBucket.setBackground(new java.awt.Color(31, 31, 31));
        btnBucket.setForeground(new java.awt.Color(255, 255, 255));
        btnBucket.setText("Balde");
        btnBucket.setPreferredSize(new java.awt.Dimension(100, 30));
        btnBucket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBucketActionPerformed(evt);
            }
        });
        panelSelectMode.add(btnBucket);

        jPanel8.add(panelSelectMode);

        jPanel9.setBackground(new java.awt.Color(90, 90, 90));
        jPanel9.setPreferredSize(new java.awt.Dimension(300, 40));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Substituir: ");
        jPanel9.add(jLabel8);

        btnSubsSrc.setBackground(new java.awt.Color(255, 255, 255));
        btnSubsSrc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSubsSrc.setMaximumSize(new java.awt.Dimension(30, 30));
        btnSubsSrc.setMinimumSize(new java.awt.Dimension(30, 30));
        btnSubsSrc.setPreferredSize(new java.awt.Dimension(30, 30));
        btnSubsSrc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubsSrcActionPerformed(evt);
            }
        });
        jPanel9.add(btnSubsSrc);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("     Por:");
        jPanel9.add(jLabel7);

        btnSubsTarget.setBackground(new java.awt.Color(255, 255, 255));
        btnSubsTarget.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSubsTarget.setMaximumSize(new java.awt.Dimension(30, 30));
        btnSubsTarget.setMinimumSize(new java.awt.Dimension(30, 30));
        btnSubsTarget.setPreferredSize(new java.awt.Dimension(30, 30));
        btnSubsTarget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubsTargetActionPerformed(evt);
            }
        });
        jPanel9.add(btnSubsTarget);

        btnSubstituir.setBackground(new java.awt.Color(31, 31, 31));
        btnSubstituir.setForeground(new java.awt.Color(255, 255, 255));
        btnSubstituir.setText("Substituir");
        btnSubstituir.setPreferredSize(new java.awt.Dimension(80, 30));
        btnSubstituir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubstituirActionPerformed(evt);
            }
        });
        jPanel9.add(btnSubstituir);

        jPanel8.add(jPanel9);

        jScrollPane4.setViewportView(jPanel8);

        jPanel7.add(jScrollPane4);

        getContentPane().add(jPanel7);

        jMenuBar2.setBackground(new java.awt.Color(31, 31, 31));
        jMenuBar2.setBorder(null);
        jMenuBar2.setForeground(new java.awt.Color(255, 255, 255));

        jMenu5.setText("Nova Arte");
        jMenu5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu5MouseClicked(evt);
            }
        });
        jMenu5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu5ActionPerformed(evt);
            }
        });
        jMenuBar2.add(jMenu5);

        jMenu4.setText("VIsualizar Arte");
        jMenu4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu4MouseClicked(evt);
            }
        });
        jMenuBar2.add(jMenu4);

        jMenu3.setText("Salvar Como Imagem");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenuBar2.add(jMenu3);

        jMenu6.setText("Salvar Arte");
        jMenu6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu6MouseClicked(evt);
            }
        });
        jMenuBar2.add(jMenu6);

        jMenu7.setText("Sair");
        jMenu7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu7MouseClicked(evt);
            }
        });

        jMenu8.setText("Abrir Arte");
        jMenu8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu8MouseClicked(evt);
            }
        });
        jMenu7.add(jMenu8);

        jMenuBar2.add(jMenu7);

        setJMenuBar(jMenuBar2);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu5ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jMenu5ActionPerformed
 
    
    private void jMenu5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu5MouseClicked

        try
        {
            String entrada = JOptionPane.showInputDialog(this,"Tamanho do desenho: ");
            if(Pattern.matches("^[0-9]* [0-9]*$",entrada))
            {
                String[] entradas = entrada.split(" ");
                linhas = Integer.parseInt(entradas[0]);
                colunas = Integer.parseInt(entradas[1]);
                renderPanel(linhas,colunas);
            }
            else
            {
                JOptionPane.showMessageDialog(this,"Digite dois números inteiros separados por espaço","ERRO",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(NumberFormatException e)
        {
            JOptionPane.showMessageDialog(this,"Entrada precisa ser numérica!","ERRO",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenu5MouseClicked

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        // TODO add your handling code here:
        this.salvar();
    }//GEN-LAST:event_jMenu3MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(!this.undo.isEmpty())
        {
            this.undo.getFirst().forEach( val -> {
                val.getButton().setBackground(val.getCor());
                float[] hsb = new float[3];
                if(Color.RGBtoHSB(val.getButton().getBackground().getRed(),val.getButton().getBackground().getGreen(),val.getButton().getBackground().getBlue(), hsb)[2] < 0.5)
                {
                    val.getButton().setBorder(new LineBorder(new Color(255,255,255,128),1,false));
                }
                else
                {
                    val.getButton().setBorder(new LineBorder(new Color(0,0,0,128),1,false));
                }
            } );
            this.undo.removeFirst();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.zoomAmount -= 5;
        if(this.zoomAmount <= 10)
        {
            this.zoomAmount = 10;
        }
         if(this.zoomAmount >= 100)
         {
            this.zoomAmount = 100;
         }
         
         resizePanel();
    }//GEN-LAST:event_jButton2ActionPerformed

    
    private void resizePanel()
    {
        this.textZoom.setText(this.zoomAmount+"%");
        this.jSlider1.setValue((int)((double)(this.zoomAmount - 10)/(100-10)*100));
        this.desenhoContainer.removeAll();
        this.revalidate();
        this.repaint();
        this.painelDesenho.setPreferredSize(new Dimension(zoomAmount * colunas , zoomAmount * linhas));
        this.painelDesenho.setBackground(new Color(255,0,0));
        this.desenhoContainer.add(this.painelDesenho);
        this.revalidate();
        this.repaint();
    }
    
    private void desenhoContainerMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_desenhoContainerMouseWheelMoved
        // TODO add your handling code here:
        this.zoomAmount -= evt.getWheelRotation();
        if(this.zoomAmount >= 100)
        {
            this.zoomAmount = 100;
        }
        
        if(this.zoomAmount <= 10)
        {
            this.zoomAmount = 10;
        }
        
        resizePanel();
    }//GEN-LAST:event_desenhoContainerMouseWheelMoved

    private void jSlider1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseReleased
        // TODO add your handling code here:
        this.zoomAmount = (int)(((double)this.jSlider1.getValue()/100)*(100-10) + 10);
        resizePanel();
    }//GEN-LAST:event_jSlider1MouseReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        this.zoomAmount += 5;
        if(this.zoomAmount <= 10)
        {
            this.zoomAmount = 10;
        }
         if(this.zoomAmount >= 100)
         {
            this.zoomAmount = 100;
         }
         
         resizePanel();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        String[] opcoes = {"Sim","Não"};
        int botao = JOptionPane.showOptionDialog(null,"Tem certeza que deseja limpar a tela?","Limpar",JOptionPane.PLAIN_MESSAGE,JOptionPane.WARNING_MESSAGE,null,opcoes,0);
        if(botao == 0)
        {
            this.undo = new LinkedList();
            Component[] comps = this.painelDesenho.getComponents();
            
            for(Component comp : comps)
            {
                comp.setBackground(Color.WHITE);
                float[] hsb = new float[3];
                if(Color.RGBtoHSB(comp.getBackground().getRed(),comp.getBackground().getGreen(),comp.getBackground().getBlue(), hsb)[2] < 0.5)
                {
                    ((JPanel)comp).setBorder(new LineBorder(new Color(255,255,255,128),1,false));
                }
                else
                {
                    ((JPanel)comp).setBorder(new LineBorder(new Color(0,0,0,128),1,false));
                }                
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtTamanhoImagemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTamanhoImagemKeyReleased
        // TODO add your handling code here:
        try
        {
            this.tamanhoImagem = Integer.parseInt(this.txtTamanhoImagem.getText());
            this.txtTamanho.setText(this.tamanhoImagem*this.linhas + " x " + this.tamanhoImagem * colunas);
        }
        catch(NumberFormatException e)
        {
            this.tamanhoImagem = 1;
            this.txtTamanho.setText(this.tamanhoImagem*this.linhas + " x " + this.tamanhoImagem * colunas);
        }
    }//GEN-LAST:event_txtTamanhoImagemKeyReleased

    private void btnSubstituirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubstituirActionPerformed
        // TODO add your handling code here:
        Color corAtual = this.btnSubsSrc.getBackground();
        Color corSelecionada = this.btnSubsTarget.getBackground();
        if(corSelecionada != null)
        {
            this.btnSubsTarget.setBackground(corSelecionada);
        }
        
        Component[] comps = this.painelDesenho.getComponents();
        
        LinkedList<BtnColor> lista = new LinkedList();
        for(Component comp : comps)
        {
            if(comp.getBackground().equals(corAtual))
            {
                comp.setBackground(corSelecionada);
                float[] hsb = new float[3];
                if(Color.RGBtoHSB(comp.getBackground().getRed(),comp.getBackground().getGreen(),comp.getBackground().getBlue(), hsb)[2] < 0.5)
                {
                    ((JPanel)comp).setBorder(new LineBorder(new Color(255,255,255,128),1,false));
                }
                else
                {
                    ((JPanel)comp).setBorder(new LineBorder(new Color(0,0,0,128),1,false));
                }
                lista.add(new BtnColor((JPanel)comp,corAtual));
            }
        }
        if(this.undo.size()==16)
        {
            this.undo.removeLast();
        }
        this.undo.addFirst(lista);
    }//GEN-LAST:event_btnSubstituirActionPerformed

    private void btnSubsSrcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubsSrcActionPerformed
        // TODO add your handling code here:
        if(this.selectedButton != null)this.selectedButton.setBorder(null);
        this.selectedButton = this.btnSubsSrc;
        this.btnSubsSrc.setBorder(new LineBorder(Color.RED,2,true));
    }//GEN-LAST:event_btnSubsSrcActionPerformed

    private void btnSubsTargetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubsTargetActionPerformed
        // TODO add your handling code here:
        if(this.selectedButton != null)this.selectedButton.setBorder(null);
        this.selectedButton = this.btnSubsTarget;
        this.btnSubsTarget.setBorder(new LineBorder(Color.RED,2,true));
    }//GEN-LAST:event_btnSubsTargetActionPerformed

    private void jMenu4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu4MouseClicked
        // TODO add your handling code here:
        this.preview();
    }//GEN-LAST:event_jMenu4MouseClicked

    private void resetSelecionButtons()
    {
        Component[] comps = this.panelSelectMode.getComponents();
        for(Component comp : comps)
        {
            comp.setBackground(new Color(31,31,31));
            comp.setForeground(new Color(255,255,255));
        }
    }
    
    private void btnBucketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBucketActionPerformed
        // TODO add your handling code here:
        this.resetSelecionButtons();
        this.btnBucket.setBackground(new Color(255,255,255));
        this.btnBucket.setForeground(new Color(0,0,0));
    }//GEN-LAST:event_btnBucketActionPerformed

    private void btnDesenhoLivreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesenhoLivreActionPerformed
        // TODO add your handling code here:this.resetSelecionButtons();
        this.resetSelecionButtons();
        this.btnDesenhoLivre.setBackground(new Color(255,255,255));
        this.btnDesenhoLivre.setForeground(new Color(0,0,0));
        
    }//GEN-LAST:event_btnDesenhoLivreActionPerformed

    private void jMenu6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu6MouseClicked
        // TODO add your handling code here:
        this.salvarWorkspace();
    }//GEN-LAST:event_jMenu6MouseClicked

    private void jMenu7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu7MouseClicked
        // TODO add your handling code here:
        this.dispose();
        Programa p = new Programa();
        p.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        p.setVisible(true);
    }//GEN-LAST:event_jMenu7MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        Color cor = JColorChooser.showDialog(this, "Escolha uma cor",this.btnCorSelecionada.getBackground());
        if(cor != null)
        {
            this.addColor(cor);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnCorSelecionadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorSelecionadaActionPerformed
        // TODO add your handling code here:
        if(this.selectedButton != null)
        {
            pickColor(this.btnCorSelecionada);
        }
    }//GEN-LAST:event_btnCorSelecionadaActionPerformed

    private void btnCorSecundariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorSecundariaActionPerformed
        // TODO add your handling code here:
        if(this.selectedButton != null)
        {
            pickColor(this.btnCorSecundaria);
        }
    }//GEN-LAST:event_btnCorSecundariaActionPerformed

    private void jMenu8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu8MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu8MouseClicked

    private void addColor(Color cor)
    {
        if(this.coresRecentes.size() == 12)
        {
            this.coresRecentes.removeFirst();
        }
        JButton corB = new JButton();
        corB.setPreferredSize(new Dimension(30,30));
        corB.setBackground(cor);
        corB.setCursor(new Cursor(Cursor.HAND_CURSOR));
        corB.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(PixelArt.this.selectedButton != null)
                {
                    pickColor(corB);
                    return;
                }
                
                if(e.getButton() == 1)
                {
                    PixelArt.this.btnCorSelecionada.setBackground(corB.getBackground());
                }
                
                if(e.getButton() == 3)
                {
                    PixelArt.this.btnCorSecundaria.setBackground(corB.getBackground());
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
        this.coresRecentes.add(corB);
        this.renderColors();
    }
    
    private void renderColors()
    {
        this.panelRecentColors.removeAll();
        this.coresRecentes.forEach( btnCor -> {
            this.panelRecentColors.add(btnCor);
        } );
        this.renderRecentColors();
        this.revalidate();
        this.repaint();
    }   
    
    private Point getPixelCoord(JPanel pixel)
    {
        for(int i=0;i<this.matrixPanel.length;i++)
        {
            for(int j=0;j<this.matrixPanel[i].length;j++)
            {
                if(this.matrixPanel[i][j].equals(pixel))
                {
                    return new Point(i,j);
                }
            }
        }
        throw new Error("Essa linha de código não deveria ser executada");
    }
    
    private Point[] getNeighbors(Point ponto)
    {
        Point[] pontos = new Point[4];
        pontos[0] = new Point((int)ponto.getX()-1,(int)ponto.getY());
        pontos[1] = new Point((int)ponto.getX()+1,(int)ponto.getY());
        pontos[2] = new Point((int)ponto.getX(),(int)ponto.getY()-1);
        pontos[3] = new Point((int)ponto.getX(),(int)ponto.getY()+1);
        java.util.List<Point> ps = java.util.List.of(pontos);
        Object[] filtrado = ps.stream().filter( p -> p.getX() > -1 && p.getY() > -1 && p.getX() < this.colunas && p.getY() < this.linhas ).toArray();
        pontos = new Point[filtrado.length];
        int i = 0;
        for(Object obj : filtrado)
        {
            pontos[i++] = (Point)obj;
        }
        
        return pontos;
    }
    
    private void recursivePaint(Color original, Color replace, Point ponto)
    {
        if(original.equals(replace))
        {
            return;
        }
        Point[] neighbors = this.getNeighbors(ponto);
        for(Point neighbor : neighbors)
        {
            if(this.matrixPanel[(int)neighbor.getX()][(int)neighbor.getY()].getBackground().equals(original))
            {
                int vermelho = original.getRed() == 255 ? 0 : original.getRed();
                int verde = original.getGreen() == 255 ? 0 : original.getGreen();
                int azul = original.getBlue() == 255 ? 0 : original.getBlue();
                this.matrixPanel[(int)neighbor.getX()][(int)neighbor.getY()].setBackground(new Color(vermelho+1,verde+1,azul+1));
                this.recursivePaint(original, replace, neighbor);
            }
        }
        this.undo.getFirst().add(new BtnColor((JPanel)this.matrixPanel[(int)ponto.getX()][(int)ponto.getY()],original));
        this.matrixPanel[(int)ponto.getX()][(int)ponto.getY()].setBackground(replace);
        Component comp = this.matrixPanel[(int)ponto.getX()][(int)ponto.getY()];
        float[] hsb = new float[3];
        if(Color.RGBtoHSB(comp.getBackground().getRed(),comp.getBackground().getGreen(),comp.getBackground().getBlue(), hsb)[2] < 0.5)
        {
            ((JPanel)comp).setBorder(new LineBorder(new Color(255,255,255,128),1,false));
        }
        else
        {
            ((JPanel)comp).setBorder(new LineBorder(new Color(0,0,0,128),1,false));
        }        
    }
    
    private int tamanhoImagem = 1;
    private LinkedList<JButton> coresRecentes = new LinkedList();
    private int zoomAmount = 20;
    private LinkedList<LinkedList<BtnColor>> undo = new LinkedList();
    private int linhas = 30;
    private int colunas = 30;
    private boolean isDrawing;
    private int numLinhas;
    private int numColunas;
    private int botaoMouse = 0;
    
    private JButton selectedButton;
    
    private boolean isBucket;
    
    private Component[][] matrixPanel;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBucket;
    private javax.swing.JButton btnCorSecundaria;
    private javax.swing.JButton btnCorSelecionada;
    private javax.swing.JButton btnDesenhoLivre;
    private javax.swing.JButton btnSubsSrc;
    private javax.swing.JButton btnSubsTarget;
    private javax.swing.JButton btnSubstituir;
    private javax.swing.JPanel desenhoContainer;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel painelCoresRecentes;
    private javax.swing.JPanel painelDesenho;
    private javax.swing.JPanel panelColors;
    private javax.swing.JPanel panelCores;
    private javax.swing.JPanel panelRecentColors;
    private javax.swing.JPanel panelSelectMode;
    private javax.swing.JPanel selectedColors;
    private javax.swing.JLabel textZoom;
    private javax.swing.JLabel txtTamanho;
    private javax.swing.JTextField txtTamanhoImagem;
    // End of variables declaration//GEN-END:variables
}
