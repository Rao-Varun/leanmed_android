package edu.uta.leanmed.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.uta.leanmed.pojo.Inventory;

public class MedicineInfoDialogue extends Dialog implements
        android.view.View.OnClickListener {
    private Activity activity;
    private Inventory inventory;
    private TextView textGenName, textTradeName, textMedType, textDosage, textWeight;
    private TextView textInventory, textExpiry;
    private TextView textZoneId, textZoneName, textZoneEmail;

    public MedicineInfoDialogue(@NonNull Context context, Inventory inventory) {
        super(context);
        this.inventory = inventory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.inventory_info_dialogue_layout);
        this.setInventoryInfoToLayout();
        this.setOkButton();
    }

    private void setOkButton() {
        Button button = findViewById(R.id.buttonOk);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setInventoryInfoToLayout() {
        this.setMedicineDetails();
        this.setInventoryDetails();
        this.setZoneDetails();
    }

    private void setZoneDetails() {
        this.defineZoneDetails();
        this.setZoneStringsToTextView();
    }

    private void setZoneStringsToTextView() {
        textZoneId.setText(String.format("%s  %s",getContext().getResources().getString(R.string.zone_id),inventory.getZone().getZoneId()));
        textZoneName.setText(String.format("%s  %s",getContext().getResources().getString(R.string.zone_name),inventory.getZone().getZoneName()));
        textZoneEmail.setText(String.format("%s  %s",getContext().getResources().getString(R.string.zone_email),inventory.getZone().getZoneEmail()));

    }

    private void defineZoneDetails() {
        textZoneId= findViewById(R.id.zoneId);
        textZoneName = findViewById(R.id.zoneName);
        textZoneEmail = findViewById(R.id.zoneEmail);
    }

    private void setInventoryDetails() {
        this.defineInventoryTextViews();
        this.setInventoryStringsToTextViews();
    }

    private void setInventoryStringsToTextViews() {
        textInventory.setText(String.format("%s  %s",getContext().getResources().getString(R.string.inventory),inventory.getUnits()));
        textExpiry.setText(String.format("%s  %s",getContext().getResources().getString(R.string.expiry),inventory.getExpiryDate()));

    }

    private void defineInventoryTextViews() {
        this.textInventory = findViewById(R.id.inventory);
        this.textExpiry = findViewById(R.id.expiryDate);
    }

    private void setMedicineDetails() {
        this.defineMedicineTextViews();
        this.setMedicineStringsToTextViews();
    }

    private void setMedicineStringsToTextViews() {
        textGenName.setText(String.format("%s  %s",getContext().getResources().getString(R.string.generic_name),inventory.getMedicine().getGenName()));
        textTradeName.setText(String.format("%s  %s",getContext().getResources().getString(R.string.trade_name),inventory.getMedicine().getTradeName()));
        textMedType.setText(String.format("%s  %s",getContext().getResources().getString(R.string.med_input),inventory.getMedicine().getMedicineType()));
        textDosage.setText(String.format("%s  %s",getContext().getResources().getString(R.string.dosage),inventory.getMedicine().getDosage()));
        textWeight.setText(String.format("%s  %s",getContext().getResources().getString(R.string.weight),inventory.getMedicine().getWeight()));
    }

    private void defineMedicineTextViews() {
        this.textGenName = findViewById(R.id.gen_name);
        this.textTradeName = findViewById(R.id.trade_name);
        this.textMedType = findViewById(R.id.medicineType);
        this.textDosage = findViewById(R.id.dosage);
        this.textWeight = findViewById(R.id.weight);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
