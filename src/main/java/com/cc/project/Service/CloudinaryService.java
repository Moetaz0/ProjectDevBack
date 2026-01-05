package com.cc.project.Service;

import com.cc.project.Config.AppProperties;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(AppProperties appProperties) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", appProperties.getCloudinaryCloudName(),
                "api_key", appProperties.getCloudinaryApiKey(),
                "api_secret", appProperties.getCloudinaryApiSecret()));
    }

    /**
     * Upload a file to Cloudinary and return the secure URL.
     * Files are stored in their original format (PDF, Word, images).
     * 
     * @param file   Multipart file to upload
     * @param folder Optional folder name in Cloudinary
     * @return secure URL of the uploaded asset
     */
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        String contentType = file.getContentType();
        String resourceType = determineResourceType(contentType);

        Map<String, Object> options = ObjectUtils.asMap(
                "resource_type", resourceType,
                "folder", folder != null ? folder : "lab_results",
                "use_filename", true,
                "unique_filename", true,
                "format", getFileFormat(file.getOriginalFilename()));
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        Object secureUrl = uploadResult.get("secure_url");
        return secureUrl != null ? secureUrl.toString() : uploadResult.get("url").toString();
    }

    /**
     * Determine the Cloudinary resource type based on MIME type.
     * Images use "image", PDFs and Word docs use "raw" (which preserves original
     * format).
     */
    private String determineResourceType(String contentType) {
        if (contentType == null) {
            return "raw";
        }
        // Images
        if (contentType.startsWith("image/")) {
            return "image";
        }
        // PDF and Word documents must use "raw" resource type
        if ("application/pdf".equalsIgnoreCase(contentType)
                || "application/msword".equalsIgnoreCase(contentType)
                || "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        .equalsIgnoreCase(contentType)) {
            return "raw";
        }
        return "raw";
    }

    /**
     * Extract file format/extension from filename
     */
    private String getFileFormat(String filename) {
        if (filename == null || !filename.contains(".")) {
            return null;
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}
