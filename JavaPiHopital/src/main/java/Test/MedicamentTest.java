package Test;

import Model.Categorie;
import Model.Medicament;
import Service.MedicamentService;


import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class MedicamentTest {
    public static void main(String[] args) {
        LocalDate currentDate = LocalDate.of(2026 , Calendar.JUNE, 10);
        LocalDate currentDate1 = LocalDate.of(2030 , 12, 10);
        Categorie c =new Categorie(27,"cat","cat","cat");
        Medicament m=new Medicament(12,c,"didi","dp",currentDate,currentDate1,45,"lolo","stock","image");
        MedicamentService ms=new MedicamentService();
        //ms.addMedicament(m);
        //ms.updateMedicament(m);
        //ms.deleteMedicament(m);
        System.out.println(ms.getData());
    }
}
