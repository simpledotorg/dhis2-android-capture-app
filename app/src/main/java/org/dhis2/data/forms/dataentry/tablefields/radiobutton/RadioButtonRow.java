package org.dhis2.data.forms.dataentry.tablefields.radiobutton;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.dhis2.R;
import org.dhis2.data.forms.dataentry.tablefields.Row;
import org.dhis2.data.forms.dataentry.tablefields.RowAction;
import org.dhis2.databinding.FormYesNoBinding;

import io.reactivex.processors.FlowableProcessor;

/**
 * QUADRAM. Created by frodriguez on 1/24/2018.
 */

public class RadioButtonRow implements Row<RadioButtonCellHolder, RadioButtonViewModel> {

    private final LayoutInflater inflater;

    @NonNull
    private final FlowableProcessor<RowAction> processor;
    private boolean accessDataWrite;

    public RadioButtonRow(LayoutInflater layoutInflater, FlowableProcessor<RowAction> processor, boolean accessDataWrite) {
        this.inflater = layoutInflater;
        this.processor = processor;
        this.accessDataWrite = accessDataWrite;
    }

    @NonNull
    @Override
    public RadioButtonCellHolder onCreate(@NonNull ViewGroup parent) {
        FormYesNoBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.form_yes_no, parent, false);
        binding.customYesNo.setCellLayout();
        return new RadioButtonCellHolder(binding, processor);
    }

    @Override
    public void onBind(@NonNull RadioButtonCellHolder viewHolder, @NonNull RadioButtonViewModel viewModel, String value) {
        viewHolder.update(viewModel, accessDataWrite);
    }

    @Override
    public void deAttach(@NonNull RadioButtonCellHolder viewHolder) {
        viewHolder.dispose();
    }


}