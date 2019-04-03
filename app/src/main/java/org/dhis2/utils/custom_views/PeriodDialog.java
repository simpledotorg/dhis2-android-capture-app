package org.dhis2.utils.custom_views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.dhis2.R;
import org.dhis2.databinding.DialogPeriodBinding;
import org.dhis2.utils.DateUtils;
import org.hisp.dhis.android.core.period.PeriodType;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

/**
 * QUADRAM. Created by ppajuelo on 21/05/2018.
 */

public class PeriodDialog extends DialogFragment {
    DialogPeriodBinding binding;
    private OnDateSet possitiveListener;
    private View.OnClickListener negativeListener;
    private String title;

    private Context context;
    private Date currentDate;
    private PeriodType period;
    private Date minDate;
    private Date maxDate;


    public PeriodDialog() {
        possitiveListener = null;
        negativeListener = null;
        title = null;
        currentDate = DateUtils.getInstance().getNextPeriod(period, Calendar.getInstance().getTime(), -1);
    }

    public PeriodDialog setPeriod(PeriodType period) {
        this.period = period;
        return this;
    }

    public PeriodDialog setPossitiveListener(OnDateSet listener) {
        this.possitiveListener = listener;
        return this;
    }

    public PeriodDialog setNegativeListener(View.OnClickListener listener) {
        this.negativeListener = listener;
        return this;
    }


    public PeriodDialog setTitle(String title) {
        this.title = title;
        return this;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_period, container, false);

        binding.title.setText(title);
        binding.acceptButton.setOnClickListener(view -> {

            possitiveListener.onDateSet(currentDate);
            dismiss();
        });
        binding.clearButton.setOnClickListener(view -> dismiss());

        binding.periodSubtitle.setText(period.name());
        if (minDate == null || currentDate.after(minDate))
            currentDate = DateUtils.getInstance().getNextPeriod(period, currentDate, 0);
        else if (minDate != null && currentDate.before(minDate))
            currentDate = DateUtils.getInstance().getNextPeriod(period, minDate, 0);
        else
            currentDate = DateUtils.getInstance().getNextPeriod(period, currentDate, 0);

        binding.selectedPeriod.setText(DateUtils.getInstance().getPeriodUIString(period,
                currentDate, Locale.getDefault()));

        binding.periodBefore.setOnClickListener(view -> {
            previousPeriod();
            checkConstraintDates();

        });
        binding.periodNext.setOnClickListener(view -> {
            nextPeriod();
            checkConstraintDates();

        });
        checkConstraintDates();
        return binding.getRoot();
    }

    public void nextPeriod() {
        currentDate = DateUtils.getInstance().getNextPeriod(period, currentDate, 1);
        binding.selectedPeriod.setText(DateUtils.getInstance().getPeriodUIString(period, currentDate, Locale.getDefault()));
    }

    public void previousPeriod() {
        currentDate = DateUtils.getInstance().getNextPeriod(period, currentDate, -1);
        binding.selectedPeriod.setText(DateUtils.getInstance().getPeriodUIString(period, currentDate, Locale.getDefault()));
    }

    protected void checkConstraintDates() {

        if (minDate != null && minDate.equals(currentDate))
            binding.periodBefore.setEnabled(false);
        else
            binding.periodBefore.setEnabled(true);

        if (maxDate != null && maxDate.equals(currentDate))
            binding.periodNext.setEnabled(false);
        else
            binding.periodNext.setEnabled(true);

        binding.periodNext.setVisibility(maxDate == null || currentDate.before(maxDate) ? View.VISIBLE : View.GONE);
        binding.periodBefore.setVisibility(minDate == null || currentDate.after(minDate) ? View.VISIBLE : View.GONE);

    }

    public PeriodDialog setMinDate(Date minDate) {
        this.minDate = minDate;
        return this;
    }

    public PeriodDialog setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    public interface OnDateSet {
        void onDateSet(Date selectedDate);
    }

    public String getTitle() {
        return title;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public PeriodType getPeriod() {
        return period;
    }

    public Date getMinDate() {
        return minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public DialogPeriodBinding getBinding() {
        return binding;
    }

    public OnDateSet getPossitiveListener() {
        return possitiveListener;
    }

    public View.OnClickListener getNegativeListener() {
        return negativeListener;
    }

    protected void setCurrentDate(Date currentDate) {
        this.currentDate = DateUtils.getInstance().getNextPeriod(period, currentDate, -1);
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }
}