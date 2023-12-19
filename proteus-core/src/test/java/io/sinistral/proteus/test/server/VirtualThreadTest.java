package io.sinistral.proteus.test.server;

import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

public class VirtualThreadTest {

    private static final Logger logger = LoggerFactory.getLogger(VirtualThreadTest.class.getName());

    static class Runner implements Runnable {


        @Override
        public void run() {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.info("Hello from {} from group {}", Thread.currentThread().getName(), Thread.currentThread().getThreadGroup());
        }
    }


    @Test
    void virtualThreadGroup() throws Exception {

        ThreadGroup virtualThreadGroup = Thread.ofVirtual().unstarted(() -> {
        }).getThreadGroup();

        Xnio xnio = Xnio.getInstance();

        XnioWorker worker = xnio.createWorkerBuilder().setThreadGroup(virtualThreadGroup).build();

        for (int i = 0; i < 100; i++) {
            worker.execute(new Runner());
        }

        Thread.sleep(10000L);

        logger.info("complete");


    }

    @Test
    void proteusVT() throws Exception {
        ThreadGroup virtualThreadGroup = Thread.ofVirtual().unstarted(() -> {
        }).getThreadGroup();

        Xnio xnio = Xnio.getInstance();

        XnioWorker.Builder builder = xnio.createWorkerBuilder().setThreadGroup(virtualThreadGroup).setDaemon(false).setWorkerName("proteus-vt");

        ProteusXnioWorker worker = new ProteusXnioWorker(builder);


        for (int i = 0; i < 100; i++) {
            worker.execute(new Runner());
        }

        Thread.sleep(10000L);

        logger.info("complete");
    }
}
