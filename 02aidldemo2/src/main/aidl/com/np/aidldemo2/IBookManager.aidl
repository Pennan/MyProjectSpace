// IBookManager.aidl
package com.np.aidldemo2;

import com.np.aidldemo2.Book;
import com.np.aidldemo2.INewBookArrivedListener;
interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(INewBookArrivedListener listener);
    void unregisterListener(INewBookArrivedListener listener);
}
