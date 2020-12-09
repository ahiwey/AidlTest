package com.ahiwey.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * 项目: AidlTest-master
 * 创建者: ahiwey
 * 创建时间: 2020/12/7 23:23
 * 来自: null
 */
public class BookManagerService extends Service {

    private List<Book> books = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    class MyBinder extends BookManagerImpl {

        @Override
        public List<Book> getBookList() throws RemoteException {
            books.add(new Book(1, "设计模式"));
            books.add(new Book(2, "数据结构"));
            books.add(new Book(3, "算法"));
            Log.d("AidlTest", "getBookList->books:" + books);
            return books;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Log.d("AidlTest", "addBook->book:" + book);
            if (book != null) {
                books.add(book);
            }
            Log.d("AidlTest", "addBook->books:" + books);
        }
    }
}
