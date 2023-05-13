package com.example.x_comic.views.main.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.x_comic.R
import com.example.x_comic.adapters.CollectionAdapter
import com.example.x_comic.models.Book
import com.example.x_comic.models.BookReading
import com.example.x_comic.models.BookSneek
import com.example.x_comic.models.CollectionBook
import com.example.x_comic.views.main.CollectionActivity
import kotlin.random.Random


class Collection : Fragment(), CollectionDialogFragment.OnInputListener {
    private var collectionName: String = ""
    val bookList: MutableList<BookSneek> = mutableListOf(
        BookSneek("How to Burn The Bad Boy", "alsophanie", R.drawable.bookcover, 4.9),
        BookSneek("Temporarily", "bbiboo123", R.drawable.book_cover_1, 4.5),
        BookSneek("Rome Is Us", "ann_beyond", R.drawable.book_cover_2, 4.0),
        BookSneek("Fool Man", "landyshere", R.drawable.book_cover_3, 4.7),
        BookSneek("The Mind Of A Leader", "vivianneee", R.drawable.book_cover_4, 4.3),
        BookSneek("The Light Beyond The Garden Wall", "pixiequinn", R.drawable.book_cover_5, 4.5),
        BookSneek("The Secret At The Joneses", "rhjulxie", R.drawable.book_cover_6, 3.9),
        BookSneek("The Victim's Picture", "gizashey", R.drawable.book_cover_7, 4.2)

    )
    var bookDetailList: MutableList<Book> = mutableListOf(

        Book(
            bookList[0],
            253.2,
            125.5,
            20,
            arrayListOf("Romance", "Thriller", "Short Story", "Humor")
        ),
        Book(bookList[1], 154.4, 100.3, 50, arrayListOf("Fiction", "Horror", "Mystery", "Humor")),
        Book(bookList[2], 179.6, 122.2, 25, arrayListOf("School Life", "Humor", "Short Story")),
        Book(
            bookList[3],
            211.3,
            112.6,
            34,
            arrayListOf("Romance", "Mystery", "Short Story", "Humor")
        ),
        Book(
            bookList[4],
            236.2,
            109.7,
            36,
            arrayListOf("Fiction", "Thriller", "Mystery", "Horror", "Humor", "Romance")
        )
    )

    var listCollection: MutableList<CollectionBook> = mutableListOf();
    var btnAddCollection: Button? = null;
    fun getListCollection(){
        repeat(10)
        {
            val collectionBook = CollectionBook(
                name = "Reading List"
               , bookList = mutableListOf<Book>(
                   bookDetailList[Random.nextInt(0,bookDetailList.size)],
                   bookDetailList[Random.nextInt(0,bookDetailList.size)],
                   bookDetailList[Random.nextInt(0,bookDetailList.size)],
                   bookDetailList[Random.nextInt(0,bookDetailList.size)],
                   bookDetailList[Random.nextInt(0,bookDetailList.size)],

               )

            )
            listCollection.add(collectionBook)
        }
    }
    var collectionBook: RecyclerView? = null;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_collection, container, false)

        collectionBook = view.findViewById(R.id.collectionBook);

        btnAddCollection = view.findViewById(R.id.btn_add_collection);

        btnAddCollection!!.setOnClickListener{
            val dialog = CollectionDialogFragment()
            dialog.show((context as? FragmentActivity)!!.supportFragmentManager,"dbchau10");
        }
        getListCollection();
        println(listCollection);

       val adapter = CollectionAdapter(listCollection);

        collectionBook!!.adapter = adapter;

        collectionBook!!.layoutManager = GridLayoutManager(this.context, 3)

        adapter!!.onItemClick = { collection ->

            val intent = Intent(getActivity(), CollectionActivity::class.java)
            startActivityForResult(intent, 123)

        }

        fun checkNumberRule(num: Int): Boolean {
            var curr = 0 // start with 0
            var diff = 3 // initialize the difference between consecutive numbers to 3
            while (curr < num) {
                curr += diff // add the current difference to the previous number
                diff = if (diff == 3) 4 else 3 // switch between adding 3 and 4 to the previous number
            }
            return curr == num // if the final number is equal to the input number, it satisfies the rule
        }

        val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
          return if (checkNumberRule(position)) 2 else 1 // Make every third item span 2 columns

            }
        }
        (collectionBook!!.layoutManager as GridLayoutManager).spanSizeLookup = spanSizeLookup



        return view
    }

    override fun sendInput(input: String) {
        collectionName = input
    }


}

class CollectionDialogFragment : DialogFragment() {


    private lateinit var mOnInputListener: OnInputListener
    private lateinit var mInput: EditText
    private lateinit var mActionOk: TextView
    private lateinit var mActionCancel: TextView

    interface OnInputListener {
        fun sendInput(input: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_collection_fragment, container, false)
        mActionCancel = view.findViewById(R.id.action_cancel)
        mActionOk = view.findViewById(R.id.action_ok)
        mInput = view.findViewById(R.id.input)

        mActionCancel.setOnClickListener {
            dialog?.dismiss()
        }

        mActionOk.setOnClickListener {
            val input = mInput.text.toString()
            mOnInputListener.sendInput(input)
            dialog?.dismiss()
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mOnInputListener = activity as OnInputListener
        } catch (e: ClassCastException) {
            Log.e("dialog", "onAttach: ClassCastException: " + e.message)
        }
    }
}