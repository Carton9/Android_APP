// LocationInterface.aidl
package com.android.carton9.pickup;

// Declare any non-default types here with import statements

interface LocationInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    //void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,double aDouble, String aString);
    void sendInfo(double _ln,double _la,String _phoneNumber,String _text);
    void action();
}
