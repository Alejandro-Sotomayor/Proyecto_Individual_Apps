package com.example.proyecto_individual_apps;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class DrawingBoardActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawingView mDrawView;
    private ImageButton mIvCurrPaint;

    private float mSmallBrush, mMediumBrush, mLargeBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_board);

        mSmallBrush = getResources().getInteger(R.integer.small_size);
        mMediumBrush = getResources().getInteger(R.integer.medium_size);
        mLargeBrush = getResources().getInteger(R.integer.large_size);

        mDrawView = (DrawingView) findViewById(R.id.drawing);

        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        if (paintLayout != null) {
            mIvCurrPaint = (ImageButton) paintLayout.getChildAt(0);

            if (mIvCurrPaint != null) {
                mIvCurrPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            }
        }

        //se setea click listeners para el boton de nuevo dibujo
        ImageButton btnNew = (ImageButton) findViewById(R.id.btn_new);
        if (btnNew != null) {
            btnNew.setOnClickListener(this);
        }

        //se setea click listeners para el boton de dibujado
        ImageButton btnDraw = (ImageButton) findViewById(R.id.btn_draw);
        if (btnDraw != null) {
            btnDraw.setOnClickListener(this);
        }

        //se setea click listeners para el boton de borraado
        ImageButton btnErase = (ImageButton) findViewById(R.id.btn_erase);
        if (btnErase != null) {
            btnErase.setOnClickListener(this);
        }

        //se setea click listeners para el boton de guardar
        ImageButton btnSave = (ImageButton) findViewById(R.id.btn_save);
        if (btnSave != null) {
            btnSave.setOnClickListener(this);
        }

        //se setea click listeners para el boton de llenado
        ImageButton btnFill = (ImageButton) findViewById(R.id.btn_fill);
        if (btnFill != null) {
            btnFill.setOnClickListener(this);
        }

        //se setea el tamanho inicial de la brocha
        mDrawView.setBrushSize(mMediumBrush);
    }

    //usar color actual
    public void paintClicked(View view) {

        mDrawView.setErase(false);

        //resetea el tamanho de la brocha
        mDrawView.setBrushSize(mDrawView.getLastBrushSize());

        //se actualiza el color
        if (view != mIvCurrPaint) {

            ImageButton ivColorPallet = (ImageButton) view;

            String color = view.getTag().toString();

            //se setea el nuevo color de la brocha
            mDrawView.setColor(color);

            ivColorPallet.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

            mIvCurrPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));

            //set current view as this button view
            mIvCurrPaint = (ImageButton) view;
        }

    }

    //use chosen pattern
    public void patternClicked(View view) {

        //set erase as false (if set previously as true)
        mDrawView.setErase(false);

        //reset brush size (if some other option was selected previously)
        mDrawView.setBrushSize(mDrawView.getLastBrushSize());

        //update pattern
        if (view != mIvCurrPaint) {

            //get the clicked image button
            ImageButton ivPatten = (ImageButton) view;

            //set pattern to brush
            mDrawView.setPattern(view.getTag().toString());

            //set current button's background as pressed button
            ivPatten.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

            //set previous button's background as unpressed button
            mIvCurrPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));

            //set current view as this button view
            mIvCurrPaint = (ImageButton) view;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_new:
                setearNuevoCanvas();
                break;

            case R.id.btn_draw:
                setearTamanhoBrocha();
                break;

            case R.id.btn_erase:
                cambiarModoBorrador();
                break;

            case R.id.btn_save:
                guardarDibujo();
                break;

            case R.id.btn_fill:
                mDrawView.fillColor();
                break;

            default:
                break;

        }
    }

    private void setearNuevoCanvas() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("Nuevo dibujo");
        newDialog.setMessage("Empezar un nuevo dibujo (perderá lo que se pinto en esta pantalla)?");
        newDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mDrawView.startNew();
                dialog.dismiss();
            }
        });
        newDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        newDialog.show();
    }

    private void setearTamanhoBrocha() {
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Tamaño de la brocha:");
        brushDialog.setContentView(R.layout.brush_chooser);

        ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(false);
                mDrawView.setBrushSize(mSmallBrush);
                mDrawView.setLastBrushSize(mSmallBrush);
                brushDialog.dismiss();
            }
        });

        ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(false);
                mDrawView.setBrushSize(mMediumBrush);
                mDrawView.setLastBrushSize(mMediumBrush);
                brushDialog.dismiss();
            }
        });

        ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(false);
                mDrawView.setBrushSize(mLargeBrush);
                mDrawView.setLastBrushSize(mLargeBrush);
                brushDialog.dismiss();
            }
        });

        brushDialog.show();
    }

    private void cambiarModoBorrador() {
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Tamaño de borrador:");
        brushDialog.setContentView(R.layout.brush_chooser);

        ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(true);
                mDrawView.setBrushSize(mSmallBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(true);
                mDrawView.setBrushSize(mMediumBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(true);
                mDrawView.setBrushSize(mLargeBrush);
                brushDialog.dismiss();
            }
        });

        brushDialog.show();
    }

    //guardar dibujo a la galeria
    private void guardarDibujo() {
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Guardar Dibujo");
        saveDialog.setMessage("Guardar dibujo a la Galería?");
        saveDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //save drawing
                mDrawView.setDrawingCacheEnabled(true);

                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), mDrawView.getDrawingCache(),
                        UUID.randomUUID().toString() + ".png", "dibujo");

                if (imgSaved != null) {
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Dibujo guardado a la Galería!", Toast.LENGTH_SHORT);
                    savedToast.show();
                } else {
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "La imagen no se pudo guardar", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }

                mDrawView.destroyDrawingCache();
            }
        });
        saveDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        saveDialog.show();
    }
}
