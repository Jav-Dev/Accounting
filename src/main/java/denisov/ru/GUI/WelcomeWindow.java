package denisov.ru.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.Component.CENTER_ALIGNMENT;

public class WelcomeWindow {

    private JPanel contents;// ������ ��� ���������� ���� �����������
    private JLabel title;// �������� ���������
    private JButton catalog,statistic;// ������ ���������� �� ������� �� ��������
    private CatalogWindow catalogWindow; // ������ ������ ��� ����������� � ���� � ����������

    public WelcomeWindow()
    {
        /*
        * ������� ������ ������ CatalogWindow
        * */
        catalogWindow = new CatalogWindow();
        /*
        *   ��������� ������ "�������" � ����������� �� ���������
        * */
        catalog = createButton("�������",CENTER_ALIGNMENT,new Font("Arial",Font.PLAIN,20));
        catalog.addActionListener(createListenerForCatalog());
        catalog.setFocusPainted(false);
        /*
        * ��������� ������ "����������"
        * */
        statistic = createButton("����������",CENTER_ALIGNMENT,new Font("Arial",Font.PLAIN,20));
        statistic.addActionListener(createStatisticWindow());
        statistic.setFocusPainted(false);

        /* ��������� ������� */
        title = new JLabel("title");
        title.setAlignmentX(CENTER_ALIGNMENT);
        /*
        * ��������� ������� ������ ���� ����� ���������� ��� ����������
        * */
        contents = new JPanel() {
            /*
            * �������������� ����� paintComponent ����� ������ ���� ������
            * */
            @Override
            public void paintComponent(Graphics g)
            {
                super.paintComponents(g);
                Graphics2D g2d =(Graphics2D) g;
                int w = getWidth();
                int h = getHeight();

                GradientPaint grd = new GradientPaint(0,0,Color.decode("#ee0979"),w,h,Color.decode("#ff6a00"));
                g2d.setPaint(grd);
                g2d.fillRect(0,0,w,h);

            }
        };

        /* ������ �������� ������������ ������ � ��������� ������ �� ������,����� ������� ������*/
        contents.setLayout(new BoxLayout(contents,BoxLayout.Y_AXIS));
        contents.add(title);
        contents.add(Box.createRigidArea(new Dimension(0,100)));
        contents.add(catalog);
        /* ������� ������ ����� ������������ */
        contents.add(Box.createRigidArea(new Dimension(0,10)));
        contents.add(statistic);
    }
/*
* ����� ��������� ������
* */
    private JButton createButton(String title,float alignment,Font font)
    {
        JButton button = new JButton(title);
        button.setAlignmentX(alignment);
        button.setFont(font);
        button.setMinimumSize(new Dimension(200,80));
        button.setMaximumSize(new Dimension(200,100));
        return button;
    }
/*
* ����� ������������ ��������� ��� ������ "�������"
* */
    private ActionListener createListenerForCatalog()
    {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                * ������� ������ CatalogWindow
                * */
                catalogWindow = new CatalogWindow();
                /*
                * ������� ��� ������ �� json �����
                * */
                catalogWindow.initialization();
                /*
                * ������ ������� ������ � ������� ���������� ������ �� ������
                * */
                Main.mainWindow.setResizable(true);
                Main.mainWindow.setSize(new Dimension(700,800));
                Main.mainWindow.setLocationRelativeTo(null);
                Main.mainWindow.remove(Main.mainWindow.getContentPane());
                /*
                * ��������� ������ �� ������ CatalogWindow � ��������� ��������� ��� ������
                * */
                Main.mainWindow.setContentPane(catalogWindow.getCatalogPanel());
                /*
                * �������������� �����
                * */
                Main.mainWindow.revalidate();
                Main.mainWindow.repaint();
            }
        };
        return actionListener;
    }
    /*
    * �������� ���� �� �����������
    * */
    private ActionListener createStatisticWindow()
    {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StatisticWindow statisticWindow = new StatisticWindow();

                Main.mainWindow.setResizable(true);
                Main.mainWindow.setSize(new Dimension(1000,800));
                Main.mainWindow.setLocationRelativeTo(null);
                Main.mainWindow.remove(Main.mainWindow.getContentPane());
                /*
                 * ��������� ������ �� ������ CatalogWindow � ��������� ��������� ��� ������
                 * */
                Main.mainWindow.setContentPane(statisticWindow.getMainpanel());
                /*
                 * �������������� �����
                 * */
                Main.mainWindow.revalidate();
                Main.mainWindow.repaint();
            }
        };
        return actionListener;
    }

/*
* ����� ���������� ������� ������ ������ WelcomeWindow
* */
    public JPanel getContents()
    {
        return contents;
    }
}
