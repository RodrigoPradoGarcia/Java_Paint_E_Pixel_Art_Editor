package paint;

import java.awt.*;
import javax.swing.*;

public class BtnCor
{
    private final Color cor;
    private final JButton botao;

    public BtnCor(Color cor, JButton botao) {
        this.cor = cor;
        this.botao = botao;
    }

    public Color getCor() {
        return cor;
    }

    public JButton getBotao() {
        return botao;
    }  
}
