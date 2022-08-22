package denisov.ru.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.Component.CENTER_ALIGNMENT;

public class WelcomeWindow {

    private JPanel contents;// панель для размещения всех компонентов
    private JLabel title;// название программы
    private JButton catalog,statistic;// кнопки отвечающие за переход по траницам
    private CatalogWindow catalogWindow; // Объект нужный для перемещения в окно с каталогами

    public WelcomeWindow()
    {
        /*
        * Создаем объект класса CatalogWindow
        * */
        catalogWindow = new CatalogWindow();
        /*
        *   Объявляем кнопку "Каталог" и присваеваем ей слушателя
        * */
        catalog = createButton("Каталог",CENTER_ALIGNMENT,new Font("Arial",Font.PLAIN,20));
        catalog.addActionListener(createListenerForCatalog());
        catalog.setFocusPainted(false);
        /*
        * Объявляем кнопку "Статистика"
        * */
        statistic = createButton("Статистика",CENTER_ALIGNMENT,new Font("Arial",Font.PLAIN,20));
        statistic.addActionListener(createStatisticWindow());
        statistic.setFocusPainted(false);

        /* объявляем надпись */
        title = new JLabel("title");
        title.setAlignmentX(CENTER_ALIGNMENT);
        /*
        * Объявляем главную панель куда будем складывать все компоненты
        * */
        contents = new JPanel() {
            /*
            * переопределяем метод paintComponent чтобы задать цвет панеле
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

        /* задаем менеджер расположения панели и добавляем кнопки на панель,затем выводим панель*/
        contents.setLayout(new BoxLayout(contents,BoxLayout.Y_AXIS));
        contents.add(title);
        contents.add(Box.createRigidArea(new Dimension(0,100)));
        contents.add(catalog);
        /* создаем отступ между компонентами */
        contents.add(Box.createRigidArea(new Dimension(0,10)));
        contents.add(statistic);
    }
/*
* Метод создающий кнопки
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
* Метод возвращающий слушателя для кнопки "Каталог"
* */
    private ActionListener createListenerForCatalog()
    {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                * Создаем объект CatalogWindow
                * */
                catalogWindow = new CatalogWindow();
                /*
                * Достаем все кнопки из json файла
                * */
                catalogWindow.initialization();
                /*
                * Задаем размеры фрейму и удаляем предыдущую панель из фрейма
                * */
                Main.mainWindow.setResizable(true);
                Main.mainWindow.setSize(new Dimension(700,800));
                Main.mainWindow.setLocationRelativeTo(null);
                Main.mainWindow.remove(Main.mainWindow.getContentPane());
                /*
                * Добавляем панель из класса CatalogWindow и добавляем слушателя для фрейма
                * */
                Main.mainWindow.setContentPane(catalogWindow.getCatalogPanel());
                /*
                * Перерисовываем фрейм
                * */
                Main.mainWindow.revalidate();
                Main.mainWindow.repaint();
            }
        };
        return actionListener;
    }
    /*
    * создание окна со статистикой
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
                 * Добавляем панель из класса CatalogWindow и добавляем слушателя для фрейма
                 * */
                Main.mainWindow.setContentPane(statisticWindow.getMainpanel());
                /*
                 * Перерисовываем фрейм
                 * */
                Main.mainWindow.revalidate();
                Main.mainWindow.repaint();
            }
        };
        return actionListener;
    }

/*
* Метод возвращает главную панель класса WelcomeWindow
* */
    public JPanel getContents()
    {
        return contents;
    }
}
