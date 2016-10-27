// IBinderPool.aidl
package com.np.ipcbinderdemo;

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
