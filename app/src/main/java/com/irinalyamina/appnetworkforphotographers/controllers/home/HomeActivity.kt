package com.irinalyamina.appnetworkforphotographers.controllers.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.controllers.FromActivity
import com.irinalyamina.appnetworkforphotographers.controllers.post.PostsAdapter
import com.irinalyamina.appnetworkforphotographers.controllers.map.MapActivity
import com.irinalyamina.appnetworkforphotographers.controllers.messenger.MessengerActivity
import com.irinalyamina.appnetworkforphotographers.controllers.search.SearchActivity
import com.irinalyamina.appnetworkforphotographers.controllers.profile.UserProfileActivity
import com.irinalyamina.appnetworkforphotographers.databinding.ActivityHomeBinding
import com.irinalyamina.appnetworkforphotographers.models.Category
import com.irinalyamina.appnetworkforphotographers.models.CategoryDirectory
import com.irinalyamina.appnetworkforphotographers.models.Post
import com.irinalyamina.appnetworkforphotographers.service.CategoryService
import com.irinalyamina.appnetworkforphotographers.service.PostService
import java.time.LocalDateTime

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var listPostsNews: ArrayList<Post>
    private lateinit var listPostsOther: ArrayList<Post>

    private lateinit var postsAdapterNews: PostsAdapter
    private lateinit var postsAdapterOther: PostsAdapter

    private var tabLayoutIndex: Int = 0
    private var tabLayoutWhatToShowIndex: Int = 0

    private var categoryId: Int = -1
    private var periodIndex: Int = -1

    private lateinit var listPeriods: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listPeriods = arrayListOf(
            getString(R.string.text_period_all_time),
            getString(R.string.text_period_day),
            getString(R.string.text_period_week),
            getString(R.string.text_period_month),
            getString(R.string.text_period_year)
        )

        onCreateBottomNavigationView()

        binding.recyclerViewNews.layoutManager = LinearLayoutManager(this)
        postsAdapterNews = PostsAdapter(this, FromActivity.home)
        binding.recyclerViewNews.adapter = postsAdapterNews

        binding.recyclerViewOther.layoutManager = LinearLayoutManager(this)
        postsAdapterOther = PostsAdapter(this, FromActivity.home)
        binding.recyclerViewOther.adapter = postsAdapterOther

        initialDate()
        onTabLayout()

        binding.btnFilter.setOnClickListener { btnFilterOnClickListener() }
        binding.btnCloseFilter.setOnClickListener { btnCloseFilterOnClickListener() }

        onTabLayoutWhatToShow()
        initSpinnerPeriod()
        initSpinnerCategory()

        binding.btnApplyFilter.setOnClickListener { btnApplyFilterOnClickListener() }
    }

    private fun initialDate() {
        val postService = PostService(this)

        listPostsNews = postService.getPostsNews()
        if (listPostsNews.isNotEmpty()) {
            postsAdapterNews.setListPosts(listPostsNews)
        }

        listPostsOther = postService.getPostsOther()
        if (listPostsOther.isNotEmpty()) {
            postsAdapterOther.setListPosts(listPostsOther)
        }
    }

    private fun btnApplyFilterOnClickListener() {
        if (tabLayoutIndex == 0) {
            val filterList: ArrayList<Post> =
                if (categoryId == -1) {
                    ArrayList(listPostsNews)
                } else {
                    ArrayList(listPostsNews.filter { it.categoryId == categoryId })
                }

            postsAdapterNews.setListPosts(filterList)

        } else {
            var filterList: ArrayList<Post>

            if (tabLayoutWhatToShowIndex == 0) {
                filterList =
                    if (categoryId == -1) {
                        ArrayList(listPostsOther)
                    } else {
                        ArrayList(listPostsOther.filter { it.categoryId == categoryId })
                    }
            } else {
                filterList =
                    if (categoryId == -1) {
                        ArrayList(listPostsOther)
                    } else {
                        ArrayList(listPostsOther.filter { it.categoryId == categoryId })
                    }

                when (periodIndex) {
                    0 -> {
                        filterList.sortByDescending { it.listLikes.size }
                    }
                    1 -> {
                        val date = LocalDateTime.now().minusDays(1)
                        filterList.removeAll { it.uploadDate < date }
                        filterList.sortByDescending { it.listLikes.size }
                    }
                    2 -> {
                        val date = LocalDateTime.now().minusWeeks(1)
                        filterList.removeAll { it.uploadDate < date }
                        filterList.sortByDescending { it.listLikes.size }
                    }
                    3 -> {
                        val date = LocalDateTime.now().minusMonths(1)
                        filterList.removeAll { it.uploadDate < date }
                        filterList.sortByDescending { it.listLikes.size }
                    }
                    4 -> {
                        val date = LocalDateTime.now().minusYears(1)
                        filterList.removeAll { it.uploadDate < date }
                        filterList.sortByDescending { it.listLikes.size }
                    }
                }

            }

            postsAdapterOther.setListPosts(filterList)
        }

        btnCloseFilterOnClickListener()
    }

    private fun btnFilterOnClickListener() {
        binding.layoutFilter.visibility = View.VISIBLE

        if (tabLayoutIndex == 0) {
            binding.titleWhatToShow.visibility = View.GONE
            binding.tabLayoutWhatToShow.visibility = View.GONE

            binding.titlePeriod.visibility = View.GONE
            binding.spinnerPeriod.visibility = View.GONE
        } else {
            binding.titleWhatToShow.visibility = View.VISIBLE
            binding.tabLayoutWhatToShow.visibility = View.VISIBLE
        }
    }

    private fun btnCloseFilterOnClickListener() {
        binding.layoutFilter.visibility = View.GONE
    }

    private fun onTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (binding.tabLayout.selectedTabPosition) {
                    0 -> {
                        tabLayoutIndex = 0
                        binding.recyclerViewNews.visibility = View.VISIBLE
                        binding.recyclerViewOther.visibility = View.GONE
                    }
                    1 -> {
                        tabLayoutIndex = 1
                        binding.recyclerViewNews.visibility = View.GONE
                        binding.recyclerViewOther.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun onTabLayoutWhatToShow() {
        binding.tabLayoutWhatToShow.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (binding.tabLayoutWhatToShow.selectedTabPosition) {
                    0 -> {
                        tabLayoutWhatToShowIndex = 0
                        binding.titlePeriod.visibility = View.GONE
                        binding.spinnerPeriod.visibility = View.GONE
                    }
                    1 -> {
                        tabLayoutWhatToShowIndex = 1
                        binding.titlePeriod.visibility = View.VISIBLE
                        binding.spinnerPeriod.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun initSpinnerPeriod() {
        val adapterPeriods: ArrayAdapter<String> = ArrayAdapter(
            this@HomeActivity,
            android.R.layout.simple_spinner_item,
            listPeriods
        )
        adapterPeriods.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerPeriod.adapter = adapterPeriods
        binding.spinnerPeriod.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    periodIndex = position
                }
            }
    }

    private fun initSpinnerCategory() {
        val categoryService = CategoryService(this)
        val hashMap = categoryService.getAllCategoriesWithDirectories()

        if (hashMap != null) {
            val listCategoryDirectories: ArrayList<CategoryDirectory> =
                arrayListOf(CategoryDirectory(-1, "All"))
            for ((key, value) in hashMap) {
                listCategoryDirectories.add(key)
            }

            val adapterCategoryDirectories: ArrayAdapter<CategoryDirectory> =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, listCategoryDirectories)
            adapterCategoryDirectories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.spinnerCategoryDirectory.adapter = adapterCategoryDirectories
            binding.spinnerCategoryDirectory.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (position == 0) {
                            val adapterCategories: ArrayAdapter<String> = ArrayAdapter(
                                this@HomeActivity,
                                android.R.layout.simple_spinner_item,
                                arrayListOf("All")
                            )
                            adapterCategories.setDropDownViewResource(
                                android.R.layout.simple_spinner_dropdown_item
                            )

                            binding.spinnerCategory.adapter = adapterCategories
                            binding.spinnerCategory.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        categoryId = -1
                                    }
                                }
                        } else {
                            var index = 0
                            for ((key, value) in hashMap) {
                                if (index == position - 1) {
                                    val indexNow = index
                                    val adapterCategories: ArrayAdapter<Category> = ArrayAdapter(
                                        this@HomeActivity,
                                        android.R.layout.simple_spinner_item,
                                        value
                                    )
                                    adapterCategories.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item
                                    )

                                    binding.spinnerCategory.adapter = adapterCategories
                                    binding.spinnerCategory.onItemSelectedListener =
                                        object : AdapterView.OnItemSelectedListener {
                                            override fun onNothingSelected(parent: AdapterView<*>?) {}

                                            override fun onItemSelected(
                                                parent: AdapterView<*>?,
                                                view: View?,
                                                position: Int,
                                                id: Long
                                            ) {
                                                var indexPosition = 0
                                                for ((key, value) in hashMap) {
                                                    if (indexPosition == indexNow) {
                                                        categoryId = value[position].id
                                                    }
                                                    indexPosition++
                                                }
                                            }
                                        }
                                }
                                index++
                            }
                        }
                    }
                }
        }
    }

    private fun onCreateBottomNavigationView() {
        val bottomNavView: BottomNavigationView = binding.bottomNavView
        bottomNavView.selectedItemId = R.id.nav_home

        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    return@setOnItemSelectedListener true
                }
                R.id.nav_search -> {
                    startActivity(Intent(applicationContext, SearchActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_map -> {
                    startActivity(Intent(applicationContext, MapActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_messenger -> {
                    startActivity(Intent(applicationContext, MessengerActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(applicationContext, UserProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}