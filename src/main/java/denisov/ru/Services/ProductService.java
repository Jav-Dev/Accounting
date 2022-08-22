package denisov.ru.Services;


import denisov.ru.Entities.Product;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;


public class ProductService {

    private SessionFactory sessionFactory;
    private Session session;

    public SessionFactory getSessionFactory()
    {
        try
        {
            sessionFactory = new Configuration()
                    .configure("hibernate/hibernate.cfg.xml")
                    .addAnnotatedClass(Product.class)
                    .buildSessionFactory();
        }catch (HibernateException e)
        {
            e.printStackTrace();
        }
        return sessionFactory;
    }

    public void createTable(String name)
    {
        session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        String sql = "CREATE TABLE IF NOT EXISTS " + name +
                " (id serial , code int NOT NULL," +
                " title varchar(40), amount int NOT NULL, " +
                " PricewithMarkup int NOT NULL, Pricepurchase int NOT NULL" +
                ", tax int NOT NULL);";

        session.createSQLQuery(sql).addEntity(Product.class).executeUpdate();

        session.getTransaction().commit();
        session.close();
    }

    public void dropTable(String name)
    {
        session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        String sql = "DROP TABLE IF EXISTS " + name + ";";

        session.createSQLQuery(sql).addEntity(Product.class).executeUpdate();

        session.getTransaction().commit();
        session.close();
    }

    public List<Product> selectAllProduct(String name)
    {
        session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        List<Product> products = session.createSQLQuery("select * from " + name + " ;").addEntity(Product.class).list();
        session.getTransaction().commit();
        session.close();
        return products;
    }

    public void addOneProduct(String name,Product product)
    {
        session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        String sql = "INSERT INTO " + name + " (code,title,amount,pricewithmarkup,pricepurchase,tax)" +
                " VALUES (" + product.getCode() +
                ",'" + product.getTitle() + "'," + product.getNumber() + "," +
                product.getPricewithMarkup() + "," + product.getPricepurchase() + "," + product.getTax() + ");";

        session.createSQLQuery(sql).addEntity(Product.class).executeUpdate();

        session.getTransaction().commit();
        session.close();
    }

    public Product foundOneProduct(String name, String title)
    {
        session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Object product = session.createSQLQuery("select * from " + name + " where title = '" + title + "' ;").addEntity(Product.class).getSingleResult();

        return (Product) product;
    }

    public void deleteProduct(String name,String title)
    {
        session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        String sql = "DELETE FROM " + name + " WHERE title = '" + title + "';";

        session.createSQLQuery(sql).addEntity(Product.class).executeUpdate();

        session.getTransaction().commit();
        session.close();
    }

    public void updateProducts(String name, ArrayList<Product> products)
    {
        session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        for(int i =0;i<products.size();i++)
        {
            String sql = "UPDATE " + name + " SET amount = " +
                    products.get(i).getNumber() + ", code = " + products.get(i).getCode() + ", " +
                    "pricewithmarkup = " + products.get(i).getPricewithMarkup() + ", " +
                    "pricepurchase = " + products.get(i).getPricepurchase() + ", tax = " +
                    products.get(i).getTax() + " WHERE title = '" + products.get(i).getTitle() + "';";

            session.createSQLQuery(sql).addEntity(Product.class).executeUpdate();
        }

        session.getTransaction().commit();
        session.close();
    }

    public void updateOneProduct(String name,Product product)
    {
        session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        String sql = "UPDATE " + name + " SET amount = " +
                product.getNumber() + ", code = " + product.getCode() + ", " +
                "pricewithmarkup = " + product.getPricewithMarkup() + ", " +
                "pricepurchase = " + product.getPricepurchase() + ", tax = " +
                product.getTax() + " WHERE title = '" + product.getTitle() + "';";

        session.createSQLQuery(sql).addEntity(Product.class).executeUpdate();

        session.getTransaction().commit();
        session.close();
    }
}
