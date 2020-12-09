package com.ahiwey.aidltest;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * 说明:
 * 项目: AidlTest-master
 * 创建者: ahiwey
 * 创建时间: 2020/12/7 22:28
 * 来自: null
 */
public abstract class BookManagerImpl extends Binder implements IBookManager {

    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (iin instanceof IBookManager) {
            return (IBookManager) iin;
        }
        return new Proxy(obj);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply,
                                 int flags) throws RemoteException {
        if (reply == null) {
            return true;
        }
        switch (code) {
            case IBinder.INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;
            case TRANSACTION_getBookList:
                //验证描述符
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = getBookList();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            case TRANSACTION_addBook:
                //验证描述符
                data.enforceInterface(DESCRIPTOR);
                Book _arg0;
                if ((0 != data.readInt())) {
                    _arg0 = Book.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                this.addBook(_arg0);
                reply.writeNoException();
                return true;
            default:
                break;
        }
        return super.onTransact(code, data, reply, flags);
    }


    @Nullable
    @Override
    public String getInterfaceDescriptor() {
        return DESCRIPTOR;
    }

    private static class Proxy implements IBookManager {

        private IBinder mRemote;

        public Proxy(IBinder remote) {
            mRemote = remote;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> result;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(TRANSACTION_getBookList, data, reply, 0);
                reply.readException();
                result = reply.createTypedArrayList(Book.CREATOR);
            } finally {
                reply.recycle();
                data.recycle();
            }
            return result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();
            try {
                _data.writeInterfaceToken(DESCRIPTOR);
                if ((book != null)) {
                    _data.writeInt(1);
                    book.writeToParcel(_data, 0);
                } else {
                    _data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addBook, _data, _reply, 0);
                _reply.readException();
            } finally {
                _reply.recycle();
                _data.recycle();
            }
        }


        @Override
        public IBinder asBinder() {
            return mRemote;
        }


    }
}
