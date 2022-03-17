package com.example.elasticsearchWord.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.stereotype.Component;

import com.example.elasticsearchWord.helper.Indices;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = Indices.FILE_INDEX)
@Setting(settingPath = "static/es-settings.json")
@Component
public class Documents {

	private long id;

	private String content;
	private String path;

	
	  public String toString() { return "Document{" + "id=" + id + ", content='" +
	  content + "path=" + path + '\'' + '}'; }
	

	

	public String getPath() {
		return path;
	}




	public void setPath(String path) {
		this.path = path;
	}




	public long getId() {
		return id;
	}

	

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Documents() {
		super();
	}
}
