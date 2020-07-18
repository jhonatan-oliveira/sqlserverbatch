package br.com.jhonatan;

import java.io.BufferedReader;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import static br.com.jhonatan.SqlServerConn.getConnection;

@Component
@PropertySource("classpath:application.properties")
public class SqlServerUtil extends RouteBuilder {

    @Value("${camelFolderIn}")
    private String camelFolderIn;

    @Value("${camelFolderOut}")
    private String camelFolderOut;

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlServerUtil.class);

    @Override
    public void configure() {

        from(camelFolderIn).
                process(new Processor() {
                            public void process(Exchange exchange) throws Exception {

                                File file = exchange.getIn().getBody(File.class);

                                LOGGER.info("Início do processamento do arquivo: " + file.getName());

                                Connection conn = getConnection();

                                try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
                                     PreparedStatement ps = conn.prepareStatement("INSERT INTO REGISTROS VALUES (?,?)");) {

                                    String texto = "";
                                    // AtomicInteger contador = new AtomicInteger(0);
                                    // AtomicInteger k = new AtomicInteger(1);
                                    int cont = 0;
                                    int[] linhas;
                                    int cont2 = 0;
                                    System.out.println("Inicio B " + System.currentTimeMillis());

                                    while ((texto = br.readLine()) != null) {
                                        try {
                                            ps.setInt(1, Integer.parseInt(texto));
                                            ps.setString(2, "teste");
                                            ps.addBatch();

                                            if (cont % 10000 == 0 && cont > 0) {
                                                linhas = ps.executeBatch();
                                                cont2 += linhas.length;
                                                System.out.println("entrei");
                                            }
                                            cont++;
                                        }
                                        catch (SQLServerException ex) {

                                        }
                                        catch (BatchUpdateException ex) {
                                            System.out.println(ex.getMessage());
                                        }
                                    }

                                    try {
                                        linhas = ps.executeBatch();
                                        cont2 += linhas.length;

                                    } catch (BatchUpdateException ex) {
                                        System.out.println(ex.getMessage());

                                    }
                                    LOGGER.info("Fim do processamento do arquivo: {}" + file.getName());
                                    System.out.println("Linhas inseridas " + cont2);

                                    System.out.println("fim B " + System.currentTimeMillis());
                                }
                            }
                        }
                ).
                to(camelFolderOut);

    }
}