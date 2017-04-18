package ru.skuptsov.telegram.bot.goodstory.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

@Service
public class DirWatcherService {
    private final static Logger log = LoggerFactory.getLogger(DirWatcherService.class);

    private static final String newBooksDir = "books";
    private static final Path BOOKS_DIR = Paths.get(newBooksDir);
    private volatile boolean isRunning;

    @Autowired
    private StorySaver storySaver;

    private void runWatcher() {
        new Thread(new Watcher()).start();
    }

    private final class Watcher implements Runnable {

        FileConsumer fileConsumer = new FileConsumer();

        @Override
        public void run() {
            while (isRunning) {

                try {
                    Files.newDirectoryStream(BOOKS_DIR, path -> path.toFile().isFile())
                            .forEach((fileConsumer));
                } catch (IOException e) {
                    log.error("Exception while deleting file", e);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private final class FileConsumer implements Consumer<Path> {

        @Override
        public void accept(Path path) {
            storySaver.processFile(path);
            try {
                Files.delete(path);
            } catch (IOException e) {
                log.error("Exception while deleting file", e);
            }

        }
    }

    @PostConstruct
    private void init() throws IOException {
        isRunning = true;
        runWatcher();
    }

    @PreDestroy
    private void destroy() {
        isRunning = false;
    }
}
