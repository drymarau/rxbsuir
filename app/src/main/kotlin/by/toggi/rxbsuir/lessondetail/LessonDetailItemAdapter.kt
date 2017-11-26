package by.toggi.rxbsuir.lessondetail

import android.content.res.Resources
import android.support.annotation.DrawableRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import by.toggi.rxbsuir.DividerItemDecoration
import by.toggi.rxbsuir.R
import by.toggi.rxbsuir.activity.LessonActivity.DetailItem
import by.toggi.rxbsuir.lessondetail.LessonDetailItemAdapter.ViewHolder
import by.toggi.rxbsuir.lessondetail.LessonDetailItemAdapter.ViewHolder.NoSummary
import by.toggi.rxbsuir.lessondetail.LessonDetailItemAdapter.ViewHolder.Summary
import by.toggi.rxbsuir.util.inflater
import by.toggi.rxbsuir.util.isVisible
import kotlin.properties.Delegates

class LessonDetailItemAdapter :
    RecyclerView.Adapter<ViewHolder>(),
    DividerItemDecoration.Provider {

  var items by Delegates.observable(emptyList<DetailItem>()) { _, _, _ ->
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun getItemViewType(position: Int): Int {
    return when (items[position].type) {
      DetailItem.Type.WEEKDAY -> ViewHolder.VIEW_TYPE_SUMMARY
      else -> ViewHolder.VIEW_TYPE_NO_SUMMARY
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    parent ?: throw IllegalArgumentException("parent == null.")
    return when (viewType) {
      ViewHolder.VIEW_TYPE_SUMMARY -> Summary(parent.listItemDetailSummary)
      ViewHolder.VIEW_TYPE_NO_SUMMARY -> NoSummary(parent.listItemDetail)
      else -> throw IllegalArgumentException("viewType == $viewType.")
    }
  }

  override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    holder?.bind(items[position])
  }

  override fun hasDivider(resources: Resources, position: Int): Boolean {
    if (position == itemCount - 1) {
      return false
    }
    return items[position].type != items[position + 1].type
  }

  private val ViewGroup.listItemDetailSummary: View
    get() = inflater.inflate(R.layout.list_item_detail_summary, this, false)
  private val ViewGroup.listItemDetail: View
    get() = inflater.inflate(R.layout.list_item_detail, this, false)

  sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected val resources: Resources = itemView.resources

    abstract fun bind(item: DetailItem)

    @get:DrawableRes
    protected val DetailItem.icon: Int
      get() = when (type) {
        DetailItem.Type.TIME -> R.drawable.ic_time
        DetailItem.Type.EMPLOYEE -> R.drawable.ic_employee
        DetailItem.Type.GROUP -> R.drawable.ic_group
        DetailItem.Type.AUDITORY -> R.drawable.ic_auditory
        DetailItem.Type.WEEKDAY -> R.drawable.ic_action_today
        DetailItem.Type.NOTE -> R.drawable.ic_note
      }

    class Summary(itemView: View) : ViewHolder(itemView) {

      private val icon = itemView.findViewById<ImageView>(R.id.lesson_detail_icon)
      private val text = itemView.findViewById<TextView>(R.id.lesson_detail_text)
      private val summary = itemView.findViewById<TextView>(R.id.lesson_detail_summary)

      override fun bind(item: DetailItem) {
        icon.isVisible = item.isIconVisible
        icon.setImageResource(item.icon)
        text.text = item.text
        val summaryText = item.summary
        summary.text = when (summaryText) {
          null -> resources.getString(R.string.weekly)
          else -> resources.getQuantityString(R.plurals.week, summaryText.length, summaryText)
        }
      }
    }

    class NoSummary(itemView: View) : ViewHolder(itemView) {

      private val icon = itemView.findViewById<ImageView>(R.id.lesson_detail_icon)
      private val text = itemView.findViewById<TextView>(R.id.lesson_detail_text)

      override fun bind(item: DetailItem) {
        icon.isVisible = item.isIconVisible
        icon.setImageResource(item.icon)
        text.text = item.text
      }
    }

    companion object {

      const val VIEW_TYPE_SUMMARY = 0
      const val VIEW_TYPE_NO_SUMMARY = 1
    }
  }
}
