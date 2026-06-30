package com.example.nesa_drunk.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Database helper menggunakan SQLiteOpenHelper (tidak butuh KAPT/KSP).
 * Mengelola tabel: agenda_desa, catatan_warga, favorite_news, info_items.
 */
class VillageDatabase(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        const val DATABASE_NAME = "village_db"
        const val DATABASE_VERSION = 2

        // Table: agenda_desa
        const val TABLE_AGENDA = "agenda_desa"
        const val COL_AGENDA_ID = "id"
        const val COL_AGENDA_KEGIATAN = "kegiatan"
        const val COL_AGENDA_TANGGAL = "tanggal"
        const val COL_AGENDA_WAKTU = "waktu"
        const val COL_AGENDA_LOKASI = "lokasi"
        const val COL_AGENDA_STATUS = "status"

        // Table: catatan_warga
        const val TABLE_CATATAN = "catatan_warga"
        const val COL_CATATAN_ID = "id"
        const val COL_CATATAN_JUDUL = "judul"
        const val COL_CATATAN_ISI = "isi"
        const val COL_CATATAN_TANGGAL = "tanggal"

        // Table: favorite_news
        const val TABLE_FAVORITE = "favorite_news"
        const val COL_FAV_ID = "id"
        const val COL_FAV_TITLE = "title"
        const val COL_FAV_IMAGE = "image"
        const val COL_FAV_AUTHOR = "author"
        const val COL_FAV_DATE = "date"
        const val COL_FAV_DESC = "description"
        const val COL_FAV_LINK = "link"

        // Table: info_items
        const val TABLE_INFO = "info_items"
        const val COL_INFO_ID = "id"
        const val COL_INFO_JUDUL = "judul"
        const val COL_INFO_DESKRIPSI = "deskripsi"
        const val COL_INFO_AVATAR = "avatar"
        const val COL_INFO_IMAGE = "image_url"
        const val COL_INFO_KATEGORI = "kategori"
        const val COL_INFO_TANGGAL = "tanggal"

        @Volatile
        private var INSTANCE: VillageDatabase? = null

        fun getInstance(context: Context): VillageDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: VillageDatabase(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_AGENDA (
                $COL_AGENDA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_AGENDA_KEGIATAN TEXT NOT NULL,
                $COL_AGENDA_TANGGAL TEXT NOT NULL,
                $COL_AGENDA_WAKTU TEXT NOT NULL,
                $COL_AGENDA_LOKASI TEXT NOT NULL,
                $COL_AGENDA_STATUS TEXT NOT NULL DEFAULT 'Akan Datang'
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_CATATAN (
                $COL_CATATAN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CATATAN_JUDUL TEXT NOT NULL,
                $COL_CATATAN_ISI TEXT NOT NULL,
                $COL_CATATAN_TANGGAL TEXT NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_FAVORITE (
                $COL_FAV_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_FAV_TITLE TEXT NOT NULL,
                $COL_FAV_IMAGE TEXT,
                $COL_FAV_AUTHOR TEXT,
                $COL_FAV_DATE TEXT,
                $COL_FAV_DESC TEXT,
                $COL_FAV_LINK TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_INFO (
                $COL_INFO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_INFO_JUDUL TEXT NOT NULL,
                $COL_INFO_DESKRIPSI TEXT NOT NULL,
                $COL_INFO_AVATAR TEXT,
                $COL_INFO_IMAGE TEXT,
                $COL_INFO_KATEGORI TEXT NOT NULL DEFAULT 'Perangkat',
                $COL_INFO_TANGGAL TEXT NOT NULL
            )
        """.trimIndent())

        // Pre-populate agenda desa
        seedAgenda(db)
        seedInfoItems(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_AGENDA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATATAN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INFO")
        onCreate(db)
    }

    private fun seedAgenda(db: SQLiteDatabase) {
        val agendaList = listOf(
            Triple("Musrenbang Desa 2026", "2026-07-10", "09:00"),
            Triple("Gotong Royong Jalan Desa", "2026-07-15", "07:00"),
            Triple("Pelatihan UMKM Digital", "2026-07-20", "10:00"),
            Triple("Posyandu Rutin", "2026-07-25", "08:00"),
            Triple("Rapat BPD Bulanan", "2026-08-01", "13:00"),
            Triple("HUT Desa Ke-52", "2026-08-17", "08:00"),
        )
        agendaList.forEachIndexed { i, (kegiatan, tanggal, waktu) ->
            val lokasi = when (i % 3) {
                0 -> "Balai Desa"
                1 -> "Lapangan Desa"
                else -> "Gedung Serbaguna"
            }
            val cv = ContentValues().apply {
                put(COL_AGENDA_KEGIATAN, kegiatan)
                put(COL_AGENDA_TANGGAL, tanggal)
                put(COL_AGENDA_WAKTU, waktu)
                put(COL_AGENDA_LOKASI, lokasi)
                put(COL_AGENDA_STATUS, "Akan Datang")
            }
            db.insert(TABLE_AGENDA, null, cv)
        }
    }

    // ─── AGENDA ────────────────────────────────────────────────────────
    fun getAllAgenda(): List<AgendaItem> {
        val list = mutableListOf<AgendaItem>()
        val db = readableDatabase
        val cursor = db.query(TABLE_AGENDA, null, null, null, null, null, "$COL_AGENDA_TANGGAL ASC")
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    AgendaItem(
                        id = it.getInt(it.getColumnIndexOrThrow(COL_AGENDA_ID)),
                        kegiatan = it.getString(it.getColumnIndexOrThrow(COL_AGENDA_KEGIATAN)),
                        tanggal = it.getString(it.getColumnIndexOrThrow(COL_AGENDA_TANGGAL)),
                        waktu = it.getString(it.getColumnIndexOrThrow(COL_AGENDA_WAKTU)),
                        lokasi = it.getString(it.getColumnIndexOrThrow(COL_AGENDA_LOKASI)),
                        status = it.getString(it.getColumnIndexOrThrow(COL_AGENDA_STATUS))
                    )
                )
            }
        }
        return list
    }

    // ─── CATATAN ───────────────────────────────────────────────────────
    fun getAllCatatan(): List<CatatanItem> {
        val list = mutableListOf<CatatanItem>()
        val db = readableDatabase
        val cursor = db.query(TABLE_CATATAN, null, null, null, null, null, "$COL_CATATAN_ID DESC")
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    CatatanItem(
                        id = it.getInt(it.getColumnIndexOrThrow(COL_CATATAN_ID)),
                        judul = it.getString(it.getColumnIndexOrThrow(COL_CATATAN_JUDUL)),
                        isi = it.getString(it.getColumnIndexOrThrow(COL_CATATAN_ISI)),
                        tanggal = it.getString(it.getColumnIndexOrThrow(COL_CATATAN_TANGGAL))
                    )
                )
            }
        }
        return list
    }

    fun insertCatatan(catatan: CatatanItem): Long {
        val cv = ContentValues().apply {
            put(COL_CATATAN_JUDUL, catatan.judul)
            put(COL_CATATAN_ISI, catatan.isi)
            put(COL_CATATAN_TANGGAL, catatan.tanggal)
        }
        return writableDatabase.insert(TABLE_CATATAN, null, cv)
    }

    fun deleteCatatan(id: Int): Int {
        return writableDatabase.delete(TABLE_CATATAN, "$COL_CATATAN_ID = ?", arrayOf(id.toString()))
    }

    // ─── FAVORITE NEWS ────────────────────────────────────────────────
    fun getAllFavoriteNews(): List<FavoriteNewsItem> {
        val list = mutableListOf<FavoriteNewsItem>()
        val db = readableDatabase
        val cursor = db.query(TABLE_FAVORITE, null, null, null, null, null, "$COL_FAV_ID DESC")
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    FavoriteNewsItem(
                        id = it.getInt(it.getColumnIndexOrThrow(COL_FAV_ID)),
                        title = it.getString(it.getColumnIndexOrThrow(COL_FAV_TITLE)),
                        image = it.getString(it.getColumnIndexOrThrow(COL_FAV_IMAGE)),
                        author = it.getString(it.getColumnIndexOrThrow(COL_FAV_AUTHOR)),
                        date = it.getString(it.getColumnIndexOrThrow(COL_FAV_DATE)),
                        description = it.getString(it.getColumnIndexOrThrow(COL_FAV_DESC)),
                        link = it.getString(it.getColumnIndexOrThrow(COL_FAV_LINK))
                    )
                )
            }
        }
        return list
    }

    fun insertFavoriteNews(fav: FavoriteNewsItem): Long {
        val cv = ContentValues().apply {
            put(COL_FAV_TITLE, fav.title)
            put(COL_FAV_IMAGE, fav.image)
            put(COL_FAV_AUTHOR, fav.author)
            put(COL_FAV_DATE, fav.date)
            put(COL_FAV_DESC, fav.description)
            put(COL_FAV_LINK, fav.link)
        }
        return writableDatabase.insert(TABLE_FAVORITE, null, cv)
    }

    fun deleteFavoriteNewsByTitle(title: String): Int {
        return writableDatabase.delete(TABLE_FAVORITE, "$COL_FAV_TITLE = ?", arrayOf(title))
    }

    // ─── InfoItem CRUD ────────────────────────────────────────────────────────

    fun insertInfoItem(item: InfoItem): Long {
        val cv = ContentValues().apply {
            put(COL_INFO_JUDUL, item.judul)
            put(COL_INFO_DESKRIPSI, item.deskripsi)
            put(COL_INFO_AVATAR, item.avatar)
            put(COL_INFO_IMAGE, item.imageUrl)
            put(COL_INFO_KATEGORI, item.kategori)
            put(COL_INFO_TANGGAL, item.tanggal)
        }
        return writableDatabase.insert(TABLE_INFO, null, cv)
    }

    fun updateInfoItem(item: InfoItem): Int {
        val cv = ContentValues().apply {
            put(COL_INFO_JUDUL, item.judul)
            put(COL_INFO_DESKRIPSI, item.deskripsi)
            put(COL_INFO_AVATAR, item.avatar)
            put(COL_INFO_IMAGE, item.imageUrl)
            put(COL_INFO_KATEGORI, item.kategori)
            put(COL_INFO_TANGGAL, item.tanggal)
        }
        return writableDatabase.update(TABLE_INFO, cv, "$COL_INFO_ID = ?", arrayOf(item.id.toString()))
    }

    fun deleteInfoItem(id: Int): Int {
        return writableDatabase.delete(TABLE_INFO, "$COL_INFO_ID = ?", arrayOf(id.toString()))
    }

    fun getAllInfoByKategori(kategori: String): MutableList<InfoItem> {
        val list = mutableListOf<InfoItem>()
        val cursor = readableDatabase.query(
            TABLE_INFO, null,
            "$COL_INFO_KATEGORI = ?", arrayOf(kategori),
            null, null, "$COL_INFO_ID DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    InfoItem(
                        id = it.getInt(it.getColumnIndexOrThrow(COL_INFO_ID)),
                        judul = it.getString(it.getColumnIndexOrThrow(COL_INFO_JUDUL)),
                        deskripsi = it.getString(it.getColumnIndexOrThrow(COL_INFO_DESKRIPSI)),
                        avatar = it.getString(it.getColumnIndexOrThrow(COL_INFO_AVATAR)) ?: "",
                        imageUrl = it.getString(it.getColumnIndexOrThrow(COL_INFO_IMAGE)) ?: "",
                        kategori = it.getString(it.getColumnIndexOrThrow(COL_INFO_KATEGORI)),
                        tanggal = it.getString(it.getColumnIndexOrThrow(COL_INFO_TANGGAL))
                    )
                )
            }
        }
        return list
    }

    private fun seedInfoItems(db: SQLiteDatabase) {
        val items = listOf(
            // Perangkat
            InfoItem(judul = "Kepala Desa (Budi Santoso)", deskripsi = "Memimpin penyelenggaraan pemerintahan desa, pembinaan kemasyarakatan, dan pembangunan desa.", avatar = "https://avatar.iran.liara.run/public/30", imageUrl = "https://picsum.photos/id/1015/800/400", kategori = "Perangkat", tanggal = "08:00"),
            InfoItem(judul = "Sekretaris Desa (Siti Aminah)", deskripsi = "Mengurusi administrasi ketatausahaan, koordinator perangkat desa, dan penyusunan anggaran.", avatar = "https://avatar.iran.liara.run/public/80", imageUrl = "https://picsum.photos/id/1016/800/400", kategori = "Perangkat", tanggal = "08:15"),
            InfoItem(judul = "Kaur Keuangan (Eko Prasetyo)", deskripsi = "Mengelola administrasi keuangan desa, pelaporan pajak, dan bendahara pengeluaran.", avatar = "https://avatar.iran.liara.run/public/33", imageUrl = "https://picsum.photos/id/1020/800/400", kategori = "Perangkat", tanggal = "08:30"),
            InfoItem(judul = "Kasi Pemerintahan (Ahmad Fauzi)", deskripsi = "Menangani registrasi kependudukan, ketertiban umum, dan pembinaan hukum tingkat desa.", avatar = "https://avatar.iran.liara.run/public/34", imageUrl = "https://picsum.photos/id/1040/800/400", kategori = "Perangkat", tanggal = "08:45"),
            InfoItem(judul = "Kepala Dusun (Joko Susilo)", deskripsi = "Membina ketentraman dan ketertiban serta koordinasi pembangunan di tingkat dusun.", avatar = "https://avatar.iran.liara.run/public/35", imageUrl = "https://picsum.photos/id/1060/800/400", kategori = "Perangkat", tanggal = "09:00"),
            // Lembaga
            InfoItem(judul = "BPD (Badan Permusyawaratan)", deskripsi = "Merumuskan peraturan desa, menampung aspirasi masyarakat, dan mengawasi kinerja kades.", avatar = "https://avatar.iran.liara.run/public/22", imageUrl = "https://picsum.photos/id/200/800/400", kategori = "Lembaga", tanggal = "BPD"),
            InfoItem(judul = "LPM (Lembaga Pemberdayaan)", deskripsi = "Merencanakan dan menggerakkan gotong royong serta partisipasi pembangunan masyarakat.", avatar = "https://avatar.iran.liara.run/public/23", imageUrl = "https://picsum.photos/id/210/800/400", kategori = "Lembaga", tanggal = "LPM"),
            InfoItem(judul = "PKK (Kesejahteraan Keluarga)", deskripsi = "Meningkatkan kesejahteraan keluarga melalui 10 program pokok PKK dan pembinaan UMKM.", avatar = "https://avatar.iran.liara.run/public/88", imageUrl = "https://picsum.photos/id/220/800/400", kategori = "Lembaga", tanggal = "PKK"),
            InfoItem(judul = "Karang Taruna", deskripsi = "Membina kreativitas kepemudaan, olahraga, seni budaya, dan penanggulangan masalah sosial.", avatar = "https://avatar.iran.liara.run/public/8", imageUrl = "https://picsum.photos/id/230/800/400", kategori = "Lembaga", tanggal = "Pemuda"),
            // Pengumuman
            InfoItem(judul = "Pembayaran PBB Buka s/d 31 Juli 2026", deskripsi = "Warga diimbau segera melunasi Pajak Bumi dan Bangunan melalui kantor desa. Jangan lewatkan batas waktu agar tidak dikenakan denda.", avatar = "https://avatar.iran.liara.run/public/5", imageUrl = "https://picsum.photos/id/300/800/400", kategori = "Pengumuman", tanggal = "30 Jun 2026"),
            InfoItem(judul = "Jadwal Posyandu Balita Bulan Juli 2026", deskripsi = "Posyandu balita akan dilaksanakan tanggal 5 Juli 2026 pukul 08.00 di Posyandu Desa. Harap bawa buku KIA.", avatar = "https://avatar.iran.liara.run/public/90", imageUrl = "https://picsum.photos/id/305/800/400", kategori = "Pengumuman", tanggal = "28 Jun 2026")
        )
        items.forEach { item ->
            val cv = ContentValues().apply {
                put(COL_INFO_JUDUL, item.judul)
                put(COL_INFO_DESKRIPSI, item.deskripsi)
                put(COL_INFO_AVATAR, item.avatar)
                put(COL_INFO_IMAGE, item.imageUrl)
                put(COL_INFO_KATEGORI, item.kategori)
                put(COL_INFO_TANGGAL, item.tanggal)
            }
            db.insert(TABLE_INFO, null, cv)
        }
    }
}

data class AgendaItem(
    val id: Int = 0,
    val kegiatan: String,
    val tanggal: String,
    val waktu: String,
    val lokasi: String,
    val status: String = "Akan Datang"
)

data class CatatanItem(
    val id: Int = 0,
    val judul: String,
    val isi: String,
    val tanggal: String
)

data class InfoItem(
    val id: Int = 0,
    val judul: String,
    val deskripsi: String,
    val avatar: String = "",
    val imageUrl: String = "",
    val kategori: String = "Perangkat",
    val tanggal: String
)
