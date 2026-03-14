package io.pragra.SwaggerDemo;

public class Test implements Runnable{

  volatile boolean running = true;
    @Override
    public void run() {
        System.out.println("Thread Started");
        while (running){

        }
        System.out.println("Thread Stopped");
    }

    public void stop(){ running = false;}

    public static void main(String[] args) throws InterruptedException {
        Test test  = new Test();
        Thread thread = new Thread(test);
        thread.start();

        Thread.sleep(1000);
        test.stop();
        System.out.println("Stop signal sent");
    }
}
