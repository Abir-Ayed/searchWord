package com.example.elasticsearchWord.controller;

import java.io.IOException;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.elasticsearchWord.utils.BusinessException;
import com.example.elasticsearchWord.Service.FileService;
import com.example.elasticsearchWord.entities.Documents;
import com.example.elasticsearchWord.search.SearchRequestDTO;
@RestController
@RequestMapping(path = "/api")
public class FileController {
	
	  private final FileService service;

	    @Autowired
	    public FileController(FileService service) {
	        this.service = service;
	    }

	/*    @PostMapping("/index")
	    public void index(@RequestBody final File file) {
	        service.index(file);
	    }*/
	
	    @PostMapping("/files")
	    public void ReadFile(@RequestParam("file") MultipartFile file) throws BusinessException, IOException {
	        service.ReadJsonFile(file);
	    }
	    @PostMapping("/search")
	    public List<Documents> search(@RequestBody final SearchRequestDTO dto) {
	        return service.search(dto);
	    }
	    
	/*    @PostMapping("/upload")
	    public void UploadFile(@RequestParam("file")MultipartFile file) {
	    	 service.singleFileUpload(file);
	    }*/

}
