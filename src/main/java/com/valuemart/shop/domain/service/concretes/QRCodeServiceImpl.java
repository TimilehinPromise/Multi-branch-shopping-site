package com.valuemart.shop.domain.service.concretes;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.valuemart.shop.domain.service.abstracts.QRCodeService;
import com.valuemart.shop.providers.cloudinary.CloudinaryConfig;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class QRCodeServiceImpl implements QRCodeService {


    private final Cloudinary cloudinary;


    private final CloudinaryConfig cloudinaryConfig;

    public QRCodeServiceImpl(CloudinaryConfig cloudinaryConfig) {
        this.cloudinaryConfig = cloudinaryConfig;
        // Initialize Cloudinary with your account details

        System.out.println(this.cloudinaryConfig.getKey());
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", this.cloudinaryConfig.getName(),
                "api_key", this.cloudinaryConfig.getKey(),
                "api_secret", this.cloudinaryConfig.getSecret()));
    }

    @Override
    public String generateQRCodeImageAndUpload(String text)  {
        System.out.println(cloudinary.config.apiKey);

        int width = 300;
        int height = 300;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

        try (ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            // Upload to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(pngData, ObjectUtils.emptyMap());

            String url = (String) uploadResult.get("url");
            System.out.println("url : " +url);
            // Return the URL of the uploaded image
            return url;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
