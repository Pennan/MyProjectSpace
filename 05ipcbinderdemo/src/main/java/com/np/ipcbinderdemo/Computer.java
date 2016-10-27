package com.np.ipcbinderdemo;

import android.os.RemoteException;

/**
 * <B>Des</B>
 *
 * @author ningpan 2016/10/12 17:08
 */
public class Computer extends IComputer.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
