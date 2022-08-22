package denisov.ru.GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import denisov.ru.Services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static java.awt.Component.CENTER_ALIGNMENT;

public class CatalogWindow {

    private HashMap<AxiliusJButton,Component> datacomponents;// map нужный для последующей инициализации компонентов на панель
    private ArrayList<AxiliusJButton> databuttons;// list хранящий данные о кнопках, нужен для сохранения данных в json
    private JPanel catalogPanel,EditPanel,panelForButton,pane,panelback;//Здесь catalogPanel главная панель
    private ProductService productService;// Объект нужен для запросов в базу данных

    public CatalogWindow()
    {
        /*
        * Объект отвечает за запросы в базу данных
        * */
        productService = new ProductService();
        /*
        * Отвечает за хранение компонентов на панеле pane
        * */
        datacomponents = new HashMap<>();
        /*
        * Отвечает за хранение данных о кнопках
        * */
        databuttons = new ArrayList<>();
        /*
        * Главная панель, которая возвращается в класс MainWindow и устанавливается в ContentPane
        * */
        catalogPanel = new JPanel(new BorderLayout()){
            /*
            * Задаем панеле catalogPanel цвет
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
        /*
        * Отвечает за кнопки "Добавить" или "Удалить"
        * */
        EditPanel = new JPanel();
        /*
        * Панель в которую добавляется панель pane с кнопками
        * */
        panelForButton = new JPanel();
        /*
        * Панель для добавление кнопок
        * */
        pane = new JPanel();
        /*
        * Панель отвечает за кнопку назад
        * */
        panelback = new JPanel();
        /*
        * Кнопка отвечает за возвращения на предыдущее окно
        * */
        JButton buttonback = new JButton("Назад");
        buttonback.addActionListener(getBack());
        buttonback.setMinimumSize(new Dimension(100,40));
        buttonback.setMaximumSize(new Dimension(120,50));
        /*
        * Добавление кнопки "Назад" на панель panelback
        * */
        panelback.add(buttonback);
        panelback.setOpaque(false);
        /*
        * Для панели с кнопками устанавливается Выравнивание по Y
        * */
        pane.setLayout(new BoxLayout(pane,BoxLayout.Y_AXIS));
        panelForButton.setLayout(new GridBagLayout());
        EditPanel.setLayout(new BoxLayout(EditPanel,BoxLayout.Y_AXIS));
        pane.setOpaque(false);
        panelForButton.setOpaque(false);
        EditPanel.setOpaque(false);
        /*
        * Задается кнопка добавления категории
        * */
        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(createCategory());
        addButton.setFocusPainted(false);
        addButton.setMinimumSize(new Dimension(100,40));
        addButton.setMaximumSize(new Dimension(120,50));
        /*
         * Задается кнопка удаления категории
         * */
        JButton removeButton = new JButton("Удалить");
        removeButton.addActionListener(deleteButton());
        removeButton.setFocusPainted(false);
        removeButton.setMinimumSize(new Dimension(100,40));
        removeButton.setMaximumSize(new Dimension(120,50));
        /*
        * Добавление кнопок удалить и добавить категорию в EditPanel
        * */
        EditPanel.add(addButton);
        EditPanel.add(removeButton);
        /*
        * добавление панелей в главную панель
        * */
        catalogPanel.add(EditPanel,"North");
        catalogPanel.add(panelback,"South");
    }
/*
* Метод отвечающий за создание категории
* */
    private ActionListener createCategory()
    {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                * Задается название категории
                * */
                String name = JOptionPane.showInputDialog("Введите название категории");

                if(name != null)
                {
                    /*
                     * Создание новой кнопки
                     * */
                    JButton but = new JButton(name);
                    but.setFocusPainted(false);
                    but.addActionListener(getActionListener());
                    but.setFont(new Font("Arial",Font.PLAIN,20));
                    but.setMinimumSize(new Dimension(200,80));
                    but.setMaximumSize(new Dimension(200,100));

                    /*
                     * Хранение данных о кнопке
                     * */
                    AxiliusJButton databutton = new AxiliusJButton(name,Font.PLAIN,"Arial",20);

                    /*
                     * Добавления кнопки и компонента отвечающего за отступ
                     * */
                    pane.add(but);
                    Component comp = Box.createRigidArea(new Dimension(0,20));
                    pane.add(comp);
                    panelForButton.add(pane);
                    catalogPanel.add(panelForButton);
                    catalogPanel.revalidate();
                    catalogPanel.repaint();

                    /*
                     * Сохранение данных в map и list
                     * */
                    datacomponents.put(databutton,comp);
                    databuttons.add(databutton);
                    /*
                     * создание таблицы
                     * */
                    productService.createTable(name);
                    save();
                }
            }
        };
        return actionListener;
    }
