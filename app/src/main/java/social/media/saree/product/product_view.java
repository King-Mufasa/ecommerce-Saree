package social.media.saree.product;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import social.media.saree.R;
import social.media.saree.cart.cart_item;
import social.media.saree.order.make_Order;
import social.media.saree.saree.saree;
import social.media.saree.util.Global;

import static social.media.saree.util.Global.user_carts;

public class product_view extends AppCompatActivity implements View.OnClickListener {


    Integer amount = 0;
    TextView product_amount;
    saree selected_saree = Global.selected_saree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        TextView saree_name = (TextView)findViewById(R.id.detail_saree_name);
        TextView saree_label = (TextView)findViewById(R.id.detail_saree_label);
        RatingBar saree_rating = (RatingBar)findViewById(R.id.detail_saree_rating);
        TextView saree_price = (TextView)findViewById(R.id.detail_saree_price);
        TextView saree_length = (TextView)findViewById(R.id.detail_saree_length);
        TextView saree_material = (TextView)findViewById(R.id.detail_saree_material);
        TextView saree_description = (TextView)findViewById(R.id.detail_saree_description);
        ImageView saree_photo = (ImageView)findViewById(R.id.detail_saree_photo);
        Button btn_minus = (Button)findViewById(R.id.btn_decrease_amount);
        Button btn_plus = (Button)findViewById(R.id.btn_increase_amount);
        product_amount = (TextView)findViewById(R.id.product_amount);
        Button add_to_cart = (Button)findViewById(R.id.btn_add_to_cart);
        final Button make_order = (Button)findViewById(R.id.btn_make_order);
        btn_minus.setOnClickListener(this);
        btn_plus.setOnClickListener(this);
        add_to_cart.setOnClickListener(this);
        make_order.setOnClickListener(this);


        Intent intent = getIntent();


        saree_name.setText(selected_saree.getSaree_Name());
        saree_price.setText(selected_saree.getSaree_Price());
        saree_label.setText(selected_saree.getSaree_Label());
        saree_length.setText(selected_saree.getSaree_Length());
        saree_rating.setProgress(Integer.parseInt(selected_saree.getSaree_rating()));
        String base64photo = selected_saree.getSaree_photo_a();
        String imageDataBytes = base64photo.substring(base64photo.indexOf(",") + 1);
        InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        saree_photo.setImageBitmap(bitmap);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_decrease_amount:
                amount --;
                if (amount<0)
                    amount = 0;
                product_amount.setText(String.valueOf(amount));
                break;
            case R.id.btn_increase_amount:
                amount ++;
                product_amount.setText(String.valueOf(amount));
                break;
            case R.id.btn_add_to_cart:
                String cart_price = String.valueOf(Integer.parseInt(selected_saree.getSaree_Price()) * amount);
                cart_item new_cart = new cart_item(selected_saree.getSaree_Id(), selected_saree.saree_Name, String.valueOf(amount), cart_price);
                Integer new_amount = 0;
                cart_item replaced_cart;
                Integer new_price = 0;
                boolean update = false;
                for(int i = 0; i < user_carts.size(); i ++){
                    if(user_carts.get(i).getProduct_id() == selected_saree.getSaree_Id()) {
                        new_amount = Integer.parseInt(user_carts.get(i).getProduct_amount()) + amount;
                        new_price = (Integer.parseInt(selected_saree.getSaree_Price()) * new_amount);
                        replaced_cart = new cart_item(user_carts.get(i).getProduct_id(), user_carts.get(i).getProduct_name(), String.valueOf(new_amount),String.valueOf(new_price) );
                        user_carts.set(i, replaced_cart);
                        update = true;
                    }
                }
                if (!update)
                    user_carts.add(new_cart);
                Toast.makeText(product_view.this, "Added to cart", Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.btn_make_order:
                if(Global.current_user_name.equals(""))
                    Toast.makeText(product_view.this,"To make order, you have to make your account.", Toast.LENGTH_LONG).show();
                else {
                    Intent intent = new Intent(product_view.this, make_Order.class);
                    startActivity(intent);
                }
                break;
        }
    }
}
