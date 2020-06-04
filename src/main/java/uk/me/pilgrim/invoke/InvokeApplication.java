package uk.me.pilgrim.invoke;

import lombok.extern.log4j.Log4j2;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;

@Log4j2
@SpringBootApplication
public class InvokeApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(InvokeApplication.class, args);
    }

    @Value("classpath:test.invoke")
    private Resource resource;

    @Override
    public void run(String... args) throws Exception {

        CharStream input = CharStreams.fromStream(resource.getInputStream());
        InvokeLexer lexer = new InvokeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        InvokeParser parser = new InvokeParser(tokens);
        InvokeParser.DocumentContext document = parser.document();

        LOGGER.info(document.toStringTree(parser));
    }
}
