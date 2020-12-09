package com.ahiwey.aidltest;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

/**
 * 说明:
 * 项目: AidlTest-master
 * 创建者: ahiwey
 * 创建时间: 2020/12/7 22:21
 * 来自: null
 */
public interface IBookManager extends IInterface {

    public static final String DESCRIPTOR = "com.ahiwey.aidltest.IBookManager";
    public static final int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION;
    public static final int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;

    public List<Book> getBookList() throws RemoteException;

    public void addBook(Book book) throws RemoteException;

}
