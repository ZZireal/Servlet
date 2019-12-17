public class Product extends Entity {
    int id;
    private String title;
    private int genre_id;
    private String descrip;
    private String debut;
    private int developer_id;
    private int publisher_id;
    private String cost;
    public Product() {
    }
    public Product(int id, String title, int genre_id, String descrip,
                   String debut,
                   int developer_id, int publisher_id, String cost) {
        super(id);
        this.title = title;
        this.genre_id = genre_id;
        this.descrip = descrip;
        this.debut = debut;
        this.developer_id = developer_id;
        this.publisher_id = publisher_id;
        this.cost = cost;
    }
    public String getTitle(String title) {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getGenre_id(String genre_id) {
        return this.genre_id;
    }
    public void setGenre_id(int genre_id) {
        this.genre_id = genre_id;
    }
    public String getDescrip(String descrip) {
        return this.descrip;
    }
    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }
    public String getDebut() {
        return debut;
    }
    public void setDebut(String debut) {
        this.debut = debut;
    }
    public int getDeveloper_id(String developer_id) {
        return this.developer_id;
    }
    public void setDeveloper_id(int developer_id) {
        this.developer_id = developer_id;
    }
    public int getPublisher_id(String publisher_id) {
        return this.publisher_id;
    }
    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }
    public String getCost() {
        return cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }
    @Override
    public String toString() {
        return "Продукт [id = " + id +
                ", название = " + title +
                ", жанр = " + genre_id +
                ", описание = " + descrip +
                ", дата выхода = " + debut +
                ", разработчик = " + developer_id +
                ", издатель = " + publisher_id +
                ", цена = " + cost + "]" + "\n";
    }
}