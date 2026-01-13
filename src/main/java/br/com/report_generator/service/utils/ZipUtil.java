package br.com.report_generator.service.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {

    public static boolean assinaturaDoArquivoCorrespondeZIP(MultipartFile file) {

        try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {
            return zis.getNextEntry() != null;

        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println(e);
            return false;
        }
    }

    public static Map<String, byte[]> extrairArquivosDoZip(MultipartFile arquivo) {

        Map<String, byte[]> map = new HashMap<>();

        try(ZipInputStream zis = new ZipInputStream(arquivo.getInputStream())) {

            ZipEntry entry;

            while((entry = zis.getNextEntry()) != null ) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                zis.transferTo(baos);
                map.put(entry.getName(), baos.toByteArray());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair .zip", e);
        }

        return map;
    }
}
