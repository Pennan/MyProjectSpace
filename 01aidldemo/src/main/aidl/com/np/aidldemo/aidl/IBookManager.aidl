// IBookManager.aidl
package com.np.aidldemo.aidl;

import com.np.aidldemo.aidl.Book;
interface IBookManager {
    List<Book> getBooks();
    void addBook(in Book book);
}
