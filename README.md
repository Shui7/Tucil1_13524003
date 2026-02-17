# Penyelesaian Permainan Queens LinkedIn
Tugas Kecil 1 IF2211 Strategi Algoritma Semester II 2025/2026

## Deskripsi
Program ini dapat menyelesaikan persoalan gim Queens dari LinkedIn menggunakan algoritma brute force dengan memeriksa seluruh kemungkinan posisi setiap queen pada papan permainan dan mengecek kevalidannya sesuai aturan gim.

## Cara Menjalankan
- Install JDK dari Oracle (https://www.oracle.com/asean/java/technologies/downloads/) atau OpenJDK (https://jdk.java.net/25/).
Cek keberadaan JDK dengan `java -version`.
- Install Maven dari https://maven.apache.org/. Cek keberadaan dengan `mvn -version`.
- Build program pada root (`.../Tucil1_13524003`) menggunakan
```bash
mvn clean install
```
- Jalankan program pada root (`.../Tucil1_13524003`) menggunakan
```bash
mvn javafx:run
```

## Cara Menggunakan Program
### Input
Terdapat tiga cara untuk memasukkan konfigurasi papan gim.

- Masukkan banyak queen atau ukuran papan (n x n) pada bidang teks "By number of queens:" dan tekan tombol "Enter". Lalu, klik sel pada bagian tengah program untuk mengganti warnanya sesuai keinginan.
- Masukkan konfigurasi papan pada bidang teks "By text:" dengan format setiap karakter adalah alfabet kapital berbentuk persegi dan tekan tombol "Enter". Contoh konfigurasi papan:
```
CABB
CABB
CAAA
AADD
```
- Masukkan file .txt dengan menekan tombol "Insert .txt file" yang berisi konfigurasi papan dengan format seperti pada cara sebelumnya dan tekan tombol "Enter".

### Kalkulasi
Mulai pencarian solusi dengan menekan tombol "Calculate". Pastikan banyak warna berbeda yang ada pada papan sama dengan banyak baris/kolom papan. Beberapa modifikasi terkait cara pencarian dapat dilakukan.

- Checkbox "Use backtracking" membuat pencarian menggunakan algoritma backtracking alih-alih algoritma brute force sehingga pencarian lebih cepat terselesaikan.
- Checkbox "Show step every *n* iteration for *m* ms" membuat pencarian sekalian menampilkan bagaimana konfigurasi posisi queen pada papan setiap *n* iterasi dengan tiap penampilan ditampilkan selama *m* millidetik.
### Output
Setelah pencarian solusi selesai, solusi akan ditampilkan di bidang teks tepat di bawah kata "Solution" serta konfigurasi papan beserta posisi queen terlihat pada bagian tengah program. Teks solusi menampilkan konfigurasi papan dengan posisi queen dalam format teks, waktu pencarian, dan banyak iterasi/kasus yang dilewati. Contoh:
```
C#BB
CAB#
#AAA
AA#D

Waktu pencarian: 23.501316 ms
Banyak kasus yang ditinjau: 741 kasus
```
Terdapat dua tombol untuk menyimpan solusi yang didapat.
- Tombol "Save as .txt" menyimpan text solusi ke dalam file .txt di tempat dan nama sesuai pilihan.
- Tombol "Save as .png" menyimpan grafik konfigurasi papan beserta posisi queen ke dalam file .png di tempat dan nama sesuai pilihan.