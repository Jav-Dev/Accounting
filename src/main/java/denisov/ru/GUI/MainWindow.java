package denisov.ru.GUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {
    public MainWindow(){
        /* ������ ��������, ������ � ��� ���������� ���� */
        super("Title");
        setSize(400,500);

        WelcomeWindow welcomeWindow = new WelcomeWindow();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (getContentPane().getClass().getName().equals("denisov.ru.GUI.WelcomeWindow$1"))
                {
                    System.exit(0);
                }
                if(getContentPane().getClass().getName().equals("denisov.ru.GUI.CatalogWindow$1"))
                {
                    System.exit(0);
                }
                if(getContentPane().getClass().getName().equals("denisov.ru.GUI.StatisticWindow$1"))
                {
                    System.exit(0);
                }
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        /*
        * ��������� � frame ������������ ������
        * */
        setContentPane(welcomeWindow.getContents());

        /* ������ ���� ������� */
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }




}
