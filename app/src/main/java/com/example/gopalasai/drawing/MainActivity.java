package com.example.gopalasai.drawing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private DrawingView drawView;
    private ImageButton currentPaint,drawBtn,eraseBtn,saveBtn,newBtn;
    private float smallBrush, mediumBrush, largeBrush; //paintbrush sizes

    public static final String TAG = "paint";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = findViewById(R.id.drawing);

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger((R.integer.medium_size));
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawView.setBrushSize(smallBrush);

        LinearLayout paintLayout = findViewById(R.id.paint_colors);

        currentPaint = (ImageButton) paintLayout.getChildAt(0);
        currentPaint.setImageDrawable(getResources().getDrawable(R.drawable.paintpressed));

        drawBtn = findViewById(R.id.drawBtn);
        drawBtn.setOnClickListener(this);
        eraseBtn = findViewById(R.id.eraseBtn);
        eraseBtn.setOnClickListener(this);
        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);
        newBtn = findViewById(R.id.newBtn);
        newBtn.setOnClickListener(this);


    }//end oncreate





    public void paintClicked(View v){
        //lets us change the paint color
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
        if (v != currentPaint){
            //update the color
            ImageButton imgView = (ImageButton) v;
            String color = v.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paintpressed));
            currentPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currentPaint = (ImageButton)v;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.drawBtn){
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush Size");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });//end small brush listener
            ImageButton mediumBtn = brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });//end medium brush listener
            ImageButton largeBtn = brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });//end large brush listener

            brushDialog.show();

        }//end draw if
        else if(v.getId() == R.id.eraseBtn){
            final Dialog eraseDialog = new Dialog(this);
            eraseDialog.setTitle("Eraser Size");
            eraseDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = eraseDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    eraseDialog.dismiss();
                }
            });//end smallerase
            ImageButton mediumBtn = eraseDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    eraseDialog.dismiss();
                }
            });//end medium erase
            ImageButton largeBtn = eraseDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    eraseDialog.dismiss();
                }
            });//end largeerase
            eraseDialog.show();
        }//end eraseBtn
        else if (v.getId() == R.id.newBtn){
            final AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New Drawing");
            newDialog.setMessage("Start a new drawing(you will lose the current drawing!)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();
        }//end new button
        else if(v.getId() == R.id.saveBtn){
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save Drawing");
            saveDialog.setMessage("Save drawing to device Gallery");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    drawView.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(getContentResolver(),
                            drawView.getDrawingCache(), UUID.randomUUID().toString() + ".png",
                            "drawing");
                    if (imgSaved != null){
                        Toast.makeText(getApplicationContext(),"Drawing Saved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Whoops,drawing not saved!", Toast.LENGTH_SHORT).show();

                    }
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }//end save button




    }
}
