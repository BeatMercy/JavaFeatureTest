package IText;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * description...
 *
 * @Author BeatMercy
 * @Date 2019/8/25 1:07
 * @Version 1.0
 */
public class EditPdfTest {

    String templatePath = "C:\\Users\\BeatMercy\\Desktop\\mess\\豆芽\\offerTemplate.pdf";
    String destPath = "C:\\Users\\BeatMercy\\Desktop\\mess\\豆芽\\offerTemplateDest.pdf";
    String imgPath = "C:\\Users\\BeatMercy\\Desktop\\mess\\豆芽\\offerImg.jpg";

    public PdfReader getTemplateReader() throws IOException {
        return new PdfReader(templatePath);
    }

    @Test
    public void replaceAllImg() throws IOException {
        //根据一个pdfreader创建一个pdfStamper.用来生成新的pdf.
        PdfDocument readerDoc = new PdfDocument(getTemplateReader());
        PdfDocument writerDoc = new PdfDocument(getTemplateReader(), new PdfWriter(destPath));

        Document doc = null;

        PdfDocument curPageDoc = writerDoc.getPage(1).getDocument();
        doc = new Document(writerDoc);
        PdfDictionary pageDict = curPageDoc.getPage(1).getPdfObject();

        PdfDictionary resources = pageDict.getAsDictionary(PdfName.Resources);
        PdfDictionary xobjects = resources.getAsDictionary(PdfName.XObject);

        Iterator<PdfName> imgRefIt = xobjects.keySet().iterator();
        while (imgRefIt.hasNext()) {
            PdfName n = imgRefIt.next();
            System.out.println(n.getValue());
            PdfStream stream = xobjects.getAsStream(n);

            Image img = replaceImg(imgPath);
            replaceStream(stream, img.getXObject().getPdfObject());

            // 覆盖文字
            Paragraph paragraph = new Paragraph("吉利帝豪orials Point originated from the idea that there exists \n" +
                    "      a class of readers who respond better to online content and prefer to learn \n" +
                    "      new skills at their ow");
            paragraph.setFixedPosition(0f, 412f, 120f);
            doc.add(paragraph);
        }
        doc.close();
    }

    @Test
    public void AddText() throws IOException {
        PdfDocument writerDoc = new PdfDocument(getTemplateReader(), new PdfWriter(destPath));
        Document doc = null;
        for (int i = 1; i <= writerDoc.getNumberOfPages(); i++) {
            PdfDocument curPageDoc = writerDoc.getPage(i).getDocument();
            doc = new Document(curPageDoc);
            Paragraph paragraph = new Paragraph("吉利帝豪");
            paragraph.setFixedPosition(i, 12f, 0f);
            doc.add(paragraph);
        }
        doc.close();

    }


    @Test
    public void printAllObj() throws IOException {
        PdfDocument writerDoc = new PdfDocument(getTemplateReader(), new PdfWriter(destPath));
        for (int i = 1; i <= writerDoc.getNumberOfPages(); i++) {
            PdfPage page = writerDoc.getPage(i);
            PdfDictionary pageDict = page.getPdfObject();
            // 页面中的所有pdf元素
            Set<PdfName> nameSet = pageDict.keySet();
            PdfDictionary resources = pageDict.getAsDictionary(PdfName.Resources);
            PdfDictionary fonts = resources.getAsDictionary(PdfName.Font);
            Iterator<PdfName> pNameIt = fonts.keySet().iterator();
            while (pNameIt.hasNext()) {
                PdfName n = pNameIt.next();

                System.out.println("content pName: " + n.getValue());
            }

//            System.out.println("content pName: " + PdfTextExtractor.getTextFromPage(page));

        }
    }

    private void printPdfName(Set<PdfName> pdfNames) {
        Iterator<PdfName> pNameIt = pdfNames.iterator();
        while (pNameIt.hasNext()) {
            PdfName n = pNameIt.next();
            System.out.println("pName: " + n.getValue());
        }

    }

    public Image replaceImg(String theImgPath) throws IOException {
        BufferedImage bi = ImageIO.read(new File(theImgPath));
        BufferedImage newBi = new BufferedImage(bi.getWidth(), bi.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        newBi.getGraphics().drawImage(bi, 0, 0, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(newBi, "png", baos);
        return new Image(ImageDataFactory.create(baos.toByteArray()));
    }

    public void replaceStream(PdfStream oriStream, PdfStream destStream) {
        oriStream.clear();
        oriStream.setData(destStream.getBytes());
        for (PdfName name : destStream.keySet()) {
            oriStream.put(name, destStream.get(name));
        }
    }
}
