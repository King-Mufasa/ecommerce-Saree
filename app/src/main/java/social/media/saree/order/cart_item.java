package social.media.saree.order;

public class cart_item {
    public String product_id;
    public String product_name;
    public String product_amount;

    public cart_item(){

    }
    public cart_item(String product_id, String product_name, String product_amount){
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_amount = product_amount;
    }

    public String getProduct_amount() {
        return product_amount;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }
}