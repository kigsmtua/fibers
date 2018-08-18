/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kiragu.fibers;

import co.paralleluniverse.common.util.Exceptions;
import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberUtil;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author john.kiragu
 */
public class Main {
    
    public static void main(String[] args) throws ExecutionException,InterruptedException {
        final Commands c = new Commands();
        FiberUtil.runInFiber(c::mySuspendableMethod1);
    }
    //This is what hapens when we use a bunch of fibers
    Main() throws SuspendExecution, InterruptedException {
        Fiber.sleep(10);
    }

    
    private static class Commands {
        private void mySuspendableMethod1()
          throws SuspendExecution, InterruptedException {
            myMarkedSyncMethod();
            myMarkedThreadBlockingMethod();
            myUnmarkedSuspendableMethod2();
        }
        private synchronized void myMarkedSyncMethod()
          throws SuspendExecution, InterruptedException {
            Fiber.sleep(10);
        }
        
        private void myMarkedThreadBlockingMethod()
          throws SuspendExecution, InterruptedException {
            Thread.sleep(10);
        }
        
        private void myUnmarkedSuspendableMethod2() {
            mySuspendableMethod3();
        }
        interface MyUnmarkedInterface {
            void myUnmarkedSuspendableInterfaceMethod();
        }
        
        @Suspendable
        private void mySuspendableMethod3() {
            MyUnmarkedInterface i = init();
            i.myUnmarkedSuspendableInterfaceMethod();
        }

        private MyUnmarkedInterface init() {
            return () -> {
                try {
                    Fiber.sleep(10);
                } catch (SuspendExecution | InterruptedException t) {
                    RuntimeException rethrow;
                    rethrow = Exceptions.rethrow(t);
                }
            };
        }
       
    }
    
}
