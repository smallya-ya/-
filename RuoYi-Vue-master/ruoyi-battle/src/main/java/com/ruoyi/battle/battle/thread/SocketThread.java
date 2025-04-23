package com.ruoyi.battle.battle.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import  com.ruoyi.battle.battle.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.*;

@Service
public class SocketThread extends Thread {
    // 默认端口设置为9090
    private int port = 9999;
    // 使用阻塞队列存储演习数据
    private final BlockingQueue<String> dataQueue = new LinkedBlockingQueue<>();
    // 日志对象
    protected final Logger log = LoggerFactory.getLogger(getClass());
    // 注入的战斗服务
    @Autowired
    private BattleService service;
    // 定时任务执行器
    private ScheduledExecutorService executor;

    // 构造函数，初始化端口并启动线程
    public SocketThread() {
        this.port = port;
//        start();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Server started on port:{}", port);

            // 初始化 ScheduledExecutorService
            executor = Executors.newSingleThreadScheduledExecutor();

            // 定义一个可运行任务，用于定期获取演习数据
            Runnable task = () -> {
                try {
                    // 记录开始获取演习数据的日志
                    log.info("开始获取演习数据");
                    // 确定查询起始时间为当前时间的前五秒
                    LocalDateTime fromTime = LocalDateTime.now().minusSeconds(5);
                    // 启动周期性演习详情查询，并获取查询结果
                    String battleDetails = service.startPeriodicBattleDetailQuery(fromTime);

                    // 记录演习数据获取成功的日志
                    log.info("演习数据获取成功");
                    log.info("演习数据：{}", battleDetails);
                    // 如果查询到的演习数据不为空，则将其放入数据队列
                    if (battleDetails != null) {
                        dataQueue.offer(battleDetails);
                    }
                } catch (Exception e) {
                    // 记录任务执行过程中的异常日志
                    log.error("Error in task execution:", e);
                }
            };

            // 安排任务在10秒之后开始执行，并且后续每5秒执行一次
            executor.scheduleAtFixedRate(task, 10, 5, TimeUnit.SECONDS);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                log.info("Client connected:{}", clientSocket.getInetAddress());
                // 创建一个新的线程来处理客户端连接
                Thread clientHandler = new ClientHandler(clientSocket, dataQueue);
                clientHandler.start();
            }
        } catch (IOException e) {
            if (e instanceof BindException && ((BindException) e).getMessage().contains("Address already in use")) {
                log.error("端口 {} 已经被占用，请检查是否有其他服务正在使用此端口", port);
                // 可以在这里选择退出程序或者尝试使用其他端口
            } else {
                log.error("服务器启动失败", e);
            }
        } finally {
            // 关闭 ScheduledExecutorService
            if (executor != null && !executor.isShutdown()) {
                executor.shutdown();
            }
        }
    }

    // 处理客户端连接的内部类
    class ClientHandler extends Thread {
        private final Socket socket;
        private final BlockingQueue<String> dataQueue;

        public ClientHandler(Socket socket, BlockingQueue<String> dataQueue) {
            this.socket = socket;
            this.dataQueue = dataQueue;
        }

        @Override
        /**
         * 处理客户端请求的主逻辑函数
         * 从数据队列中不断取数据，并将其发送给客户端，直到队列中没有数据为止
         */
        public void run() {
            try (OutputStream outputStream = socket.getOutputStream()) {
                String data;
                // 循环从数据队列中取数据，如果队列为空则等待
                while ((data = dataQueue.take()) != null) {
                    // 将字符串数据转换为字节数组
                    byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
                    // 将字节数组写入到输出流，即发送给客户端
                    outputStream.write(bytes);
                    // 刷新输出流，确保数据发送
                    outputStream.flush();
                    // 记录日志，表示数据发送成功
                    log.info("Sent successfully");
                }
            } catch (IOException | InterruptedException e) {
                // 如果发生异常，记录错误日志
                log.error("Error handling client:", e);
            }
        }

        // 获取数据队列的方法
        public BlockingQueue<String> getDataQueue() {
            return dataQueue;
        }
    }
}
