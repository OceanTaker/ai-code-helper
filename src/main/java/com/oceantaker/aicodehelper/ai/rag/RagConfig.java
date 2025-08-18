package com.oceantaker.aicodehelper.ai.rag;


import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 加载RAG配置
 */

@Configuration
public class RagConfig {

    @Resource
    private EmbeddingModel qwenEmbeddingModel;

    //基于内存的向量存储 需要额外添加langchain4j-core依赖
//    @Resource
//    private EmbeddingStore<TextSegment> embeddingStore;

    //为什么上面那个不行，下面这样就可以
//    @Autowired
//    private EmbeddingModel embeddingModel;
//使用内存向量存储，生产环境建议使用持久化的向量数据库
    private final EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

    // 确保配置类中有类似这样的 bean 定义
//    @Bean
//    public EmbeddingStore<TextSegment> embeddingStore() {
//        return new InMemoryEmbeddingStore<>(); // 或其他实现如 ChromaEmbeddingStore 等
//    }
    @Bean
    public ContentRetriever contentRetriever() {
        // ------ RAG知识库构建阶段 ------
        // 1. 加载文档 使用langchain4j的内置文档加载器，多个文件-s
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/docs");
        // 2. 文档切割：将每个文档按每段进行分割，最大 1000 字符，每次重叠最多 200 个字符
        DocumentByParagraphSplitter documentByParagraphSplitter =
                new DocumentByParagraphSplitter(1000, 200);
        // 3. 自定义文档加载器，把文档转化成向量保存到向量数据库中
        EmbeddingStoreIngestor ingestor =
                EmbeddingStoreIngestor.builder()
                        // 指定文档切割器
                        .documentSplitter(documentByParagraphSplitter)
                        // 为了提高搜索质量，为每个 TextSegment 添加文档名称，拼接到原本的 TextSegment 中，得到新的文本碎片TextSegment
                        // getString("file_name")是固定写法，是文档名称，text是文档内容，metadata是文档的元数据，比如文档的路径，名称，作者，创建时间等等，这里我们只用到了文档名称
                        .textSegmentTransformer(textSegment -> TextSegment.from(textSegment.metadata().getString("file_name") +
                                "\n" + textSegment.text(), textSegment.metadata()))
                        // 使用的向量模型
                        .embeddingModel(qwenEmbeddingModel)
                        // 指定向量数据库
                        .embeddingStore(embeddingStore)
                        .build();
        // 加载文档
        ingestor.ingest(documents);

        // ------ 检索阶段 ------
        // 4. 自定义内容检索器
        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)  // 绑定向量库
                .embeddingModel(qwenEmbeddingModel)  // 向量化用户问题
                .maxResults(5)   // 返回 Top-5 相关片段
                .minScore(0.75)  // 过滤低置信结果（余弦相似度阈值）
                .build();
        return contentRetriever;
    }

}
