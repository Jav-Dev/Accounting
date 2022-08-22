package denisov.ru.Entities;



import javax.persistence.*;


@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Column(name = "code")
    private int code;
    @Column(name = "title")
    private String title;
    @Column(name = "amount")
    private int number;
    @Column(name = "pricewithmarkup")
    private int PricewithMarkup;
    @Column(name = "pricepurchase")
    private int Pricepurchase;
    @Column(name = "tax")
    private int tax;

    public Product(){}


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPricewithMarkup() {
        return PricewithMarkup;
    }

    public void setPricewithMarkup(int pricewithMarkup) {
        PricewithMarkup = pricewithMarkup;
    }

    public int getPricepurchase() {
        return Pricepurchase;
    }

    public void setPricepurchase(int pricepurchase) {
        Pricepurchase = pricepurchase;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }
}
