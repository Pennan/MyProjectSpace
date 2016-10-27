// INewBookArrivedListener.aidl
package com.np.aidldemo2;

import com.np.aidldemo2.Book;
interface INewBookArrivedListener {
    void onNewBookArrived(in Book book);
}
