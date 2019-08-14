package edu.uta.leanmed.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import edu.uta.leanmed.pojo.CartItem;
import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.services.SharedPreferenceService;

public class InventoryDetailActivity extends AppCompatActivity {
    private TextView mTradeName,mGenName,mMedType,mDosage,mInventory,mExpiry,mBoxId,mZoneId,mZoneName,mZoneEmail,mZoneCountry;
    private Spinner quantity;
    private ImageButton cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);
        final Inventory inventory=(Inventory) getIntent().getSerializableExtra("medicineInventory");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(inventory.getMedicine().getTradeName());
        mTradeName=findViewById(R.id.trade_name);
        mGenName=findViewById(R.id.gen_name);
        mMedType=findViewById(R.id.medicineType);
        mDosage=findViewById(R.id.dosage);
        mInventory=findViewById(R.id.inventory);
        mExpiry=findViewById(R.id.expiryDate);
        mBoxId=findViewById(R.id.idBox);
        mZoneId=findViewById(R.id.zoneId);
        mZoneName=findViewById(R.id.zoneName);
        mZoneEmail=findViewById(R.id.zoneEmail);
        quantity=findViewById(R.id.spinner);
        cart=findViewById(R.id.cart);
        mTradeName.setText(getString(R.string.trade_name)+inventory.getMedicine().getTradeName());
        mGenName.setText(getString(R.string.generic_name)+inventory.getMedicine().getGenName());
        mMedType.setText(getString(R.string.medicine_type)+inventory.getMedicine().getMedicineType());
        mDosage.setText(getString(R.string.dosage)+inventory.getMedicine().getDosage());
        mInventory.setText(getString(R.string.inventory)+inventory.getUnits());
        mExpiry.setText(getString(R.string.expiry)+inventory.getExpiryDate());
        mBoxId.setText(getString(R.string.box_id)+inventory.getIdBox());
        mZoneId.setText(getString(R.string.zone_id)+inventory.getZone().getZoneId());
        mZoneName.setText(getString(R.string.zone_name)+inventory.getZone().getZoneName());
        mZoneEmail.setText(getString(R.string.zone_email)+inventory.getZone().getZoneEmail());

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int val=Integer.parseInt(quantity.getSelectedItem().toString());
                if(val>inventory.getUnits()){
                    Snackbar.make(findViewById(android.R.id.content),getString(R.string.cart_error),Snackbar.LENGTH_LONG).show();
                    return;
                }
                CartItem cartItem=new CartItem(inventory,val,inventory.getZone());
                SharedPreferenceService.saveCartToSharedPreference(getBaseContext(),cartItem);
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
