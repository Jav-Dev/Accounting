package denisov.ru.GUI;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import denisov.ru.Entities.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class StatisticWindow {

    private JPanel mainpanel,panelfortable;
    private JTable maintable;
    private DefaultTableModel mainmodel;

    public StatisticWindow()
    {
        mainpanel = new JPanel(new BorderLayout()){
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
        panelfortable = new JPanel();
        panelfortable.setOpaque(false);
        panelfortable.setLayout(new BoxLayout(panelfortable,BoxLayout.Y_AXIS));

        mainmodel = operations();

        JPanel panelforbackbutton = new JPanel();
        panelforbackbutton.setOpaque(false);
        JPanel panelforlabel = new JPanel();
        panelforlabel.setOpaque(false);

        JLabel label = new JLabel("Статистика");
        label.setFont(new Font("Arial",Font.PLAIN,30));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setPreferredSize(new Dimension(200,60));

        panelforlabel.add(label);

        JButton buttonback = new JButton("Назад");
        buttonback.addActionListener(back());
        buttonback.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonback.setFocusPainted(false);
        buttonback.setPreferredSize(new Dimension(80,20));

        panelforbackbutton.add(buttonback);

        maintable = new JTable();

        maintable.setModel(mainmodel);

        panelfortable.add(maintable.getTableHeader());
        panelfortable.add(maintable);

        mainpanel.add(panelforlabel,"North");
        mainpanel.add(panelfortable);
        mainpanel.add(panelforbackbutton,"South");

    }

    public DefaultTableModel operations()
    {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnCount(8);

        String[] names = new String[]{
                "Номер","Код","Название Товара","Количество",
                "Цена с наценкой","Цена закупки","Налог","Прибыль"
        };

        model.setColumnIdentifiers(names);

        for (int i =0;i< initialisation().size();i++)
        {
            Object[] obj = new Object[]{
                    initialisation().get(i).getId(),initialisation().get(i).getCode(),initialisation().get(i).getTitle(),
                    initialisation().get(i).getNumber(),initialisation().get(i).getPricewithMarkup(),
                    initialisation().get(i).getPricepurchase(),
                    initialisation().get(i).getTax(),
                    initialisation().get(i).getNumber() * initialisation().get(i).getPricewithMarkup()
            };

            model.addRow(obj);
        }
        return model;
    }

    private ArrayList<Product> initialisation()
    {
        ArrayList<Product> products = new ArrayList<>();
        try(FileReader fileReader = new FileReader("C:\\Project on Java\\AccessToDataBase" +
                "\\src\\main\\resources\\save\\SaveDataForStatistic.json")
        ) {
            Gson json = new Gson();
            Type type = new TypeToken<ArrayList<Product>>() {
            }.getType();

            products = json.fromJson(fileReader, type);

        }catch (IOException e)
        {
            e.printStackTrace();
        }

        if(products == null)
        {
            return null;
        }
        else
        {
            return products;
        }
    }

    private ActionListener back()
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

    public JPanel getMainpanel()
    {
        return mainpanel;
    }
}
