package nl.mwinkels.photomanager;


import nl.mwinkels.photomanager.config.FaceRecognitionViewConfig;
import nl.mwinkels.photomanager.dao.PhotoDao;
import nl.mwinkels.photomanager.data.Photo;
import org.hibernate.ScrollableResults;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;


public class PhotoViewer {

    private final Path photoPath;

    private final PhotoDao photoDao;

    private final PlatformTransactionManager transactionManager;

    public PhotoViewer(Path photoPath, PhotoDao photoDao, PlatformTransactionManager transactionManager) {
        this.photoPath = photoPath;
        this.photoDao = photoDao;
        this.transactionManager = transactionManager;
    }

    public static void main(String... args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().getPropertySources().addFirst(new SimpleCommandLinePropertySource(args));
        context.register(FaceRecognitionViewConfig.class);
        context.refresh();

        PhotoViewer view = context.getAutowireCapableBeanFactory().createBean(PhotoViewer.class);
        view.start();
    }

    private void start() throws IOException, InterruptedException {
        Object lock = new Object();
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try (ScrollableResults results = photoDao.scrollAll()) {
            while (results.next()) {
                Photo photo = (Photo) results.get(0);
                BufferedImage image = ImageIO.read(photo.getPath(photoPath).toFile());
                if (image == null) {
                    continue;
                }
                PhotoFacesWindow photoFacesWindow = new PhotoFacesWindow(image, photo.getFaces());
                photoFacesWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                photoFacesWindow.setTitle(String.format("%s [%s] %s", photo.getFileName(), photo.getTaken(), photo.getSuggestions()));
                photoFacesWindow.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            synchronized (lock) {
                                lock.notify();
                            }
                        }
                    }
                });
                photoFacesWindow.setVisible(true);
                synchronized (lock) {
                    lock.wait();
                }
                photoFacesWindow.dispose();
            }
        }
        transactionManager.rollback(transaction);
    }
}
