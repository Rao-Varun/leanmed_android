package edu.uta.leanmed.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class BoxContentMenuDialogue extends Dialog implements
        android.view.View.OnClickListener {
    private String medicineName;
    private int position;
    private Context context;
    private TextView textMedicineName;
    private CardView cardEdit;
    private CardView cardDelete;
    private DialogListener dialogListener;


    public interface DialogListener{
        public void onSetDeleteBoxContent(int position);
        public void onSetEditBoxContent(int position);
    }

    public BoxContentMenuDialogue(@NonNull Context context ,int position, String medicineName) {
        super(context);
        this.position = position;
        this.medicineName = medicineName;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.box_content_menu_dialogue);
        this.setTextViewElement();
        this.setAllCardViewsElement();
    }

    private void setAllCardViewsElement() {
        cardEdit = findViewById(R.id.cardEdit);
        cardDelete = findViewById(R.id.cardDelete);
        cardEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogListener.onSetEditBoxContent(position);
                dismiss();
            }
        });

        cardDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogListener.onSetDeleteBoxContent(position);
                dismiss();
            }
        });
    }

    private void setTextViewElement() {
        textMedicineName = findViewById(R.id.textMedicineName);
        textMedicineName.setText(medicineName);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

}
