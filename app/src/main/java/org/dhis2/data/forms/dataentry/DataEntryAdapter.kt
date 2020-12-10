package org.dhis2.data.forms.dataentry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import org.dhis2.data.forms.dataentry.fields.FieldUiModel
import org.dhis2.data.forms.dataentry.fields.FieldViewModel
import org.dhis2.data.forms.dataentry.fields.FormViewHolder
import org.dhis2.data.forms.dataentry.fields.FormViewHolder.FieldItemCallback
import org.dhis2.data.forms.dataentry.fields.section.SectionViewModel
import java.util.ArrayList
import java.util.LinkedHashMap

class DataEntryAdapter :
    ListAdapter<FieldUiModel, FormViewHolder>(DataEntryDiff()),
    FieldItemCallback {

    var didItemShowDialog: ((title: String, message: String?) -> Unit)? = null
    private var currentFocusPosition: Int? = null

    private val sectionHandler = SectionHandler()
    var sectionPositions: MutableMap<String, Int> = LinkedHashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return FormViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        if (getItem(position) is SectionViewModel) {
            updateSectionData(position, false)
        }
        holder.bind(getItem(position), position, this, isItemFocused(position))
    }

    private fun isItemFocused(position: Int): Boolean {
        return position == currentFocusPosition
    }

    fun updateSectionData(position: Int, isHeader: Boolean) {
        (getItem(position) as SectionViewModel?)!!.setShowBottomShadow(
            !isHeader && position > 0 && getItem(
                position - 1
            ) !is SectionViewModel
        )
        (getItem(position) as SectionViewModel?)!!.sectionNumber = getSectionNumber(position)
        (getItem(position) as SectionViewModel?)!!.setLastSectionHeight(
            position > 0 && position == itemCount - 1 && getItem(
                position - 1
            ) !is SectionViewModel
        )
    }

    private fun getSectionNumber(sectionPosition: Int): Int {
        var sectionNumber = 1
        for (i in 0 until sectionPosition) {
            if (getItem(i) is SectionViewModel) {
                sectionNumber++
            }
        }
        return sectionNumber
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)!!.getLayoutId()
    }

    fun swap(updates: List<FieldViewModel>, commitCallback: Runnable) {
        sectionPositions = LinkedHashMap()
        val items: MutableList<FieldUiModel> = ArrayList()
        for (fieldViewModel in updates) {
            if (fieldViewModel is SectionViewModel) {
                sectionPositions[fieldViewModel.getUid()] = updates.indexOf(fieldViewModel)
            }
            items.add(fieldViewModel)
        }
        submitList(items) {
            commitCallback.run()
        }
    }

    fun getSectionSize(): Int {
        return sectionPositions.size
    }

    fun getSectionForPosition(visiblePos: Int): SectionViewModel? {
        val sectionPosition = sectionHandler.getSectionPositionFromVisiblePosition(
            visiblePos,
            isSection(visiblePos),
            ArrayList(sectionPositions.values)
        )
        return if (sectionPosition != -1) {
            getItem(sectionPosition) as SectionViewModel?
        } else {
            null
        }
    }

    fun getSectionPosition(sectionUid: String): Int? {
        return sectionPositions[sectionUid]
    }

    fun isSection(position: Int): Boolean {
        return if (position <= itemCount) {
            getItemViewType(position) == DataEntryViewHolderTypes.SECTION.ordinal
        } else {
            false
        }
    }

    override fun onNext(position: Int) {
        if (position < itemCount - 1) {
            onItemClick(position + 1)
        }
    }

    override fun onShowDialog(title: String, message: String?) {
        didItemShowDialog?.let { action ->
            action(title, message)
        }
    }

    override fun onItemClick(position: Int) {

        if (currentFocusPosition != position) {

            currentFocusPosition?.let {
                getItem(it).onDeactivate()
            }

            if (getItem(position) is SectionViewModel) {
                currentFocusPosition = null
            } else {
                currentFocusPosition = position
                getItem(currentFocusPosition!!).onActivate()
            }
        }
    }
}
