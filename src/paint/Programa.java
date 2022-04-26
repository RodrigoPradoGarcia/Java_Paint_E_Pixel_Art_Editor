package paint;

import javax.imageio.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.border.LineBorder;
import paint.Painel.Painel;

public class Programa extends javax.swing.JFrame {

    public Programa() {
        initComponents();
        JPanel p = new JPanel(){
            @Override
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                try
                {
                    BufferedImage resized = new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
                    BufferedImage img = ImageIO.read(new File("assets/PaintIcons/aquarela.jpg"));
                    Graphics gg = resized.createGraphics();
                    gg.drawImage(img,0,0,200,200, null);
                    g.drawImage(resized,0,0,null);
                }
                catch(IOException e)
                {
                    System.err.println("Erro: "+e.getMessage());
                } 
            }
        };          
        this.panelPaint.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.panelPaint.setLayout(new CardLayout());
        
        this.panelPaint.setBorder(new LineBorder(new Color(255,255,255),4,false));
        this.panelPaint.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                Programa.this.panelPaint.setBorder(new LineBorder(new Color(255,128,0),4,false));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                Programa.this.panelPaint.setBorder(new LineBorder(new Color(255,255,255),4,false));
            }
        });
        this.panelPaint.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e)
            {
                Programa.this.dispose();
                Painel p = new Painel(new Color(61,61,61));
                Paint pa = new Paint(p);
                try
                {
                    BufferedImage img = new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
                    BufferedImage iii = ImageIO.read(new File("assets/PaintIcons/aquarela.jpg"));
                    Graphics g = img.getGraphics();
                    g.drawImage(iii,0,0,200,200,null);
                    pa.setIconImage(img);
                }
                catch(IOException er)
                {
                    System.err.println("Erro: "+er.getMessage());
                }
                pa.setTitle("Paint");
                pa.setSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 50,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 50));
                pa.setVisible(true);
            }
        });
        this.panelPaint.add(p);
        
        p = new JPanel(){
            @Override
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                try
                {
                    BufferedImage img = ImageIO.read(new File("assets/PixelArtIcons/pixelHeart.jpg"));
                    BufferedImage resized = new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
                    Graphics gg = resized.getGraphics();
                    gg.drawImage(img,0,0,200,200,null);
                    g.drawImage(resized,0,0,null);
                    
                }
                catch(IOException e)
                {
                    System.err.println("Erro: "+e.getMessage());
                }
            }
        };
        this.panelPixel.setLayout(new CardLayout());
        this.panelPixel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.panelPixel.setBorder(new LineBorder(new Color(255,255,255),4,false));
        this.panelPixel.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                Programa.this.panelPixel.setBorder(new LineBorder(new Color(255,128,0),4,false));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                Programa.this.panelPixel.setBorder(new LineBorder(new Color(255,255,255),4,false));
            }
        });
        this.panelPixel.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                Programa.this.dispose();
                PixelArt p = new PixelArt();
                try
                {
                    BufferedImage img = new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
                    BufferedImage iii = ImageIO.read(new File("assets/PixelArtIcons/pixelHeart.jpg"));
                    Graphics g = img.getGraphics();
                    g.drawImage(iii,0,0,200,200,null);
                    p.setIconImage(img);
                }
                catch(IOException er)
                {
                    System.err.println("Erro: "+er.getMessage());
                }
                p.setTitle("Pixel Art Editor");
                p.setSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 50,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 50));
                p.setVisible(true);
            }
        });
        this.panelPixel.add(p);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        panelPaint = new javax.swing.JPanel();
        panelPixel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel2.setBackground(new java.awt.Color(61, 61, 61));

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel3.setBackground(new java.awt.Color(31, 31, 31));
        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 50));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Bem Vindo!");
        jPanel3.add(jLabel1);

        jPanel1.add(jPanel3);

        jPanel4.setBackground(new java.awt.Color(31, 31, 31));
        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 100));
        jPanel4.setMinimumSize(new java.awt.Dimension(50, 50));
        jPanel4.setPreferredSize(new java.awt.Dimension(50, 50));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Para onde quer ir?");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(338, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(335, 335, 335))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(26, 26, 26))
        );

        jPanel1.add(jPanel4);

        jPanel5.setBackground(new java.awt.Color(31, 31, 31));
        jPanel5.setPreferredSize(new java.awt.Dimension(10, 50));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Paint");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Pixel Art Editor");

        panelPaint.setPreferredSize(new java.awt.Dimension(200, 200));

        javax.swing.GroupLayout panelPaintLayout = new javax.swing.GroupLayout(panelPaint);
        panelPaint.setLayout(panelPaintLayout);
        panelPaintLayout.setHorizontalGroup(
            panelPaintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        panelPaintLayout.setVerticalGroup(
            panelPaintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );

        panelPixel.setPreferredSize(new java.awt.Dimension(200, 200));

        javax.swing.GroupLayout panelPixelLayout = new javax.swing.GroupLayout(panelPixel);
        panelPixel.setLayout(panelPixelLayout);
        panelPixelLayout.setHorizontalGroup(
            panelPixelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        panelPixelLayout.setVerticalGroup(
            panelPixelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(201, 201, 201)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(187, 187, 187))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(panelPaint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 154, Short.MAX_VALUE)
                .addComponent(panelPixel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelPaint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelPixel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel5);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 836, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Programa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Programa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Programa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Programa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Programa p = new Programa();
                p.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 50,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 50);
                p.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel panelPaint;
    private javax.swing.JPanel panelPixel;
    // End of variables declaration//GEN-END:variables
}
