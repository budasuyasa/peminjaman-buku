package budasuyasa.android.simplecrud.Models;

public class Peminjaman {

    String id;
    String bookId;
    String title;
    String nama;
    String lamaPinjam;
    String tanggalPinjam;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLamaPinjam() {
        return lamaPinjam;
    }

    public void setLamaPinjam(String lamaPinjam) {
        this.lamaPinjam = lamaPinjam;
    }

    public String getTanggalPinjam() {
        return tanggalPinjam;
    }

    public void setTanggalPinjam(String tanggalPinjam) {
        this.tanggalPinjam = tanggalPinjam;
    }



}
