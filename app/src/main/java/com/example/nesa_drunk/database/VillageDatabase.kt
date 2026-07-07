package com.example.nesa_drunk.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VillageDatabase(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        const val DATABASE_NAME = "village_db"
        const val DATABASE_VERSION = 3 // Versi naik agar onCreate dipanggil lagi (atau hapus data app)

        const val TABLE_AGENDA = "agenda_desa"
        const val COL_AGENDA_ID = "id"
        const val COL_AGENDA_KEGIATAN = "kegiatan"
        const val COL_AGENDA_TANGGAL = "tanggal"
        const val COL_AGENDA_WAKTU = "waktu"
        const val COL_AGENDA_LOKASI = "lokasi"
        const val COL_AGENDA_STATUS = "status"

        const val TABLE_CATATAN = "catatan_warga"
        const val COL_CATATAN_ID = "id"
        const val COL_CATATAN_JUDUL = "judul"
        const val COL_CATATAN_ISI = "isi"
        const val COL_CATATAN_TANGGAL = "tanggal"

        const val TABLE_FAVORITE = "favorite_news"
        const val COL_FAV_ID = "id"
        const val COL_FAV_TITLE = "title"
        const val COL_FAV_IMAGE = "image"
        const val COL_FAV_AUTHOR = "author"
        const val COL_FAV_DATE = "date"
        const val COL_FAV_DESC = "description"
        const val COL_FAV_LINK = "link"

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
        db.execSQL("CREATE TABLE $TABLE_AGENDA ($COL_AGENDA_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_AGENDA_KEGIATAN TEXT NOT NULL, $COL_AGENDA_TANGGAL TEXT NOT NULL, $COL_AGENDA_WAKTU TEXT NOT NULL, $COL_AGENDA_LOKASI TEXT NOT NULL, $COL_AGENDA_STATUS TEXT NOT NULL DEFAULT 'Akan Datang')")
        db.execSQL("CREATE TABLE $TABLE_CATATAN ($COL_CATATAN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_CATATAN_JUDUL TEXT NOT NULL, $COL_CATATAN_ISI TEXT NOT NULL, $COL_CATATAN_TANGGAL TEXT NOT NULL)")
        db.execSQL("CREATE TABLE $TABLE_FAVORITE ($COL_FAV_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_FAV_TITLE TEXT NOT NULL, $COL_FAV_IMAGE TEXT, $COL_FAV_AUTHOR TEXT, $COL_FAV_DATE TEXT, $COL_FAV_DESC TEXT, $COL_FAV_LINK TEXT)")
        db.execSQL("CREATE TABLE $TABLE_INFO ($COL_INFO_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_INFO_JUDUL TEXT NOT NULL, $COL_INFO_DESKRIPSI TEXT NOT NULL, $COL_INFO_AVATAR TEXT, $COL_INFO_IMAGE TEXT, $COL_INFO_KATEGORI TEXT NOT NULL DEFAULT 'Perangkat', $COL_INFO_TANGGAL TEXT NOT NULL)")
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
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val agendaList = listOf(
            Triple("Rapat RT Rutin", today, "19:00"),
            Triple("Posyandu Balita", today, "08:00"),
            Triple("Gotong Royong", "2024-12-30", "07:00")
        )
        agendaList.forEach { (kegiatan, tanggal, waktu) ->
            val cv = ContentValues().apply {
                put(COL_AGENDA_KEGIATAN, kegiatan)
                put(COL_AGENDA_TANGGAL, tanggal)
                put(COL_AGENDA_WAKTU, waktu)
                put(COL_AGENDA_LOKASI, "Balai Desa")
            }
            db.insert(TABLE_AGENDA, null, cv)
        }
    }

    fun getAllAgenda(): List<AgendaItem> {
        val list = mutableListOf<AgendaItem>()
        val cursor = readableDatabase.query(TABLE_AGENDA, null, null, null, null, null, "$COL_AGENDA_TANGGAL ASC")
        cursor.use {
            while (it.moveToNext()) {
                list.add(AgendaItem(
                    it.getInt(it.getColumnIndexOrThrow(COL_AGENDA_ID)),
                    it.getString(it.getColumnIndexOrThrow(COL_AGENDA_KEGIATAN)),
                    it.getString(it.getColumnIndexOrThrow(COL_AGENDA_TANGGAL)),
                    it.getString(it.getColumnIndexOrThrow(COL_AGENDA_WAKTU)),
                    it.getString(it.getColumnIndexOrThrow(COL_AGENDA_LOKASI)),
                    it.getString(it.getColumnIndexOrThrow(COL_AGENDA_STATUS))
                ))
            }
        }
        return list
    }

    fun insertAgenda(agenda: AgendaItem): Long {
        val cv = ContentValues().apply {
            put(COL_AGENDA_KEGIATAN, agenda.kegiatan)
            put(COL_AGENDA_TANGGAL, agenda.tanggal)
            put(COL_AGENDA_WAKTU, agenda.waktu)
            put(COL_AGENDA_LOKASI, agenda.lokasi)
        }
        return writableDatabase.insert(TABLE_AGENDA, null, cv)
    }

    fun deleteAgenda(id: Int) = writableDatabase.delete(TABLE_AGENDA, "$COL_AGENDA_ID=?", arrayOf(id.toString()))

    fun getAllCatatan(): List<CatatanItem> {
        val list = mutableListOf<CatatanItem>()
        val cursor = readableDatabase.query(TABLE_CATATAN, null, null, null, null, null, "$COL_CATATAN_ID DESC")
        cursor.use {
            while (it.moveToNext()) {
                list.add(CatatanItem(
                    it.getInt(it.getColumnIndexOrThrow(COL_CATATAN_ID)),
                    it.getString(it.getColumnIndexOrThrow(COL_CATATAN_JUDUL)),
                    it.getString(it.getColumnIndexOrThrow(COL_CATATAN_ISI)),
                    it.getString(it.getColumnIndexOrThrow(COL_CATATAN_TANGGAL))
                ))
            }
        }
        return list
    }

    fun insertCatatan(catatan: CatatanItem) = writableDatabase.insert(TABLE_CATATAN, null, ContentValues().apply {
        put(COL_CATATAN_JUDUL, catatan.judul)
        put(COL_CATATAN_ISI, catatan.isi)
        put(COL_CATATAN_TANGGAL, catatan.tanggal)
    })

    fun deleteCatatan(id: Int) = writableDatabase.delete(TABLE_CATATAN, "$COL_CATATAN_ID=?", arrayOf(id.toString()))

    private fun seedInfoItems(db: SQLiteDatabase) {
        val list = listOf(
            InfoItem(judul = "Kepala Desa", deskripsi = "Bapak Ahmad S.T.", avatar = "", imageUrl = "", kategori = "Perangkat", tanggal = "2024-01-01"),
            InfoItem(judul = "Sekretaris Desa", deskripsi = "Ibu Siti Aminah", avatar = "", imageUrl = "", kategori = "Perangkat", tanggal = "2024-01-01")
        )
        list.forEach { item ->
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

    fun getAllInfoByKategori(k: String): List<InfoItem> {
        val list = mutableListOf<InfoItem>()
        val cursor = readableDatabase.query(TABLE_INFO, null, "$COL_INFO_KATEGORI=?", arrayOf(k), null, null, "$COL_INFO_ID DESC")
        cursor.use {
            while (it.moveToNext()) {
                list.add(InfoItem(
                    it.getInt(it.getColumnIndexOrThrow(COL_INFO_ID)),
                    it.getString(it.getColumnIndexOrThrow(COL_INFO_JUDUL)),
                    it.getString(it.getColumnIndexOrThrow(COL_INFO_DESKRIPSI)),
                    it.getString(it.getColumnIndexOrThrow(COL_INFO_AVATAR)),
                    it.getString(it.getColumnIndexOrThrow(COL_INFO_IMAGE)),
                    it.getString(it.getColumnIndexOrThrow(COL_INFO_KATEGORI)),
                    it.getString(it.getColumnIndexOrThrow(COL_INFO_TANGGAL))
                ))
            }
        }
        return list
    }

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
        return writableDatabase.update(TABLE_INFO, cv, "$COL_INFO_ID=?", arrayOf(item.id.toString()))
    }

    fun deleteInfoItem(id: Int) = writableDatabase.delete(TABLE_INFO, "$COL_INFO_ID=?", arrayOf(id.toString()))

    fun getAllFavoriteNews(): List<FavoriteNewsItem> {
        val list = mutableListOf<FavoriteNewsItem>()
        val cursor = readableDatabase.query(TABLE_FAVORITE, null, null, null, null, null, "$COL_FAV_ID DESC")
        cursor.use {
            while (it.moveToNext()) {
                list.add(FavoriteNewsItem(
                    it.getInt(it.getColumnIndexOrThrow(COL_FAV_ID)),
                    it.getString(it.getColumnIndexOrThrow(COL_FAV_TITLE)),
                    it.getString(it.getColumnIndexOrThrow(COL_FAV_IMAGE)),
                    it.getString(it.getColumnIndexOrThrow(COL_FAV_AUTHOR)),
                    it.getString(it.getColumnIndexOrThrow(COL_FAV_DATE)),
                    it.getString(it.getColumnIndexOrThrow(COL_FAV_DESC)),
                    it.getString(it.getColumnIndexOrThrow(COL_FAV_LINK))
                ))
            }
        }
        return list
    }

    fun insertFavoriteNews(item: FavoriteNewsItem): Long {
        val cv = ContentValues().apply {
            put(COL_FAV_TITLE, item.title)
            put(COL_FAV_IMAGE, item.image)
            put(COL_FAV_AUTHOR, item.author)
            put(COL_FAV_DATE, item.date)
            put(COL_FAV_DESC, item.description)
            put(COL_FAV_LINK, item.link)
        }
        return writableDatabase.insert(TABLE_FAVORITE, null, cv)
    }

    fun deleteFavoriteNewsByTitle(title: String) = writableDatabase.delete(TABLE_FAVORITE, "$COL_FAV_TITLE=?", arrayOf(title))
}
