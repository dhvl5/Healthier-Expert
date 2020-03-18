package com.example.healthierexpert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.transition.Fade;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import java.util.Date;

public class MedicineReminder extends AppCompatActivity {

    static long backPressed;

    EditText titleEditText;

    CheckBox morning, noon, evening;
    int first, second, third, total;

    FloatingActionButton floatingActionButton;

    RelativeLayout bottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    String notification_title="My Notification";
    String notification_msg="Hello World";
    Button createNoteBtn;

    RecyclerView recyclerView;
    MedicineAdapter medicineAdapter;
    ArrayList<Medicine> noteList;

    View dimBackgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_reminder);

        sendNotification();
        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container),true);
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground,true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;

        dimBackgroundView = findViewById(R.id.dimBackground);
        recyclerView = findViewById(R.id.rv);
        bottomSheet = findViewById(R.id.bottom_sheet);
        createNoteBtn = findViewById(R.id.createNoteBtn);
        titleEditText = findViewById(R.id.titleNoteEditText);
        floatingActionButton = findViewById(R.id.floating_btn);
        morning = findViewById(R.id.morning_value);
        noon = findViewById(R.id.noon_value);
        evening = findViewById(R.id.evening_value);

        dimBackgroundView.setVisibility(View.GONE);

        noteList = new ArrayList<>();
        medicineAdapter = new MedicineAdapter(this, noteList);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(medicineAdapter);

        floatingActionButton.setImageResource(R.drawable.ic_add_black);

        if(inputMethodManager.hideSoftInputFromWindow(titleEditText.getWindowToken(), 0))
            titleEditText.clearFocus();

        ClearFocus(titleEditText, inputMethodManager);

        //region floatingActionButton click listener
        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(medicineAdapter.getSelected().size() > 0)
                {
                    for(Medicine n : new ArrayList<>(medicineAdapter.getSelected()))
                    {
                        noteList.remove(n);
                        medicineAdapter.notifyItemRemoved(noteList.size());
                        medicineAdapter.notifyDataSetChanged();
                    }
                    floatingActionButton.setRippleColor(ColorStateList.valueOf(Color.BLACK));
                    floatingActionButton.setBackgroundTintList(getColorStateList(R.color.colorAccent));
                    floatingActionButton.setImageResource(R.drawable.ic_add_black);
                    floatingActionButton.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
                    return;
                }

                HideFloatingButton();
                dimBackgroundView.setVisibility(View.VISIBLE);
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dimBackgroundView, "Alpha", .7f);
                objectAnimator.setDuration(1000);
                objectAnimator.start();
                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
            }
        });
        //endregion

        //region createNoteBtn click listener
        createNoteBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(titleEditText.getText().toString().matches(""))
                {
                    ShowToast("Empty note discarded!");
                }
                else
                {
                    Medicine a = new Medicine(titleEditText.getText().toString(), ReturnCheckedBoxes());
                    noteList.add(a);
                    medicineAdapter.notifyDataSetChanged();

                    inputMethodManager.hideSoftInputFromWindow(titleEditText.getWindowToken(), 0);
                    titleEditText.clearFocus();
                    titleEditText.getText().clear();
                }
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                RevealFloatingButton();
            }
        });
        //endregion

        //region noteAdapter single click listener
        medicineAdapter.setOnItemClickListener(new MedicineAdapter.OnItemClickListener()
        {
            @Override
            public void OnItemClick(int position, View v, MedicineAdapter.MyViewHolder holder)
            {
                if(medicineAdapter.getSelected().size() > 0)
                {
                    noteList.get(position).setChecked(!noteList.get(position).isChecked());
                    if(noteList.get(position).isChecked())
                    {
                        holder.noteCard.setCardBackgroundColor(Color.GRAY);
                    }
                    else
                    {
                        holder.noteCard.setCardBackgroundColor(Color.WHITE);
                    }
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), EditMedicineDetails.class);
                intent.putExtra("edit_title", noteList.get(position).getNoteTitle());
                Pair<View, String> pair1 = new Pair<>(v, ViewCompat.getTransitionName(v));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MedicineReminder.this, pair1);
                startActivity(intent, options.toBundle());
            }
        });
        //endregion

        //region noteAdapter long click listener
        medicineAdapter.setOnItemLongClickListener(new MedicineAdapter.OnItemLongClickListener()
        {
            @Override
            public void OnLongClick(int position, MedicineAdapter.MyViewHolder holder)
            {
                floatingActionButton.setRippleColor(ColorStateList.valueOf(Color.TRANSPARENT));
                floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                floatingActionButton.setImageResource(R.drawable.ic_delete_white);
                floatingActionButton.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
                noteList.get(position).setChecked(!noteList.get(position).isChecked());
                if(noteList.get(position).isChecked())
                {
                    holder.noteCard.setCardBackgroundColor(Color.GRAY);
                }
                else
                {
                    holder.noteCard.setCardBackgroundColor(Color.WHITE);
                }
            }
        });
        //endregion

        //region dimBackgroundView click listener
        dimBackgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        //endregion

        //region bottomSheetBehavior callback
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
                if(newState == BottomSheetBehavior.STATE_HIDDEN)
                {
                    if(floatingActionButton.getVisibility() != View.VISIBLE)
                        RevealFloatingButton();
                    dimBackgroundView.setVisibility(View.GONE);
                    dimBackgroundView.setAlpha(0);

                    if(inputMethodManager.hideSoftInputFromWindow(titleEditText.getWindowToken(), 0))
                        titleEditText.clearFocus();
                }
                else if( newState == BottomSheetBehavior.STATE_DRAGGING)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {

            }
        });
        //endregion
    }

    private void RevealFloatingButton()
    {
        int centerX = floatingActionButton.getWidth() / 2;
        int centerY = floatingActionButton.getHeight() / 2;

        float radius = (float) Math.hypot(centerX, centerY);

        Animator animator = ViewAnimationUtils.createCircularReveal(floatingActionButton, centerX, centerY, 0, radius);
        animator.setDuration(500);
        floatingActionButton.setVisibility(View.VISIBLE);
        animator.start();
    }

    private void HideFloatingButton()
    {
        int centerX = floatingActionButton.getWidth() / 2;
        int centerY = floatingActionButton.getHeight() / 2;

        float radius = (float) Math.hypot(centerX, centerY);

        Animator animator = ViewAnimationUtils.createCircularReveal(floatingActionButton, centerX, centerY, radius, 0);
        animator.setDuration(500);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                floatingActionButton.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
    }

    private void ClearFocus(final EditText editText, final InputMethodManager inputMethodManager)
    {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    editText.clearFocus();
                }
            }
        });
    }

    private void ShowToast(String msg)
    {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        toast.show();
    }

    @Override
    public void onBackPressed()
    {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN)
        {
            if(backPressed + 2000 > System.currentTimeMillis())
                super.onBackPressed();
            else
            {
                ShowToast("Press again to exit!!!");
                backPressed = System.currentTimeMillis();
            }
        }
    }

    public void sendNotification()
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(notification_title)
                        .setContentText(notification_msg);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }

    public String ReturnCheckedBoxes()
    {
        if(morning.isChecked())
        {
            first = 1;
            total = first;
        }
        if(noon.isChecked())
        {
            second = 2;
            total = second;
        }
        if(evening.isChecked())
        {
            third = 3;
            total = third;
        }
        if(morning.isChecked() && noon.isChecked())
            total = 4;
        if(noon.isChecked() && evening.isChecked())
            total = 5;
        if(morning.isChecked() && evening.isChecked())
            total = 6;
        if(morning.isChecked() && noon.isChecked() && evening.isChecked())
            total = 7;

        String value;

        switch (total)
        {
            case 1:
                value = "Morning";
                break;
            case 2:
                value = "Noon";
                break;
            case 3:
                value = "Evening";
                break;
            case 4:
                value = "Morning, Noon";
                break;
            case 5:
                value = "Noon, Evening";
                break;
            case 6:
                value = "Morning, Evening";
                break;
            case 7:
                value = "Morning, Noon & Evening";
                break;
            default:
                value = "";
                break;
        }

        return value;
    }
}
