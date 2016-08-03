package nl.mwinkels.photomanager.loader;


import nl.mwinkels.photomanager.data.DetectedFace;
import nl.mwinkels.photomanager.data.Photo;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_objdetect;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_core.CvSeq;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvLoad;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvResize;
import static org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;
import static org.bytedeco.javacpp.opencv_objdetect.cvHaarDetectObjects;


public class FacesRecognizer {

    static {
        Loader.load(opencv_objdetect.class);
    }

    private final Path photoPath;

    private final CvHaarClassifierCascade classifier;

    public FacesRecognizer(Path photoPath) {
        this.photoPath = photoPath;
        try {
            classifier = new CvHaarClassifierCascade(cvLoad(
                    new File(FacesRecognizer.class.getResource("/haarcascade/frontalface_alt.xml").toURI()).getAbsolutePath()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Photo process(Photo photo) {
        IplImage image = cvLoadImage(photo.getPath(photoPath).toString());
        if (image != null) {
            int width = image.width(), height = image.height();
            double scale = Math.max(width, height) / 640d;
            scale = scale < 1 ? 1 : scale;

            IplImage grayImage = IplImage.create(width, height, IPL_DEPTH_8U, 1);
            cvCvtColor(image, grayImage, CV_BGR2GRAY);
            IplImage smallImage = IplImage.create(i(width / scale), i(height / scale), IPL_DEPTH_8U, 1);
            cvResize(grayImage, smallImage, CV_INTER_AREA);
            CvSeq faces = cvHaarDetectObjects(smallImage, classifier, cvCreateMemStorage());
            int nrFaces = faces.total();
            for (int i = 0; i < nrFaces; i++) {
                CvRect r = new CvRect(cvGetSeqElem(faces, i));
                photo.getFaces().add(new DetectedFace(photo, i(r.x() * scale), i(r.y() * scale), i(r.width() * scale), i(r.height() * scale)));
            }
        }
        return photo;
    }

    private int i(double v) {
        return (int) v;
    }
}
