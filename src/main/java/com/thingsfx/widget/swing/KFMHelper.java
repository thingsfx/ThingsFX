package com.thingsfx.widget.swing;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import sun.awt.CausedFocusEvent;

class KFMHelper {

    static final int SNFH_FAILURE = 0;
    static final int SNFH_SUCCESS_HANDLED = 1;
    static final int SNFH_SUCCESS_PROCEED = 2;

    private static Method processSynchronousLightweightTransferMethod;

    private static Method shouldNativelyFocusHeavyweightMethod;

    static boolean processSynchronousLightweightTransfer(Component target,
                                                         Component lightweightChild,
                                                         boolean temporary,
                                                         boolean focusedWindowChangeAllowed,
                                                         long time)
    {
        try {
            if (processSynchronousLightweightTransferMethod == null) {
                processSynchronousLightweightTransferMethod = (Method) AccessController
                        .doPrivileged(new PrivilegedExceptionAction() {
                            public Object run() throws IllegalAccessException,
                                    NoSuchMethodException {
                                Method m = KeyboardFocusManager.class
                                        .getDeclaredMethod(
                                                "processSynchronousLightweightTransfer",
                                                new Class[] { Component.class,
                                                        Component.class,
                                                        Boolean.TYPE,
                                                        Boolean.TYPE, Long.TYPE });
                                m.setAccessible(true);
                                return m;
                            }
                        });
            }
            Object[] params = new Object[] { target, lightweightChild,
                    Boolean.valueOf(temporary),
                    Boolean.valueOf(focusedWindowChangeAllowed),
                    Long.valueOf(time) };
            return ((Boolean) processSynchronousLightweightTransferMethod
                    .invoke(null, params)).booleanValue();
        } catch (PrivilegedActionException pae) {
            pae.printStackTrace();
            return false;
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            return false;
        } catch (IllegalArgumentException iaee) {
            iaee.printStackTrace();
            return false;
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
            return false;
        }
    }

    static int shouldNativelyFocusHeavyweight(Component heavyweight,
                                              Component descendant,
                                              boolean temporary,
                                              boolean focusedWindowChangeAllowed,
                                              long time,
                                              CausedFocusEvent.Cause cause)
    {
        if (shouldNativelyFocusHeavyweightMethod == null) {
            Class[] arg_types = new Class[] {
                    Component.class, Component.class,
                    Boolean.TYPE, Boolean.TYPE, Long.TYPE,
                    CausedFocusEvent.Cause.class
            };

            try {
                shouldNativelyFocusHeavyweightMethod = KeyboardFocusManager.class
                        .getDeclaredMethod("shouldNativelyFocusHeavyweight",
                                arg_types);
                shouldNativelyFocusHeavyweightMethod.setAccessible(true);
                
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        Object[] args = new Object[] {
                heavyweight, descendant,
                Boolean.valueOf(temporary),
                Boolean.valueOf(focusedWindowChangeAllowed),
                Long.valueOf(time), cause
        };

        int result = SNFH_FAILURE;
        if (shouldNativelyFocusHeavyweightMethod != null) {
            try {
                result = ((Integer)
                        shouldNativelyFocusHeavyweightMethod.invoke(null, args)).intValue();
            } catch (IllegalAccessException e) {
                assert false;
                
            } catch (InvocationTargetException e) {
                assert false;
            }
        }

        return result;
    }

}
