package com.np.ipcbinderdemo;

import android.os.RemoteException;

/**
 * <B>Des</B>
 *
 * @author ningpan 2016/10/25 8:45
 */
public class ComputerImpl extends IComputer.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
