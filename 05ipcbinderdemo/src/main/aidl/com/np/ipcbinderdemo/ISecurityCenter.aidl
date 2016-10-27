// ISecurityCenter.aidl
package com.np.ipcbinderdemo;

interface ISecurityCenter {
    /** 加密 */
    String encrypt(String content);
    /** 解密 */
    String decrypt(String password);
}
