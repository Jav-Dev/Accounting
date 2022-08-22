package denisov.ru.GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import denisov.ru.Entities.Product;
import denisov.ru.Services.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;

public class CustomWindow {

    private JPanel mainpanel,productpanel,panelforallelements,panelback;
    private ArrayList<Product> products;
    private ProductService productService;
    private JTable maintable;
    private ActionEvent eventCatalogWindow;

    public CustomWindow(ActionEvent e)
    {
        this.eventCatalogWindow = e;
        /*
        * Панель для добавления всех элементов
        * */
        mainpanel = new JPanel(){
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
        mainpanel.setLayout(new BorderLayout());
        /*
        * Объект отвечающий за запросы в базу данных
        * */
        productService = new ProductService();
        /*
        * панель для кнопок добавления и удаления товара
        * */
        productpanel = new JPanel();
        productpanel.setLayout(new BoxLayout(productpanel,BoxLayout.Y_AXIS));
        productpanel.setOpaque(false);
        /*
        * панель для кнопки назад
        * */
        panelback = new JPanel();
        panelback.setOpaque(false);
        /*
        * панель для наименования столбцов и для таблицы
        * */
        panelforallelements = new JPanel();
        panelforallelements.setOpaque(false);
        panelforallelements.setLayout(new BoxLayout(panelforallelements,BoxLayout.Y_AXIS));
        /*
         * прокручивающаяся панель для таблицы
         * */
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setSize(new Dimension(panelforallelements.getWidth(),panelforallelements.getHeight()));
        /*
        * кнопка возвращения назад
        * */
        JButton backbutton = new JButton("Назад");
        backbutton.addActionListener(back());
        backbutton.setFocusPainted(false);
        backbutton.setMinimumSize(new Dimension(100,40));
        backbutton.setMaximumSize(new Dimension(120,50));
        /*
        * добавление кнопки назад в панель panelback
        * */
        panelback.add(backbutton);
        /*
        * кнопка добавления товара
        * */
        JButton addbutton = new JButton("Добавить товар");
        addbutton.addActionListener(addNewProduct(e));
        addbutton.setFocusPainted(false);
        addbutton.setMinimumSize(new Dimension(120,60));
        addbutton.setMaximumSize(new Dimension(150,80));
        /*
        * кнопка удаления товара
        * */
        JButton removebutton = new JButton("Удалить товар");
        removebutton.addActionListener(deleteProduct(e));
        removebutton.setFocusPainted(false);
        removebutton.setMinimumSize(new Dimension(120,60));
        removebutton.setMaximumSize(new Dimension(150,80));
        /*
        * добавление кнопок добавить и удалить товар в панель productpanel
        * */
        productpanel.add(addbutton);
        productpanel.add(removebutton);
        /*
        * массив с названиями столбцов
        * */
        String[] titlecolumn = new String[]{"Номер","Код","Название Товара","Количество",
                "Цена с наценкой","Цена закупки","Налог"};
        /*
        * главная таблица хранящая все данные по определенной категории
        * */
        maintable = new JTable();
        /*
        * поток достающий данные из базы данных и запихивающий их в модель с последующим присваиванием их таблице
        * */
        new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultTableModel model = getAllProduct(productService.selectAllProduct(e.getActionCommand()));
                model.setColumnIdentifiers(titlecolumn);
                maintable.setModel(model);
            }
        }).start();
        /*
        * создание еонтексного меню для удаления объектов из таблицы выделением
        * */

        maintable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouse_e) {
                if(mouse_e.getButton() == MouseEvent.BUTTON3)
                {
                    JPopupMenu menu = new JPopupMenu();
                    JButton button = new JButton("Удалить");
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent event_e) {
                            for (int i =0;i<maintable.getSelectedRows().length;i++)
                            {
                                productService.deleteProduct(e.getActionCommand(),
                                        maintable.getValueAt(maintable.getSelectedRows()[i],2).toString());

                            }
                            /*
                             * Удаление данных из сохраненного файла
                             * */
                            ArrayList<Product> deleteproducts = initialisation();
                            ArrayList<Product> productondelete = new ArrayList<>();

                            for(Product product:deleteproducts)
                            {
                                for (int i =0;i<maintable.getSelectedRows().length;i++)
                                {
                                    if(maintable.getValueAt(i,2).toString().equals(product.getTitle()))
                                    {
                                        productondelete.add(product);
                                    }
                                }
                            }


                            for(Product product: productondelete)
                            {
                                deleteproducts.remove(product);
                            }

                            try(FileWriter filesaveproducts = new FileWriter("C:\\Project on Java\\AccessToDataBase" +
                                    "\\src\\main\\resources\\save\\SaveDataForStatistic.json"))
                            {
                                String linefordata = new GsonBuilder().setPrettyPrinting().create().toJson(deleteproducts);

                                filesaveproducts.write(linefordata);
                                filesaveproducts.flush();

                            }catch(IOException exeption)
                            {
                                exeption.printStackTrace();
                            }
                            /*
                             * конец удаления
                             * */

                            DefaultTableModel model = getAllProduct(productService.selectAllProduct(e.getActionCommand()));

                            String[] titlecolumn = new String[]{"Номер","Код","Название Товара","Количество",
                                    "Цена с наценкой","Цена закупки","Налог"};

                            model.setColumnIdentifiers(titlecolumn);
                            maintable.setModel(model);

                            mainpanel.revalidate();
                            mainpanel.repaint();
                        }
                    });
                    menu.add(button);
                    menu.show(mouse_e.getComponent(),mouse_e.getX(),mouse_e.getY());
                }
            }
        });

        /*
        *
        *
        * */
        panelforallelements.add(maintable.getTableHeader());
        panelforallelements.add(scrollPane.add(maintable));

        mainpanel.add(productpanel,"North");
        mainpanel.add(panelforallelements,"Center");
        mainpanel.add(panelback,"South");

        Main.mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int res = JOptionPane.showConfirmDialog(null,
                        "Хотите ли вы сохранить изменения?","Dialog",JOptionPane.YES_NO_CANCEL_OPTION);

                if(res == 0)
                {
                    saveData();
                    updateData();
                    System.exit(0);
                }
                if(res == 1)
                {
                    System.exit(0);
                }
            }
        });
    }

    public ActionListener deleteProduct(ActionEvent event)
    {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] title = new String[products.size()];

                for(int i =0;i< products.size();i++)
                {
                    title[i] = products.get(i).getTitle();
                }

                JComboBox comboBox = new JComboBox(title);

                JOptionPane.showConfirmDialog(null,comboBox,"Удаление Товара",JOptionPane.PLAIN_MESSAGE);

                productService.deleteProduct(event.getActionCommand(),comboBox.getItemAt(comboBox.getSelectedIndex()).toString());
                products.remove(comboBox.getItemAt(comboBox.getSelectedIndex()));

                DefaultTableModel model = getAllProduct(productService.selectAllProduct(event.getActionCommand()));

                String[] titlecolumn = new String[]{"Номер","Код","Название Товара","Количество",
                        "Цена с наценкой","Цена закупки","Налог"};

                model.setColumnIdentifiers(titlecolumn);
                maintable.setModel(model);

                mainpanel.revalidate();
                mainpanel.repaint();

                ArrayList<Product> deleteproducts = initialisation();
                Product product = deleteproducts.stream().filter(new Predicate<Product>() {
                    @Override
                    public boolean test(Product product) {
                        return product.getTitle().equals(comboBox.getItemAt(comboBox.getSelectedIndex()));
                    }
                }).findFirst().get();

                deleteproducts.remove(product);

                try(FileWriter filesaveproducts = new FileWriter("C:\\Project on Java\\AccessToDataBase" +
                        "\\src\\main\\resources\\save\\SaveDataForStatistic.json"))
                {
                    String linefordata = new GsonBuilder().setPrettyPrinting().create().toJson(deleteproducts);

                    filesaveproducts.write(linefordata);
                    filesaveproducts.flush();

                }catch(IOException exeption)
                {
                    exeption.printStackTrace();
                }
            }
        };
        return actionListener;
    }

    public ActionListener addNewProduct(ActionEvent event){
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JLabel labelcode,labeltitle,labelamount,labelpricewithmarkup,labelpricepurchase,labeltax;
                labelcode = new JLabel("Введите код товара");
                labeltitle = new JLabel("Введите название товара");
                labelamount = new JLabel("Введите количество");
                labelpricewithmarkup = new JLabel("Введите цену с наценкой");
                labelpricepurchase = new JLabel("Введите цену закупки");
                labeltax = new JLabel("введите налог");

                JTextArea textcode,texttitle,textamount,textpricewithmarkup,textpricepurchase,texttax;
                textcode = new JTextArea();
                texttitle = new JTextArea();
                textamount = new JTextArea();
                textpricewithmarkup = new JTextArea();
                textpricepurchase = new JTextArea();
                texttax = new JTextArea();

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
                panel.add(labelcode);
                panel.add(textcode);
                panel.add(labeltitle);
                panel.add(texttitle);
                panel.add(labelamount);
                panel.add(textamount);
                panel.add(labelpricewithmarkup);
                panel.add(textpricewithmarkup);
                panel.add(labelpricepurchase);
                panel.add(textpricepurchase);
                panel.add(labeltax);
                panel.add(texttax);

                JOptionPane.showConfirmDialog(null,panel,
                        "Добавление товара",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

                Product product = new Product();
                product.setCode(Integer.parseInt(textcode.getText()));
                product.setTitle(texttitle.getText());
                product.setNumber(Integer.parseInt(textamount.getText()));
                product.setPricewithMarkup(Integer.parseInt(textpricewithmarkup.getText()));
                product.setPricepurchase(Integer.parseInt(textpricepurchase.getText()));
                product.setTax(Integer.parseInt(texttax.getText()));

                productService.addOneProduct(event.getActionCommand(),product);

                product = productService.foundOneProduct(event.getActionCommand(),texttitle.getText());

                DefaultTableModel model =(DefaultTableModel) maintable.getModel();
                Object[] object = new Object[]{product.getId(), product.getCode(),product.getTitle()
                        ,product.getNumber(),product.getPricewithMarkup(),product.getPricepurchase(),product.getTax()};
                model.addRow(object);

                maintable.setModel(model);

                products.add(product);

                panelforallelements.revalidate();
                panelforallelements.repaint();

            }
        };
        return actionListener;
    }

    public DefaultTableModel getAllProduct(List<Product> products)
    {
        this.products = (ArrayList<Product>) products;

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnCount(7);

        for(int i = 0;i<products.size();i++)
        {
            Object[] obj = new Object[]{
                    products.get(i).getId(),products.get(i).getCode(),products.get(i).getTitle(),
                    products.get(i).getNumber(),products.get(i).getPricewithMarkup(),products.get(i).getPricepurchase(),
                    products.get(i).getTax()
            };

            model.addRow(obj);
        }

        return model;
    }

    public ActionListener back()
    {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Main.mainWindow.remove(Main.mainWindow.getContentPane());

                CatalogWindow catalogWindow = new CatalogWindow();
                catalogWindow.initialization();

                Main.mainWindow.setResizable(true);
                Main.mainWindow.setSize(new Dimension(700,800));
                Main.mainWindow.setLocationRelativeTo(null);

                Main.mainWindow.setContentPane(catalogWindow.getCatalogPanel());

                Main.mainWindow.revalidate();
                Main.mainWindow.repaint();

                saveData();
                updateData();
            }
        };
        return actionListener;
    }

    public void updateData()
    {
        for(int i =0;i<maintable.getRowCount();i++)
        {
            if (!maintable.getValueAt(i,3).toString().equals(String.valueOf(products.get(i).getNumber())))
            {
                products.get(i).setNumber(Integer.parseInt(maintable.getValueAt(i,3).toString()));
                productService.updateOneProduct(eventCatalogWindow.getActionCommand(), products.get(i));
            }
        }
    }

    public void saveData(){
        ArrayList<Product> changeproduct = initialisation();
        ArrayList<Product> differenceproducts = new ArrayList<>();
        ArrayList<Product> productArrayList = new ArrayList<>();

        for(int i=0;i<maintable.getRowCount();i++)
        {
            if (!maintable.getValueAt(i,3).toString().equals(String.valueOf(products.get(i).getNumber())))
            {
                Product product = new Product();
                product.setCode(products.get(i).getCode());
                product.setTitle(products.get(i).getTitle());
                product.setNumber(products.get(i).getNumber()-
                       Integer.parseInt(maintable.getValueAt(i,3).toString()));
                product.setPricewithMarkup(products.get(i).getPricewithMarkup());
                product.setPricepurchase(products.get(i).getPricepurchase());
                product.setTax(products.get(i).getTax());

                differenceproducts.add(product);
            }
        }

        boolean isTrue1,isTrue2;

        if(changeproduct != null)
        {
            for (Product product:changeproduct)
            {
                isTrue1 = true;
                for(Product pr:differenceproducts)
                {
                    if(product.getTitle().equals(pr.getTitle()))
                    {
                        pr.setNumber(product.getNumber()+pr.getNumber());
                        productArrayList.add(pr);
                        isTrue1 = false;
                        break;
                    }
                }
                if(isTrue1)
                {
                    productArrayList.add(product);
                }
            }
        }
        for(Product product:differenceproducts)
        {
            isTrue2 = true;
            for (Product pr:productArrayList)
            {
                if(product.getTitle().equals(pr.getTitle()))
                {
                    isTrue2 = false;
                }
            }
            if (isTrue2)
            {
                productArrayList.add(product);
            }
        }


        try(FileWriter filesaveproducts = new FileWriter("C:\\Project on Java\\AccessToDataBase" +
                "\\src\\main\\resources\\save\\SaveDataForStatistic.json"))
        {
            String linefordata = new GsonBuilder().setPrettyPrinting().create().toJson(productArrayList);

            filesaveproducts.write(linefordata);
            filesaveproducts.flush();

        }catch(IOException e)
        {
            e.printStackTrace();
        }
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

        if(products.size() == 0)
        {
            return null;
        }
        else
        {
            return products;
        }
    }

    public JPanel getMainpanel()
    {
        return mainpanel;
    }
}
