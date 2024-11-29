package telran.io;

public interface Persistable {
    void saveToFile(String fileName);

    void restoreFromFile(String fileName);
}