package com.example.elasticsearchWord.Service;

import java.util.Collections;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.lucene.search.Query;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.elasticsearchWord.entities.Documents;
import com.example.elasticsearchWord.helper.Indices;
import com.example.elasticsearchWord.repository.FileRepository;
import com.example.elasticsearchWord.search.SearchRequestDTO;
import com.example.elasticsearchWord.search.SearchUtil;
import com.example.elasticsearchWord.utils.BusinessException;
import com.example.elasticsearchWord.utils.FileReader;
import com.example.elasticsearchWord.utils.FileUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

@Service

public class FileService {

//	private final FileRepository repository;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final Logger LOG = LoggerFactory.getLogger(FileService.class);
	private Resource file;
	private static String UPLOADED_FOLDER = "D://";
	private final RestHighLevelClient client;

	@Autowired
	public FileService(RestHighLevelClient client) {
		this.client = client;
	}

	public List<Documents> search(final SearchRequestDTO dto) {
		final SearchRequest request = SearchUtil.buildSearchRequest(Indices.FILE_INDEX, dto);

		return searchInternal(request);
	}

	private List<Documents> searchInternal(final SearchRequest request) {
		if (request == null) {
			LOG.error("Failed to build search request");
			return Collections.emptyList();
		}

		try {
			final SearchResponse response = client.search(request, RequestOptions.DEFAULT);

			final SearchHit[] searchHits = response.getHits().getHits();
			final List<Documents> files = new ArrayList<>(searchHits.length);
			for (SearchHit hit : searchHits) {
				files.add(MAPPER.readValue(hit.getSourceAsString(), Documents.class));
			}

			return files;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	/*
	 * @RequestMapping(path = "/files") public boolean ReadJsonFile(MultipartFile
	 * file) throws BusinessException, IOException { String json = ""; String type =
	 * ""; InputStream inputStream = (InputStream) FileUtils.singleFileUpload(file);
	 * System.out.println("inputstream" + inputStream); BufferedReader reader = new
	 * BufferedReader(new InputStreamReader(inputStream)); StringBuilder sb = new
	 * StringBuilder(); String line = reader.readLine();
	 * 
	 * while (line != null) { sb.append(line);
	 * 
	 * line = reader.readLine(); System.out.println("line" + line); } json =
	 * sb.toString(); System.out.println("json" + json); ObjectMapper mapper = new
	 * ObjectMapper();
	 * 
	 * Documents doc = new Documents(); type = file.getContentType();
	 * 
	 * doc.setContent(json); // doc.setType(type); json =
	 * mapper.writeValueAsString(doc);
	 * 
	 * System.out.println("ResultingJSONstring = " + json);
	 * System.out.println(json); final IndexRequest request = new
	 * IndexRequest(Indices.FILE_INDEX); System.out.println("request" + request);
	 * request.source(json, XContentType.JSON); final IndexResponse response =
	 * client.index(request, RequestOptions.DEFAULT); System.out.println("response"
	 * + response); return response != null &&
	 * response.status().equals(RestStatus.OK);
	 * 
	 * }
	 */

	@RequestMapping(path = "/files")
	public boolean ReadJsonFile(MultipartFile file) throws BusinessException, IOException {
		String json = "";
		String type = "";
		String textPDF = "";
		Documents doc = new Documents();
		InputStream inputStream = (InputStream) FileUtils.singleFileUpload(file);
		System.out.println("inputstream" + inputStream);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		type = file.getContentType();
		System.out.println("type" + type);
		if (type.equals("text/plain")
			) {
			StringBuilder sb = new StringBuilder();
			String line = reader.readLine();

			while (line != null) {
				sb.append(line);

				line = reader.readLine();
				System.out.println("line" + line);
			}
			json = sb.toString();
			System.out.println("json" + json);

			doc.setContent(json);
		} 
		else if(type.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
			json = FileUtils.ReadDocx(file);
			doc.setContent(json);
		}
		
		else {
			System.out.println("pdf file");
			json = FileUtils.ReadPdf(file);
			doc.setContent(json);
		}
		Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
		doc.setPath(path.toString());
		ObjectMapper mapper = new ObjectMapper();

		json = mapper.writeValueAsString(doc);

		System.out.println("ResultingJSONstring = " + json);
		final IndexRequest request = new IndexRequest(Indices.FILE_INDEX);
		request.source(json, XContentType.JSON);
		final IndexResponse response = client.index(request, RequestOptions.DEFAULT);
		System.out.println("response" + response);
		return response != null && response.status().equals(RestStatus.OK);

	}

}
