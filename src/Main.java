import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static String makeDir(String name) {
        var dir = new File(name);
        if (dir.mkdir()) {
            return "Директория " + name + " создана\n";
        }
        return "Директория " + name + " не создана\n";
    }

    public static String makeFile(String name) {
        var file = new File(name);
        try {
            if (file.createNewFile()) {
                return "Файл " + name + " создан\n";
            }
        } catch (IOException excp) {
            System.out.println(excp.getMessage());
        }
        return "Файл " + name + " не создан\n";
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
                ZipEntry entry = new ZipEntry(name);

                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);

                zout.write(buffer);
                zout.closeEntry();
                fis.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void openZip(String zipName, String directoryName) {
        try(ZipInputStream zin = new ZipInputStream(new FileInputStream(zipName))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
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

//        StringBuilder log = new StringBuilder();
//
//        Scanner scanner = new Scanner(System.in);
//        for (int i = 0; i < 4; i++) {
//            log.append(makeDir(scanner.nextLine()));
//        }
//
//        for (int i = 0; i < 2; i++) {
//            log.append(makeDir(scanner.nextLine()));
//        }
//
//        for (int i = 0; i < 2; i++) {
//            log.append(makeFile(scanner.nextLine()));
//        }
//
//        for (int i = 0; i < 3; i++) {
//            log.append(makeDir(scanner.nextLine()));
//        }
//
//        String fileName = scanner.nextLine();
//        log.append(makeFile(fileName));
//
//        try (FileWriter writer = new FileWriter(fileName, false);) {
//            writer.write(new String(log));
//            writer.flush();
//        } catch (IOException excp) {
//            System.out.println(excp.getMessage());
//        }

        GameProgress startProgress =  new GameProgress(100, 3, 10, 3.5);
        GameProgress midProgress =  new GameProgress(50, 5, 7, 5.5);
        GameProgress finishProgress =  new GameProgress(150, 2, 15, 7.5);

        saveGame("D:/netology/Core3/Games/savegames/start.dat", startProgress);
        saveGame("D:/netology/Core3/Games/savegames/mid.dat", midProgress);
        saveGame("D:/netology/Core3/Games/savegames/finish.dat", finishProgress);

        List<String> names = new ArrayList<String>();
        names.add("D:/netology/Core3/Games/savegames/start.dat");
        names.add("D:/netology/Core3/Games/savegames/mid.dat");
        names.add("D:/netology/Core3/Games/savegames/finish.dat");

        zipFiles("D:/netology/Core3/Games/savegames/gameZip.zip", names);

        for (String name : names) {
            File file = new File(name);
            file.delete();
        }

        openZip("D:/netology/Core3/Games/savegames/gameZip.zip", "D:/netology/Core3/Games/savegames");
        GameProgress gameProgress = openProgress("D:/netology/Core3/Games/savegames/start.dat");
        System.out.println(gameProgress);
    }
}

//Games/src
//        Games/res
//        Games/savegames
//        Games/temp
//        Games/src/main
//        Games/src/test
//        Games/src/main/Main.java
//        Games/src/main/Utils.java
//        Games/res/drawables
//        Games/res/vectors
//        Games/res/icons
