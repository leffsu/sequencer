package org.nullpointerexception.sampler.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.holder_sound.view.*
import org.nullpointerexception.sampler.R
import org.nullpointerexception.sampler.entity.SoundEntity


class HomeAdapter(private val soundList: List<SoundEntity>) :
    RecyclerView.Adapter<HomeAdapter.SoundHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.holder_sound, parent, false)
        val holder = SoundHolder(view)

        holder.listen { position, beat, type ->
            println("BEAT CLICKED $position $beat")
            val array = soundList[position].array
            soundList[position].play()
            // Переключение бита
            array[beat] = !array[beat]
            notifyItemChanged(position)
        }

        return holder
    }

    override fun getItemCount(): Int {
        return soundList.size
    }

    override fun onBindViewHolder(holder: SoundHolder, position: Int) {
        holder.bind(soundList[position])
    }

    inner class SoundHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var beatCells = arrayOf<View>()

        init {
            beatCells = arrayOf(
                itemView.imgBeat1,
                itemView.imgBeat2, itemView.imgBeat3,
                itemView.imgBeat4, itemView.imgBeat5,
                itemView.imgBeat6, itemView.imgBeat7,
                itemView.imgBeat8, itemView.imgBeat9,
                itemView.imgBeat10, itemView.imgBeat11,
                itemView.imgBeat12, itemView.imgBeat13,
                itemView.imgBeat14, itemView.imgBeat15,
                itemView.imgBeat16
            )
        }

        fun bind(soundEntity: SoundEntity) {
            // Иконка
            itemView.imgIcon.setImageResource(soundEntity.icon)
            // Установка цвета битов
            setBeatColor(
                soundEntity, beatCells
            )
        }

        private fun setBeatColor(soundEntity: SoundEntity, view: Array<View>) {
            // Включен - оранжевый, выключен - бирюзовато-блевотный
            for (x in view.indices) {
                setBeatColor(soundEntity, view[x], x)
            }
        }

        private fun setBeatColor(soundEntity: SoundEntity, view: View, position: Int) {
            // Включен - оранжевый, выключен - бирюзовато-блевотный
            val array = soundEntity.array
            if (array[position]) {
                view.setBackgroundResource(soundEntity.activeColorResId)
            } else {
                view.setBackgroundResource(R.color.colorInactive)
            }
        }
    }
}

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, beatNumber: Int, type: Int) -> Unit): T {

    // Да, второй массив. Потому что разные scope'ы.
    val array = arrayOf(
        itemView.imgBeat1,
        itemView.imgBeat2, itemView.imgBeat3,
        itemView.imgBeat4, itemView.imgBeat5,
        itemView.imgBeat6, itemView.imgBeat7,
        itemView.imgBeat8, itemView.imgBeat9,
        itemView.imgBeat10, itemView.imgBeat11,
        itemView.imgBeat12, itemView.imgBeat13,
        itemView.imgBeat14, itemView.imgBeat15,
        itemView.imgBeat16
    )

    // Слушатель без утечек памяти
    for (x in array.indices) {
        array[x].setOnClickListener {
            event.invoke(adapterPosition, x, itemViewType)
        }
    }
    return this
}