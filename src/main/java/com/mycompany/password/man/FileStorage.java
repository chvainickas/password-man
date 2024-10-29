/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.password.man;
import java.io.*;
import java.util.HashMap;
/**
 *
 * @author pylyp
 */


public class FileStorage {

    private final String filename;

    public FileStorage(String filename) {
        this.filename = filename;
    }

    // Метод для сохранения данных в файл
    public void save(HashMap<String, String> data) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        }
    }

    // Метод для загрузки данных из файла
    public HashMap<String, String> load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (HashMap<String, String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>(); // Возвращаем пустую карту, если файл отсутствует
        }
    }
}

