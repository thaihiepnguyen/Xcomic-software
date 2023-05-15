package com.example.x_comic.views.more

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.adapters.BookListAdapter
import com.example.x_comic.databinding.ActivityLastestBinding
import com.example.x_comic.databinding.ActivityPopularBinding
import com.example.x_comic.models.Product
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.views.detail.DetailActivity

class LastestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLastestBinding
    private val bookLastestList: MutableList<Product> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLastestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backImg.setOnClickListener {
            finish()
        }

        var productViewModel: ProductViewModel
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        val bookListAdapter = BookListAdapter(bookLastestList)
        bookListAdapter.onItemClick = {
                book -> nextBookDetailActivity(book)
        }

        binding.listView!!.adapter = bookListAdapter;
        binding.listView!!.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        productViewModel.getAllLatestBook { books ->
            run {
                bookLastestList.clear()
                for (book in books.children) {
                    val product = book.getValue(Product::class.java)
                    if (product != null && !product.hide) {
                        bookLastestList.add(product)
                    }
                }
                bookListAdapter.notifyDataSetChanged()
            }
        }
    }

    fun nextBookDetailActivity(book: Product) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("book_data", book.id)

        startActivity(intent)
    }
}