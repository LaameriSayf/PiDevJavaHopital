package Service;

import java.util.List;

public interface IMedicament <T> {
    public void addMedicament(T t);
    public void deleteMedicament(T t);
    public void updateMedicament(T t);
    public List<T> getData();
}
