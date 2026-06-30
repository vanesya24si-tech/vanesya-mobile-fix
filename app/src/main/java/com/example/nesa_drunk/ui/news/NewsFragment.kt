package com.example.nesa_drunk.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nesa_drunk.R
import com.example.nesa_drunk.api.NewsItem
import com.example.nesa_drunk.api.RetrofitClient
import com.example.nesa_drunk.database.FavoriteNewsItem
import com.example.nesa_drunk.database.VillageDatabase
import com.example.nesa_drunk.databinding.FragmentNewsBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: VillageDatabase
    private lateinit var newsAdapter: NewsAdapter

    private var currentTab = 0 // 0: Online, 1: Favorites
    private var currentPublisher = "antara"
    private var currentCategory = "politik"

    private val onlineNewsList = mutableListOf<NewsItem>()
    private val favoriteNewsList = mutableListOf<NewsItem>()
    private var favoriteTitlesSet = setOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()
        db = VillageDatabase.getInstance(context)

        setupRecyclerView()
        setupTabLayout()
        setupCategoryChips()

        loadFavorites()

        binding.swipeRefreshNews.setOnRefreshListener {
            if (currentTab == 0) {
                fetchOnlineNews()
            } else {
                binding.swipeRefreshNews.isRefreshing = false
                loadFavorites()
            }
        }

        fetchOnlineNews()
    }

    private fun loadFavorites() {
        lifecycleScope.launch(Dispatchers.IO) {
            val favorites = db.getAllFavoriteNews()
            withContext(Dispatchers.Main) {
                favoriteTitlesSet = favorites.map { it.title }.toSet()
                favoriteNewsList.clear()
                favoriteNewsList.addAll(favorites.map {
                    NewsItem(
                        link = it.link ?: "",
                        title = it.title,
                        date = it.date,
                        description = it.description,
                        thumbnail = it.image
                    )
                })
                updateUI()
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(
            requireContext(),
            emptyList(),
            emptySet(),
            onReadMoreClicked = { item ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Tidak dapat membuka berita", Toast.LENGTH_SHORT).show()
                }
            },
            onFavoriteClicked = { item, isFav ->
                toggleFavorite(item, isFav)
            }
        )

        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }
    }

    private fun setupTabLayout() {
        binding.tabLayoutNews.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTab = tab?.position ?: 0
                if (currentTab == 0) {
                    binding.newsCategoryScroll.visibility = View.VISIBLE
                } else {
                    binding.newsCategoryScroll.visibility = View.GONE
                    loadFavorites()
                }
                updateUI()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupCategoryChips() {
        binding.chipGroupNews.setOnCheckedStateChangeListener { group, checkedIds ->
            val checkedId = checkedIds.firstOrNull() ?: return@setOnCheckedStateChangeListener
            when (checkedId) {
                R.id.chipGov -> {
                    currentPublisher = "antara"
                    currentCategory = "politik"
                }
                R.id.chipEdu -> {
                    currentPublisher = "republika"
                    currentCategory = "pendidikan"
                }
                R.id.chipHealth -> {
                    currentPublisher = "antara"
                    currentCategory = "humaniora"
                }
                R.id.chipUmkm -> {
                    currentPublisher = "antara"
                    currentCategory = "ekonomi"
                }
                R.id.chipAgri -> {
                    currentPublisher = "antara"
                    currentCategory = "humaniora"
                }
            }
            fetchOnlineNews()
        }
    }

    private fun fetchOnlineNews() {
        binding.pbNews.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.newsApiService.getNews(currentPublisher, currentCategory)
                }
                if (response.success && response.data?.posts != null) {
                    onlineNewsList.clear()
                    onlineNewsList.addAll(response.data.posts)
                } else {
                    loadLocalMockNews()
                }
            } catch (e: Exception) {
                loadLocalMockNews()
            } finally {
                binding.pbNews.visibility = View.GONE
                binding.swipeRefreshNews.isRefreshing = false
                updateUI()
            }
        }
    }

    private fun loadLocalMockNews() {
        onlineNewsList.clear()
        val checkedChipId = binding.chipGroupNews.checkedChipId
        val mockData = when (checkedChipId) {
            R.id.chipGov -> listOf(
                NewsItem(
                    title = "Pembangunan Balai Desa Baru Mulai Memasuki Tahap Fondasi",
                    date = "Selasa, 30 Juni 2026",
                    description = "Pemerintah Desa mengumumkan dimulainya proyek renovasi total balai kemasyarakatan guna meningkatkan kenyamanan pelayanan administrasi warga.",
                    link = "https://example.com/balai-desa",
                    thumbnail = "https://picsum.photos/id/1018/800/400"
                ),
                NewsItem(
                    title = "Musyawarah Perencanaan Pembangunan Desa Bahas Prioritas Anggaran 2026",
                    date = "Senin, 29 Juni 2026",
                    description = "Rapat koordinasi bersama BPD, LPM, dan tokoh masyarakat telah berhasil menyepakati alokasi dana desa untuk infrastruktur jalan pertanian.",
                    link = "https://example.com/musrenbang",
                    thumbnail = "https://picsum.photos/id/1074/800/400"
                )
            )
            R.id.chipEdu -> listOf(
                NewsItem(
                    title = "Penyaluran Program Beasiswa Pendidikan Anak Berprestasi Desa Bina",
                    date = "Sabtu, 27 Juni 2026",
                    description = "Sebagai upaya menekan angka putus sekolah, sepuluh pelajar tingkat dasar dan menengah menerima bantuan alat tulis serta subsidi SPP tahunan.",
                    link = "https://example.com/beasiswa",
                    thumbnail = "https://picsum.photos/id/1062/800/400"
                ),
                NewsItem(
                    title = "Ruang Baca Taman Pintar Desa Resmi Membuka Kelas Belajar Komputer Gratis",
                    date = "Kamis, 25 Juni 2026",
                    description = "Mulai bulan depan, anak-anak usia sekolah dasar dapat mengikuti kursus pengenalan teknologi dasar setiap akhir pekan tanpa biaya.",
                    link = "https://example.com/taman-pintar",
                    thumbnail = "https://picsum.photos/id/1010/800/400"
                )
            )
            R.id.chipHealth -> listOf(
                NewsItem(
                    title = "Pemeriksaan Kesehatan Gratis & Pembagian Makanan Bergizi Balita",
                    date = "Jumat, 26 Juni 2026",
                    description = "Kader Posyandu berkolaborasi dengan Puskesmas menggelar imunisasi rutin bulanan serta penyuluhan nutrisi sehat guna cegah stunting dini.",
                    link = "https://example.com/posyandu-sehat",
                    thumbnail = "https://picsum.photos/id/838/800/400"
                ),
                NewsItem(
                    title = "Senam Kebugaran Lansia Digelar di Lapangan Serbaguna Minggu Pagi",
                    date = "Minggu, 28 Juni 2026",
                    description = "Kegiatan senam jantung sehat bersama instruktur terlatih dihadiri antusias oleh puluhan warga lansia untuk menjaga stamina tubuh tetap bugar.",
                    link = "https://example.com/senam-lansia",
                    thumbnail = "https://picsum.photos/id/64/800/400"
                )
            )
            R.id.chipUmkm -> listOf(
                NewsItem(
                    title = "Bazaar Kreatif UMKM Desa Sukses Memamerkan Puluhan Produk Kerajinan Tangan",
                    date = "Rabu, 24 Juni 2026",
                    description = "Warga setempat memamerkan produk olahan keripik pisang madu, anyaman bambu, hingga batik tulis khas lokal yang menarik minat pembeli luar daerah.",
                    link = "https://example.com/bazaar-umkm",
                    thumbnail = "https://picsum.photos/id/192/800/400"
                ),
                NewsItem(
                    title = "Pelatihan Pemasaran E-Commerce bagi Pelaku Usaha Mikro Rumahan",
                    date = "Senin, 22 Juni 2026",
                    description = "Bekerja sama dengan praktisi digital, puluhan ibu rumah tangga diajarkan cara mengunggah produk ke marketplace online dan mempromosikannya.",
                    link = "https://example.com/pelatihan-marketplace",
                    thumbnail = "https://picsum.photos/id/2/800/400"
                )
            )
            R.id.chipAgri -> listOf(
                NewsItem(
                    title = "Kelompok Tani Mandiri Terima Bantuan Pupuk Cair & Benih Padi Unggul",
                    date = "Sabtu, 20 Juni 2026",
                    description = "Bantuan dari Dinas Pertanian Kabupaten disalurkan merata ke empat sub-blok persawahan untuk mendukung ketahanan pangan lokal tahun ini.",
                    link = "https://example.com/bantuan-tani",
                    thumbnail = "https://picsum.photos/id/1027/800/400"
                ),
                NewsItem(
                    title = "Sosialisasi Sistem Irigasi Tetes Modern Guna Hadapi Musim Kemarau",
                    date = "Kamis, 18 Juni 2026",
                    description = "Penyuluh membekali para petani hortikultura teknik irigasi hemat air berbasis selang tetes sederhana yang efektif menjaga kelembapan tanah.",
                    link = "https://example.com/irigasi-modern",
                    thumbnail = "https://picsum.photos/id/152/800/400"
                )
            )
            else -> emptyList()
        }
        onlineNewsList.addAll(mockData)
    }

    private fun updateUI() {
        if (currentTab == 0) {
            newsAdapter.updateData(onlineNewsList, favoriteTitlesSet)
        } else {
            newsAdapter.updateData(favoriteNewsList, favoriteTitlesSet)
        }
    }

    private fun toggleFavorite(item: NewsItem, isCurrentlyFav: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (isCurrentlyFav) {
                db.deleteFavoriteNewsByTitle(item.title)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Dihapus dari Favorit", Toast.LENGTH_SHORT).show()
                    loadFavorites()
                }
            } else {
                val fav = FavoriteNewsItem(
                    title = item.title,
                    image = item.thumbnail,
                    author = currentPublisher.uppercase(),
                    date = item.date,
                    description = item.description,
                    link = item.link
                )
                db.insertFavoriteNews(fav)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show()
                    loadFavorites()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
