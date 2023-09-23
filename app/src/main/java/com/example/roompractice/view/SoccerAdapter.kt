import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.roompractice.R
import com.example.roompractice.databinding.ListitemPlayerCardBinding
import com.example.roompractice.db.SoccerEntity
import java.util.Locale


class SoccerAdapter :
    ListAdapter<SoccerEntity, SoccerAdapter.SoccerViewHolder>(SoccerEntityDiffCallback()) {

    val selectedItems = mutableSetOf<SoccerEntity>()
    private var onItemSelectedListener: OnItemSelectedListener? = null

    fun setOnItemSelectedListener(listener: OnItemSelectedListener) {
        onItemSelectedListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoccerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListitemPlayerCardBinding.inflate(inflater, parent, false)
        return SoccerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SoccerViewHolder, position: Int) {
        val soccer = getItem(position)
        holder.bind(soccer)

        // 아이템 선택 처리
        holder.itemView.setOnClickListener {
            if (selectedItems.contains(soccer)) {
                selectedItems.remove(soccer)
            } else {
                selectedItems.add(soccer)
            }
            onItemSelectedListener?.onItemSelected(selectedItems.size)

            // 아이템의 배경색 변경
            holder.itemView.setBackgroundResource(
                if (selectedItems.contains(soccer)) R.drawable.shape_selected
                else 0 // 배경색 없음
            )
        }

        // 아이템 선택 상태 설정
        holder.itemView.isSelected = selectedItems.contains(soccer)
    }

    fun getSelectedItems(): List<SoccerEntity> {
        return selectedItems.toList()
    }

    fun clearSelectedItems() {
        selectedItems.clear()
        notifyDataSetChanged()
        onItemSelectedListener?.onItemSelected(0)
    }

    interface OnItemSelectedListener {
        fun onItemSelected(selectedCount: Int)
    }

    inner class SoccerViewHolder(private val binding: ListitemPlayerCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(soccer: SoccerEntity) {
            binding.nameTxt.text = soccer.name
            binding.teamTxt.text = soccer.team
            binding.nationalityTxt.text = soccer.nationality

            // 이미지 설정 함수 호출
            setImageViewBasedOnName(binding.playerImg, soccer.name)
        }
    }

    private fun setImageViewBasedOnName(imageView: ImageView, name: String) {
        // 이름을 소문자로 변환하고 공백을 제거하여 리소스 이름을 생성
        val resourceName = name.lowercase(Locale.getDefault()).replace(" ", "_")

        // 리소스 ID를 찾음
        val resourceId = imageView.context.resources.getIdentifier(resourceName, "drawable", imageView.context.packageName)

        // 리소스 ID가 유효한 경우 해당 이미지를 설정하고, 그렇지 않은 경우 기본 이미지를 설정
        if (resourceId != 0) {
            imageView.setImageResource(resourceId)
        } else {
            imageView.setImageResource(R.drawable.ic_person_black_64) // 기본 이미지 설정
        }
    }

}

class SoccerEntityDiffCallback : DiffUtil.ItemCallback<SoccerEntity>() {

    override fun areItemsTheSame(oldItem: SoccerEntity, newItem: SoccerEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SoccerEntity, newItem: SoccerEntity): Boolean {
        return oldItem == newItem
    }
}

