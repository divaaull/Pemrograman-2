package nilaimhs;
import java.util.Scanner;

public class NilaiMhs {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String nim, nama, grade;
        double uts, uas, rata;

        System.out.println("Data Mahasiswa: ");

        System.out.print("NIM: ");
        nim = input.nextLine(); // gunakan nextLine() agar bisa membaca seluruh input

        System.out.print("Nama: ");
        nama = input.nextLine(); // gunakan nextLine() agar bisa membaca nama lengkap dengan spasi

        System.out.print("Nilai UTS: ");
        while (!input.hasNextDouble()) { // validasi input
            System.out.println("Masukkan angka!");
            input.next(); // buang input yang salah
        }
        uts = input.nextDouble();

        System.out.print("Nilai UAS: ");
        while (!input.hasNextDouble()) {
            System.out.println("Masukkan angka!");
            input.next();
        }
        uas = input.nextDouble();

        rata = (uts + uas) / 2;

        if (rata < 50)
            grade = "E";
        else if (rata < 60)
            grade = "D";
        else if (rata < 70)
            grade = "C";
        else if (rata < 80)
            grade = "B";
        else
            grade = "A";

        System.out.println("================================================");
        System.out.println("NIM\tNama\tUTS\tUAS\tRata2\tGrade");
        System.out.println("================================================");
        System.out.println(nim + "\t" + nama + "\t" + uts + "\t" + uas + "\t" + rata + "\t" + grade);
    }
}