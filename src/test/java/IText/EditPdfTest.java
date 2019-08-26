package IText;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFontFamilies;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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

    private class DemandOffer {
        public String vehicleBuyerName = "大叶榕供应链科技（深圳）公司";
        public String clientName = "乐珈彤";
        public String offerNo = "DYR-ZG-181220-01";
        public String invalidDate = "2018/12/20 16：00";
        public String vehicleModelImg = "/asdasdasdasd";
        public String vehicleModelBrandName = "吉利博瑞";
        public String vehicleModelGuidPrice = "232800";
        public String vehicleModelOfferPrice = "195500";
        public String vehicleModelSpec = "中规";
        public String vehicleModelYear = "2018年款";
        public String vehicleModelOuter = "外观颜色蓝色";
        public String vehicleModelInner = "内饰颜色不限";
        public String vehicleModelNote = "1.8L 133马力 \n" +
                "170扭矩 \n" +
                "L4缸 6挡双离合 \n" +
                "国五 \n" +
                "4725*1802*1478 \n" +
                "4门5座3厢  \n";

        public String vehicleModelName = "2018款1.5TPHEV 耀领版";
        public String deliverInfo = "物流包运";
        public String vehicleDocument = "单证，钥匙一把";
        public String eachDeposit = "500";
        public String eachParkFee = "15";
        public String expressFee = "0";
        public String eachCheckoutFee = "0";
        public String buyerFeePercent = "0.06%" + "（资金垫付开始日进行计算，提车后停止计算）";
        public String earnestMoneyPercent = "30%" + "（包含意向定金）";
        public String deliverArea = "深圳南山" + "盐和姜乐阿斯顿安抚5号";
        public String latestDeliverDays = "25" + "(垫资开始日起，25天内车辆须提走)";
        public String invoiceType = "增值税发票（不加税点）";

        public String demandAmount = "100";
        public String note = "无备注信息信息信息信息信息信息信息信" +
                "息信息信息信息信息信息信息信息信息信息信息信息信息";


    }

    public PdfReader getTemplateReader() throws IOException {
        return new PdfReader(templatePath);
    }

    @Test
    public void makeOfferPdf() throws IOException {
        PdfDocument reader = new PdfDocument(getTemplateReader());
        PdfDocument writerDoc = new PdfDocument(getTemplateReader(), new PdfWriter(destPath));
        Document curDoc = null;
        for (int i = 1; i <= writerDoc.getNumberOfPages(); i++) {
            Rectangle size = writerDoc.getPage(i).getPageSize();
            /*IText 以 left bottom 作为相对位移基准线*/
            System.out.println("page " + i + " size is: " + size.getWidth() + "," + size.getHeight());
            System.out.println("page " + i + " position is: " + size.getLeft() + "," + size.getTop());
            PdfDocument curPageDoc = writerDoc.getPage(i).getDocument();

            curDoc = new Document(curPageDoc);

            Div divBlock = new Div();
            float[] pointColumnWidths = {150F, 150F, 150F};
            Table table = new Table(pointColumnWidths);

            // Adding cells to the table
            table.addCell(new Cell().add(new Paragraph("Name")));
            table.addCell(new Cell().add(new Paragraph("Raju")));
            table.addCell(new Cell().add(new Paragraph("Id")));
            table.addCell(new Cell().add(new Paragraph("1001")));
            table.addCell(new Cell().add(new Paragraph("Designation")));
            table.addCell(new Cell().add(new Paragraph("Programmer")));

            // make List item
            com.itextpdf.layout.element.List listDiv = new com.itextpdf.layout.element.List();
            listDiv.setListSymbol("> ");
            listDiv.setUnderline();
            listDiv.add(new ListItem("ListItemHere"));
            listDiv.add("ListItemHere");
            listDiv.add("ListItemHere");
            table.addCell(new Cell().add(listDiv));
            divBlock.add(table);
            divBlock.setRelativePosition(10f, 200f, 0f, 0f);
            Div headerBlock = new Div();
            headerBlock.setFixedPosition(i, 20f, size.getHeight() - 20f, size.getWidth())
                    .setTextAlignment(TextAlignment.CENTER);
            headerBlock.add(new Paragraph("ss标s题d").setFont(getChineseFont()));

            curDoc.add(divBlock);
            curDoc.add(headerBlock);
        }

        curDoc.close();

    }

    @Test
    public void replaceTemplate() throws IOException {
        DemandOffer offer = new DemandOffer();

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
//            paragraph.setFixedPosition(0f, 412f, 120f);
            paragraph.setRelativePosition(0f, 0f, 10f, 10f);
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

    public static PdfFont getChineseFont() throws IOException {
        String myFont = "./src/main/resources/simfang.ttf";
        return PdfFontFactory.createFont(myFont, PdfEncodings.IDENTITY_H, false);
    }
}
