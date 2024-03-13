import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void makeDir(String name, StringBuilder log) {
        var dir = new File(name);
        if (dir.mkdir()) {
            log.append("Директория " + name + " создана\n");
        }
        log.append("Директория " + name + " не создана\n");
    }

    public static void makeFile(String name, StringBuilder log) {
        var file = new File(name);
        try {
            if (file.createNewFile()) {
                log.append("Файл " + name + " создан\n");
            }
        } catch (IOException excp) {
            System.out.println(excp.getMessage());
        }
        log.append("Файл " + name + " не создан\n");
    }

    public static void saveGame(String pathname, GameProgress progress) {

        try (FileOutputStream fos = new FileOutputStream(pathname);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(progress);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String pathame, List<String> names) {

        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathame))) {
            for (String name : names) {
                FileInputStream fis = new FileInputStream(name);
                ZipEntry entry = new ZipEntry(getFileNameFromPathname(name));

                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);

                zout.write(buffer);
                zout.closeEntry();
                fis.close();

                File file = new File(name);
                if (file.delete()) {
                    System.out.println(file.toString() + " удален!");
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static String getFileNameFromPathname(String pathname) {
        String directory = "D:/netology/Core3/Games/savegames/";
        return pathname.substring(directory.length());
    }

    public static void openZip(String zipName, String directoryName) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipName))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = directoryName + "/" + entry.getName();
                FileOutputStream fout = new FileOutputStream(name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static GameProgress openProgress(String pathname) {
        GameProgress progress = null;
        try (FileInputStream fis = new FileInputStream(pathname);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            progress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return progress;
    }

    public static void main(String[] args) {

        StringBuilder log = new StringBuilder();

        makeDir("Games/src", log);
        makeDir("Games/res", log);
        makeDir("Games/savegames", log);
        makeDir("Games/temp", log);


        makeDir("Games/src/main", log);
        makeDir("Games/src/test", log);


        makeFile("Games/src/main/Main.java", log);
        makeFile("Games/src/main/Utils.java", log);


        makeDir("Games/res/drawables", log);
        makeDir("Games/res/vectors", log);
        makeDir("Games/res/icons", log);

        makeFile("Games/temp/temp.txt", log);

        try (FileWriter writer = new FileWriter("Games/temp/temp.txt", false)) {
            writer.write(new String(log));
            writer.flush();
        } catch (IOException excp) {
            System.out.println(excp.getMessage());
        }

        GameProgress startProgress = new GameProgress(100, 3, 10, 3.5);
        GameProgress midProgress = new GameProgress(50, 5, 7, 5.5);
        GameProgress finishProgress = new GameProgress(150, 2, 15, 7.5);

        saveGame("D:/netology/Core3/Games/savegames/start.dat", startProgress);
        saveGame("D:/netology/Core3/Games/savegames/mid.dat", midProgress);
        saveGame("D:/netology/Core3/Games/savegames/finish.dat", finishProgress);

        List<String> names = new ArrayList<String>();
        names.add("D:/netology/Core3/Games/savegames/start.dat");
        names.add("D:/netology/Core3/Games/savegames/mid.dat");
        names.add("D:/netology/Core3/Games/savegames/finish.dat");

        zipFiles("D:/netology/Core3/Games/savegames/gameZip.zip", names);

        openZip("D:/netology/Core3/Games/savegames/gameZip.zip", "D:/netology/Core3/Games/savegames");
        GameProgress gameProgress = openProgress("D:/netology/Core3/Games/savegames/start.dat");
        System.out.println(gameProgress);
    }
}
