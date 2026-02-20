import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class FileOrganizer {

    // Mapa de extensões para categorias
    private static final Map<String, String> CATEGORIES = new HashMap<>();

    static {
        CATEGORIES.put("pdf", "Documentos");
        CATEGORIES.put("docx", "Documentos");
        CATEGORIES.put("txt", "Documentos");
        CATEGORIES.put("jpg", "Imagens");
        CATEGORIES.put("png", "Imagens");
        CATEGORIES.put("mp4", "Videos");
        CATEGORIES.put("zip", "Compactados");
        CATEGORIES.put("exe", "Executaveis");
    }

    public static void main(String[] args) {
        // Altere para o caminho da sua pasta de Downloads ou use "." para a pasta atual
        Path targetDir = Paths.get(System.getProperty("user.home"), "Downloads");

        System.out.println("Iniciando organização em: " + targetDir);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(targetDir)) {
            for (Path file : stream) {
                if (!Files.isDirectory(file)) {
                    organizeFile(file);
                }
            }
            System.out.println("Organização concluída com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao acessar o diretório: " + e.getMessage());
        }
    }

    private static void organizeFile(Path file) {
        String fileName = file.getFileName().toString();
        String extension = getExtension(fileName);
        
        String category = CATEGORIES.getOrDefault(extension, "Outros");
        Path destinationDir = file.getParent().resolve(category);

        try {
            if (Files.notExists(destinationDir)) {
                Files.createDirectories(destinationDir);
            }

            Path targetPath = destinationDir.resolve(fileName);
            Files.move(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("  [MOVido] " + fileName + " -> " + category);
            
        } catch (IOException e) {
            System.err.println("  [ERRO] Não foi possível mover " + fileName + ": " + e.getMessage());
        }
    }

    private static String getExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        return (i > 0) ? fileName.substring(i + 1).toLowerCase() : "";
    }
}
