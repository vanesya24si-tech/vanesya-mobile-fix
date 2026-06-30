# 📱 Bina Desa Perangkat Lembaga

> Aplikasi mobile berbasis Android yang dirancang untuk membantu pemerintah desa dalam mengelola informasi perangkat desa, struktur kelembagaan, berita desa, agenda kegiatan, serta meningkatkan komunikasi antara pemerintah desa dan masyarakat.

---

# 📌 Nama Project

**Bina Desa Perangkat Lembaga**

---

# 📖 Deskripsi Project

Bina Desa Perangkat Lembaga merupakan aplikasi Android yang berfungsi sebagai pusat informasi mengenai pemerintahan desa. Aplikasi ini memudahkan masyarakat untuk mengenal struktur organisasi desa, tugas setiap perangkat desa, kelembagaan desa, berita terbaru, agenda kegiatan, serta memperoleh informasi resmi dari pemerintah desa.

Selain sebagai media informasi, aplikasi juga menyediakan reminder agenda desa, penyimpanan data offline, dan berita nasional maupun daerah melalui REST API.

---

# 🎯 Tujuan Aplikasi

- Memperkenalkan struktur pemerintahan desa.
- Menyediakan informasi perangkat dan lembaga desa.
- Memudahkan masyarakat memperoleh berita terbaru.
- Menyediakan agenda kegiatan desa.
- Menampilkan informasi secara modern dan mudah dipahami.
- Mendukung digitalisasi pemerintahan desa.

---

# 👥 Target Pengguna

- Masyarakat Desa
- Kepala Desa
- Sekretaris Desa
- Perangkat Desa
- Karang Taruna
- BPD
- PKK
- Mahasiswa KKN

---

# 🛠 Teknologi

- Kotlin
- Android Studio
- ViewBinding
- RecyclerView
- TabLayout
- ViewPager2
- Retrofit
- Room Database
- Material Design 3
- Notification Manager
- AlarmManager / WorkManager

---

# 📋 Implementasi Materi Praktikum

---

# 1. ListView + Custom Adapter

## Fitur

### Daftar Perangkat Desa

Menampilkan seluruh perangkat desa menggunakan ListView dengan Custom Adapter.

### Data Ditampilkan

- Foto Perangkat
- Nama
- Jabatan
- Nomor Telepon
- Bidang

### Contoh Data

- Kepala Desa
- Sekretaris Desa
- Kasi Pemerintahan
- Kasi Pelayanan
- Kasi Kesejahteraan
- Kaur Keuangan
- Kaur Umum
- Kepala Dusun

### Isi Card

- Foto
- Nama
- Jabatan
- Tombol Detail

### Tujuan

Mengimplementasikan ListView dan Custom Adapter dengan tampilan profesional.

---

# 2. TabLayout + RecyclerView

Membagi informasi menjadi beberapa kategori.

---

## Tab 1

### Perangkat Desa

RecyclerView menampilkan:

- Kepala Desa
- Sekretaris
- Kaur
- Kasi
- Kepala Dusun

---

## Tab 2

### Lembaga Desa

RecyclerView menampilkan:

- BPD
- LPM
- PKK
- Karang Taruna
- RT/RW
- Posyandu

---

## Tab 3

### Agenda Desa

RecyclerView menampilkan:

- Musyawarah Desa
- Gotong Royong
- Posyandu
- Pelatihan UMKM
- Sosialisasi

---

## Tab 4

### Pengumuman

RecyclerView menampilkan:

- Informasi Desa
- Jadwal Pelayanan
- Bantuan Sosial
- Pengumuman Penting

---

# Tujuan

Menggunakan TabLayout dan RecyclerView dalam satu halaman agar informasi lebih rapi.

---

# 3. Onboarding Screen (ViewPager2)

Tampil ketika aplikasi pertama kali dibuka.

---

## Halaman 1

### Judul

**Selamat Datang**

Deskripsi

Selamat datang di aplikasi digital informasi Perangkat dan Lembaga Desa.

---

## Halaman 2

### Judul

**Kenali Pemerintah Desa**

Deskripsi

Lihat struktur organisasi dan tugas setiap perangkat desa.

---

## Halaman 3

### Judul

**Akses Informasi Desa**

Deskripsi

Temukan berita, agenda kegiatan, dan pengumuman resmi kapan saja.

---

## Komponen

- ViewPager2
- Dot Indicator
- Skip
- Next
- Mulai Sekarang

---

# 4. REST API

## Fitur

### Berita Nasional / Pemerintahan

Menggunakan Retrofit untuk mengambil berita terbaru.

Contoh API:

- NewsAPI
- GNews
- Mediastack

---

## Data Ditampilkan

- Judul Berita
- Thumbnail
- Penulis
- Tanggal
- Deskripsi
- Link Berita

---

## Kategori

- Pemerintahan
- Desa
- UMKM
- Pertanian
- Pendidikan
- Kesehatan

---

## Implementasi

- Retrofit
- RecyclerView
- ProgressBar
- Swipe Refresh
- Error Handling

---

# 5. Room Database

Digunakan agar aplikasi tetap dapat digunakan tanpa internet.

---

## Tabel Favorite News

Field

- id
- title
- image
- author
- date
- description

Fungsi

Menyimpan berita favorit.

---

## Tabel Agenda Desa

Field

- id
- kegiatan
- lokasi
- tanggal
- waktu
- status

Fungsi

Menyimpan agenda kegiatan desa.

---

## Tabel Catatan Warga

Field

- id
- judul
- isi
- tanggal

Fungsi

Catatan pribadi masyarakat mengenai kegiatan desa.

---

# 6. Notification & Reminder

Menggunakan AlarmManager / WorkManager.

---

## Reminder Agenda

Contoh

> Hari ini terdapat Musyawarah Desa pukul 08.00 WIB.

---

## Reminder Posyandu

Contoh

> Besok terdapat kegiatan Posyandu di Balai Desa.

---

## Reminder Gotong Royong

Contoh

> Jangan lupa mengikuti Gotong Royong Desa hari Minggu.

---

## Reminder Berita Baru

Contoh

> Terdapat berita terbaru mengenai pemerintahan desa.

---

# 📱 Struktur Menu

## Home

- Banner
- Ringkasan Desa
- Quick Menu

---

## Perangkat Desa

- Kepala Desa
- Sekretaris
- Kasi
- Kaur
- Kepala Dusun

---

## Lembaga Desa

- PKK
- BPD
- Karang Taruna
- Posyandu
- RT/RW

---

## Berita

Berita terbaru dari REST API.

---

## Agenda

Agenda kegiatan desa.

---

## Favorit

Berita favorit dari Room Database.

---

## Profil

Tentang aplikasi.

---

# 🗂 Implementasi Materi

| No | Materi | Implementasi |
|----|---------|--------------|
| 1 | ListView + Custom Adapter | Daftar Perangkat Desa |
| 2 | TabLayout + RecyclerView | Perangkat, Lembaga, Agenda, Pengumuman |
| 3 | ViewPager2 | Onboarding Screen |
| 4 | REST API | Berita Desa & Pemerintahan |
| 5 | Room Database | Favorite News, Agenda, Catatan |
| 6 | Notification | Agenda Desa & Pengumuman |

---

BUATKAN DRAWNBALE PENDUKUNG AGAR TAMPILAN BAGUS,DAN BUATKAN SESUAI DENGAN TEMA PROJECT SAYA SEKARANG