package com.reporteloya.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Cloudinary cloudinary;

    private static final List<String> TIPOS_PERMITIDOS = List.of(
            "image/jpeg",
            "image/png",
            "image/webp");

    public FileStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String guardarArchivo(MultipartFile file, Long reporteId) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("Archivo vacío");
        }

        if (!TIPOS_PERMITIDOS.contains(file.getContentType())) {
            throw new RuntimeException("Tipo de archivo no permitido");
        }

        String publicId = "reporte_" + reporteId + "_" + UUID.randomUUID().toString();

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", publicId,
                        "folder", "reporteloya",
                        "resource_type", "image"
                )
        );

        String secureUrl = (String) uploadResult.get("secure_url");

        return secureUrl;
    }

    public String guardarFotoPerfil(MultipartFile file, Long usuarioId) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("Archivo vacío");
        }

        if (!TIPOS_PERMITIDOS.contains(file.getContentType())) {
            throw new RuntimeException("Tipo de archivo no permitido");
        }

        String publicId = "perfil_" + usuarioId + "_" + UUID.randomUUID().toString();

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", publicId,
                        "folder", "reporteloya/perfiles",
                        "resource_type", "image",
                        "transformation", new Transformation().width(200).height(200).crop("fill").gravity("face")
                )
        );

        String secureUrl = (String) uploadResult.get("secure_url");

        return secureUrl;
    }

    public String guardarEvidencia(MultipartFile file, Long reporteId, int indice) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("Archivo vacío");
        }

        if (!TIPOS_PERMITIDOS.contains(file.getContentType())) {
            throw new RuntimeException("Tipo de archivo no permitido");
        }

        String publicId = "evidencia_" + reporteId + "_" + indice + "_" + UUID.randomUUID().toString();

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", publicId,
                        "folder", "reporteloya/evidencias",
                        "resource_type", "image"
                )
        );

        String secureUrl = (String) uploadResult.get("secure_url");

        return secureUrl;
    }

    public String guardarFotoPerfil(byte[] imageBytes, String contentType, Long usuarioId) throws IOException {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new RuntimeException("Archivo vacío");
        }

        if (!TIPOS_PERMITIDOS.contains(contentType)) {
            throw new RuntimeException("Tipo de archivo no permitido");
        }

        String publicId = "perfil_" + usuarioId + "_" + UUID.randomUUID().toString();

        Map uploadResult = cloudinary.uploader().upload(
                imageBytes,
                ObjectUtils.asMap(
                        "public_id", publicId,
                        "folder", "reporteloya/perfiles",
                        "resource_type", "image",
                        "transformation", new Transformation().width(200).height(200).crop("fill").gravity("face")
                )
        );

        return (String) uploadResult.get("secure_url");
    }
}