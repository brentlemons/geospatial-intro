package com.fltck.demo.geospatial.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.common.settings.Settings;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fltck.demo.geospatial.service.impl.ElasticsearchRepositoryServiceImpl;

/**
 * @author brentlemons
 *
 */
@Configuration
public class ElasticsearchConfiguration {
	@Value("${elasticsearch.host}")
	private String host;

	@Value("${elasticsearch.port}")
	private int port;

	@Value("${elasticsearch.index}")
	private String elasticsearchIndex;

	@Value("${elasticsearch.type}")
	private String elasticsearchType;

	@Bean
	public TransportClient elasticsearchClient() throws UnknownHostException {
		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
	            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
	    return client;
	}
	
	@Bean
	ElasticsearchRepositoryServiceImpl elasticsearchRepositoryService(Client elasticsearchClient) {
		ElasticsearchRepositoryServiceImpl repository = new ElasticsearchRepositoryServiceImpl(elasticsearchClient);
		repository.setIndex(elasticsearchIndex);
		repository.setType(elasticsearchType);
		return repository;
	}
	
}