package paint;

import javax.swing.*;

public class ButtonShape
{
    private final JButton botao;
    private final int index;
    private final String tooltipText;

    public ButtonShape(JButton botao, int index,String tooltipText) {
        this.botao = botao;
        this.index = index;
        this.tooltipText = tooltipText;
    }

    public JButton getBotao() {
        return botao;
    }
    public int getIndex() {
        return index;
    }

    public String getTooltipText() {
        return tooltipText;
    }
    
}
