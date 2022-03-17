package com.example.elasticsearchWord.utils;

import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;
import com.spire.doc.Document;

public class FileUtils {

	private static final String ERROR_MSG_START = "Loading Testfile encounters problems: ";
	private static String UPLOADED_FOLDER = "D://";

	public static String readFileInClasspath2String(String folderWithEndingSlash, String fileName)
			throws BusinessException {
		String file;
		try {
			Path filePath = buildPath(folderWithEndingSlash, fileName);
			file = Files.lines(filePath).collect(Collectors.joining());
		} catch (Exception exception) {
			throw new BusinessException(ERROR_MSG_START + exception.getMessage(), exception);
		}
		return file;
	}

	public static InputStream readFileInClasspath2InputStream(String folderWithEndingSlash, String fileName)
			throws BusinessException {
		InputStream inputStream = null;
		try {
			Path filePath = buildPath(folderWithEndingSlash, fileName);
			inputStream = Files.newInputStream(filePath);
		} catch (Exception exception) {
			throw new BusinessException(ERROR_MSG_START + exception.getMessage(), exception);
		}
		return inputStream;
	}

	public static byte[] readFileInClasspath2Bytes(String folderWithEndingSlash, String fileName)
			throws BusinessException {
		byte[] dummyResponse = null;
		try {
			Path filePath = buildPath(folderWithEndingSlash, fileName);
			dummyResponse = Files.readAllBytes(filePath);
		} catch (Exception exception) {
			throw new BusinessException(ERROR_MSG_START + exception.getMessage(), exception);
		}
		return dummyResponse;
	}

	private static Path buildPath(String folderWithEndingSlash, String fileName)
			throws URISyntaxException, BusinessException, IOException {
		URI fileUri = buildFileUri(folderWithEndingSlash, fileName);
		// We have to create a FileSystem, otherwise we´ll get a
		// FileSystemNotFoundException, when running SpringBoot-fatjar with java -jar
		// http://stackoverflow.com/questions/25032716/getting-filesystemnotfoundexception-from-zipfilesystemprovider-when-creating-a-p
		// http://docs.oracle.com/javase/7/docs/technotes/guides/io/fsp/zipfilesystemprovider.html
		if (fileUri.toString().startsWith("jar")) {
			Map<String, String> env = new HashMap<String, String>();
			env.put("create", "true");
			FileSystems.newFileSystem(fileUri, env);
		}
		return Paths.get(fileUri);
	}

	private static URI buildFileUri(String folderWithEndingSlash, String fileName)
			throws URISyntaxException, BusinessException {
		URL fileInClasspath = FileUtils.class.getClassLoader().getResource(folderWithEndingSlash + fileName);
		if (fileInClasspath == null)
			throw new BusinessException("Filepath seems to be wrong.");
		return fileInClasspath.toURI();
	}

	/// Upload File
	public static InputStream singleFileUpload(MultipartFile file) throws IOException {
		InputStream inputStream = null;

		byte[] bytes = file.getBytes();
		System.out.println("bytes" + bytes);

		Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
		System.out.println("path file" + path);
		Files.write(path, bytes);
		inputStream = Files.newInputStream(path);

		return inputStream;
	}

	public static String ReadPdf(MultipartFile file) throws IOException {
		byte[] data = file.getBytes();

		// byte[] to string
		System.out.println("data" + data);

		Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
		File pdfFile = new File(String.valueOf(path));

		Files.write(path, data);
		PDDocument pdDocument = PDDocument.load(pdfFile);
		System.out.println("pdDocument" + pdDocument);
		PDFTextStripper pdfStripper = new PDFTextStripper();

		String text = pdfStripper.getText(pdDocument);
		return text;

	}

	public static String ReadDocx(MultipartFile file) throws IOException {
		Document document = new Document();
		Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());

		document.loadFromFile(String.valueOf(path));

		// Get text from document as string
		String text = document.getText();
		return text;

	}

}