/*
* Метод отвечающий создания слушателя для категорий
* */
    public ActionListener getActionListener()
    {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.mainWindow.setSize(new Dimension(1000,800));
                Main.mainWindow.setLocationRelativeTo(null);

                CustomWindow window = new CustomWindow(e);

                Main.mainWindow.remove(Main.mainWindow.getContentPane());

                Main.mainWindow.setContentPane(window.getMainpanel());
                Main.mainWindow.revalidate();
                Main.mainWindow.repaint();
            }
        };
        return actionListener;
    }
/*
* Метод отвечающий за удаление категорий
* */
    private ActionListener deleteButton()
    {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                * Массив хранящий название кнопок и передающий их в диалоговое окно
                * */
                String[] name = new String[databuttons.size()+1];
                /*
                * Заполнение массива
                * */
                for(int i=0;i < databuttons.size();i++)
                {
                    name[i] = databuttons.get(i).getName();
                }
                name[databuttons.size()] = "";
                /*
                * Выпадающий список с названиями кнопок
                * */
                JComboBox box = new JComboBox(name);
                /*
                * Диалоговое окно запрашивающее название кнокпи подлежащей удалению
                * */
                int res = JOptionPane.showConfirmDialog(null,box,"Удаление компонента",JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if(!box.getItemAt(box.getSelectedIndex()).equals(""))
                {
                    /*
                     * получение всех компонентов в панеле pane
                     * */
                    Component[] components = pane.getComponents();
                    /*
                     * цикл удаляющий кнопку из HashMap и поиска компонента с последующим его удалением
                     * */
                    for (int i = 0;i< components.length;i++)
                    {
                        if(components[i] instanceof JButton)
                        {
                            if(((JButton) components[i]).getText().equals(box.getItemAt(box.getSelectedIndex())))
                            {
                                pane.remove(components[i]);
                                for (AxiliusJButton button: datacomponents.keySet())
                                {
                                    if(button.getName().equals(box.getItemAt(box.getSelectedIndex())))
                                    {
                                        databuttons.remove(button);
                                        pane.remove(datacomponents.get(button));
                                        datacomponents.remove(button);
                                        break;
                                    }
                                }
                                break;
                            }
                        }

                    }

                    pane.revalidate();
                    pane.repaint();

                    productService.dropTable(box.getItemAt(box.getSelectedIndex()).toString());
                    save();
                }
            }
        };
        return actionListener;
    }
/*
* Метод отвечающий за возвращение на предыдущую панель
* */
    private ActionListener getBack()
    {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.mainWindow.remove(Main.mainWindow.getContentPane());

                Main.mainWindow.setSize(400,500);
                Main.mainWindow.setLocationRelativeTo(null);
                Main.mainWindow.setResizable(false);

                Main.mainWindow.setContentPane(new WelcomeWindow().getContents());
                Main.mainWindow.revalidate();
                Main.mainWindow.repaint();
            }
        };
        return actionListener;
    }
/*
*
* Инициализация компонентов, доваление их на панель pane
*
* */
    public void initialization()
    {
        try(FileReader fileReader = new FileReader("C:\\Project on Java\\AccessToDataBase" +
                    "\\src\\main\\resources\\save\\SaveButton.json")
        ) {
            Gson json = new Gson();
            Type type = new TypeToken<ArrayList<AxiliusJButton>>(){}.getType();
            databuttons = json.fromJson(fileReader,type);

            if(databuttons == null)
            {
                databuttons = new ArrayList<>();
            }else
            {
                for(AxiliusJButton button: databuttons)
                {
                    JButton but = new JButton(button.getName());
                    but.setFont(button.getFont());
                    but.setFocusPainted(false);
                    but.addActionListener(getActionListener());
                    but.setMinimumSize(button.getMinsize());
                    but.setMaximumSize(button.getMaxsize());

                    Component comp = Box.createRigidArea(new Dimension(0,20));

                    pane.add(but);
                    pane.add(comp);

                    datacomponents.put(button,comp);

                    panelForButton.add(pane);
                    catalogPanel.add(panelForButton);
                    catalogPanel.revalidate();
                    catalogPanel.repaint();
                }
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
/*
*
* Сохранение компонентов в json формат
*
* */
    public void save()
    {
        String linefordata = new GsonBuilder().setPrettyPrinting().create().toJson(databuttons);

        try(FileWriter filesavebutton = new FileWriter("C:\\Project on Java\\AccessToDataBase" +
                    "\\src\\main\\resources\\save\\SaveButton.json")
        )
        {
            filesavebutton.write(linefordata);
            filesavebutton.flush();

        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
/*
* Возвращает главную панель класса CatalogWindow
* */
    public JPanel getCatalogPanel()
    {
        return catalogPanel;
    }
}
