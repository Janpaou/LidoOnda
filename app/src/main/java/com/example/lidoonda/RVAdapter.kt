import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.lidoonda.DataClass
import com.example.lidoonda.DataClassPrenotazioni
import com.example.lidoonda.R
import com.example.lidoonda.ImageData
import com.example.lidoonda.ListaPrenotazioniFragment
import com.example.lidoonda.idUtente
import com.example.lidoonda.makeQueryPagamento
import com.example.lidoonda.makeQueryValore
import com.example.lidoonda.makeUpdate

class ImageAdapter(private val imageList: ArrayList<ImageData>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageList[position]
        holder.imageView.setImageResource(imageUrl.dataImage )
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.rvImage)
    }
}

class AdapterClass(private val dataList: ArrayList<DataClass>) : RecyclerView.Adapter<AdapterClass.ViewHolderClass>() {

    private lateinit var mListener : onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolderClass(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvImage.setImageResource(currentItem.dataImage)
        holder.rvTitle.text = currentItem.dataTitle
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolderClass(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val rvImage: ImageView = itemView.findViewById(R.id.rvImageServizi)
        val rvTitle: TextView = itemView.findViewById(R.id.rvTitleServizi)
        init {
        itemView.setOnClickListener{
            listener.onItemClick(adapterPosition)
        }
        }
    }
}

class PrenotazioniClass(private val dataList: ArrayList<DataClassPrenotazioni>) : RecyclerView.Adapter<PrenotazioniClass.ViewHolderClass>() {
    var listData = dataList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_prenotazioni, parent, false)
        return ViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = listData[position]
        holder.rvImage.setImageResource(currentItem.dataImage)
        holder.rvTitle.text = currentItem.dataTitle
        holder.rvButton.visibility = currentItem.dataButton
        holder.rvButton.setOnClickListener { changeButton( holder, position) }
        holder.buttonRemove.setOnClickListener{deleteItem(position, holder)}
        holder.buttonAnnulla.setOnClickListener { changeButton(holder, position) }
        holder.buttonPagamento.setOnClickListener { pagamentoButton(holder, position) }

    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvImage: ImageView = itemView.findViewById(R.id.rvImagePrenotazioni)
        val rvTitle: TextView = itemView.findViewById(R.id.rvTitlePrenotazioni)
        val rvButton : Button = itemView.findViewById(R.id.rvButtonPrenotazioni)
        val buttonRemove : Button = itemView.findViewById(R.id.rvButtonRimuovi)
        val buttonAnnulla : Button = itemView.findViewById(R.id.rvButtonAnnulla)
        val buttonPagamento : Button = itemView.findViewById(R.id.rvButtonPagamento)

    }

    private fun deleteItem( index: Int, holder:ViewHolderClass) {
        Log.v("Index", index.toString())
        ListaPrenotazioniFragment().getPosition(index, listData)
        listData.removeAt(index)
        changeButton(holder, index)
        notifyDataSetChanged()
    }
    fun changeButton( holder:ViewHolderClass, position: Int){
        var buttonDettagli : Button = holder.itemView.findViewById(R.id.rvButtonPrenotazioni)
        if (buttonDettagli.isVisible){
            holder.itemView.findViewById<ImageView>(R.id.rvImagePrenotazioni).visibility = View.INVISIBLE
            holder.itemView.findViewById<TextView>(R.id.rvTitlePrenotazioni).visibility = View.INVISIBLE
            buttonDettagli.visibility = View.INVISIBLE
            holder.itemView.findViewById<Button>(R.id.rvButtonPagamento).visibility = View.VISIBLE
            holder.itemView.findViewById<Button>(R.id.rvButtonRimuovi).visibility = View.VISIBLE
            holder.itemView.findViewById<Button>(R.id.rvButtonAnnulla).visibility = View.VISIBLE

            if (pagamentoEffettuato(position) == 1){
                holder.itemView.findViewById<Button>(R.id.rvButtonPagamento).text = "Pagato"
                holder.itemView.findViewById<Button>(R.id.rvButtonPagamento).isClickable = false
            }
        }else{
            holder.itemView.findViewById<ImageView>(R.id.rvImagePrenotazioni).visibility = View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.rvTitlePrenotazioni).visibility = View.VISIBLE
            buttonDettagli.visibility = View.VISIBLE
            holder.itemView.findViewById<Button>(R.id.rvButtonPagamento).visibility = View.INVISIBLE
            holder.itemView.findViewById<Button>(R.id.rvButtonRimuovi).visibility = View.INVISIBLE
            holder.itemView.findViewById<Button>(R.id.rvButtonAnnulla).visibility = View.INVISIBLE
        }
    }
    private fun pagamentoButton(holder: ViewHolderClass, position: Int) {

        var chiamata = makeQueryPagamento("select * from pagamento where idutente='$idUtente'")
        Log.v("chiamata", chiamata)
        if (chiamata == "[]"){
            Toast.makeText(holder.itemView.context, "Inserire metodo di pagamento", Toast.LENGTH_SHORT).show()
        }else{
            var item = listData[position].dataTitle
            var data = item.substring(7,17)
            var ora = item.substring(25,27)
            Log.v("item", ora)
            makeUpdate("update prenotazioni set prenotato = 1 where data='$data' and ora='$ora'")
            Toast.makeText(holder.itemView.context, "Pagamento effettuato", Toast.LENGTH_SHORT).show()
            changeButton(holder, position)
        }

    }
    private fun pagamentoEffettuato(position: Int) : Int{
        var item = listData[position].dataTitle
        var data = item.substring(7,17)
        var ora = item.substring(25,27)
        var pagamento = makeQueryValore("select prenotato from prenotazioni where idutente = '$idUtente' and data='$data' and ora='$ora'")
        Log.v("item", pagamento.toString())
        return pagamento
    }


}
