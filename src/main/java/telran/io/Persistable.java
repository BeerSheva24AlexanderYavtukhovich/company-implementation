package telran.io;

public interface Persistable {
    void saveTofile(String fileName);

    void restoreFromFile(String fileName);
}
